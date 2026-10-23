package com.cricket.service;

import com.cricket.dto.LiveScoreUpdateDTO;
import com.cricket.dto.ManualBallRequest;
import com.cricket.model.*;
import com.cricket.repository.BallEventRepository;
import com.cricket.repository.MatchRepository;
import com.cricket.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.*;

@Service
public class SimulationEngine {

    private static final Logger logger = LoggerFactory.getLogger(SimulationEngine.class);

    private final MatchRepository matchRepository;
    private final BallEventRepository ballEventRepository;
    private final PlayerRepository playerRepository;
    private final CommentaryGenerator commentaryGenerator;
    private final RealtimeNotificationService notificationService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> activeSimulations = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public SimulationEngine(MatchRepository matchRepository,
                            BallEventRepository ballEventRepository,
                            PlayerRepository playerRepository,
                            CommentaryGenerator commentaryGenerator,
                            RealtimeNotificationService notificationService) {
        this.matchRepository = matchRepository;
        this.ballEventRepository = ballEventRepository;
        this.playerRepository = playerRepository;
        this.commentaryGenerator = commentaryGenerator;
        this.notificationService = notificationService;
    }

    public synchronized void startSimulation(Long matchId, Integer customSpeedMs) {
        CricketMatch match = matchRepository.findById(matchId).orElse(null);
        if (match == null) return;

        if (match.getStatus() == MatchStatus.COMPLETED) {
            logger.info("Match {} is already completed.", matchId);
            return;
        }

        int speedMs = customSpeedMs != null && customSpeedMs >= 300 ? customSpeedMs : match.getSimulationSpeedMs();
        match.setSimulationSpeedMs(speedMs);
        match.setSimulating(true);
        if (match.getStatus() == MatchStatus.UPCOMING || match.getStatus() == MatchStatus.PAUSED) {
            match.setStatus(MatchStatus.LIVE);
        }
        matchRepository.save(match);

        // Cancel existing task if running
        stopScheduledTask(matchId);

        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            try {
                processNextBall(matchId, null);
            } catch (Exception e) {
                logger.error("Error in simulation loop for match {}: {}", matchId, e.getMessage(), e);
            }
        }, 100, speedMs, TimeUnit.MILLISECONDS);

        activeSimulations.put(matchId, task);
        logger.info("Simulation started for match {} at speed {}ms", matchId, speedMs);
    }

    public synchronized void pauseSimulation(Long matchId) {
        stopScheduledTask(matchId);
        matchRepository.findById(matchId).ifPresent(match -> {
            match.setSimulating(false);
            if (match.getStatus() == MatchStatus.LIVE) {
                match.setStatus(MatchStatus.PAUSED);
            }
            matchRepository.save(match);
            notificationService.broadcastLiveScore(createScoreUpdate(match, "Simulation paused"));
        });
        logger.info("Simulation paused for match {}", matchId);
    }

    public synchronized void resumeSimulation(Long matchId) {
        CricketMatch match = matchRepository.findById(matchId).orElse(null);
        if (match != null && match.getStatus() != MatchStatus.COMPLETED) {
            startSimulation(matchId, match.getSimulationSpeedMs());
        }
    }

    public synchronized void resetMatch(Long matchId) {
        stopScheduledTask(matchId);
        CricketMatch match = matchRepository.findById(matchId).orElse(null);
        if (match == null) return;

        ballEventRepository.deleteByMatchId(matchId);

        // Reset match to initial state
        match.setStatus(MatchStatus.UPCOMING);
        match.setSimulating(false);
        match.setCurrentInningsNumber(1);
        match.setTarget(null);
        match.setRequiredRunRate(0.0);
        match.setCurrentRunRate(0.0);
        match.setResultNote("Match reset to initial state");
        match.setWinnerTeamId(null);
        match.setWinnerTeamName(null);
        match.setManOfTheMatch(null);
        match.getRecentBalls().clear();

        // Re-initialize innings
        initializeInningsForMatch(match);
        matchRepository.save(match);

        notificationService.broadcastLiveScore(createScoreUpdate(match, "Match reset"));
        logger.info("Match {} has been reset.", matchId);
    }

    @Transactional
    public synchronized LiveScoreUpdateDTO processNextBall(Long matchId, ManualBallRequest manualRequest) {
        CricketMatch match = matchRepository.findById(matchId).orElse(null);
        if (match == null) return null;

        if (match.getStatus() == MatchStatus.COMPLETED) {
            stopScheduledTask(matchId);
            return createScoreUpdate(match, "Match has already concluded.");
        }

        Innings innings = match.getCurrentInnings();
        if (innings == null) return null;

        if (match.getStatus() == MatchStatus.UPCOMING) {
            match.setStatus(MatchStatus.LIVE);
        }

        // 1. Identify Striker, Non-Striker, Bowler
        BatsmanScore striker = getOrCreateCurrentStriker(innings);
        BatsmanScore nonStriker = getOrCreateCurrentNonStriker(innings, striker);
        BowlerScore bowler = getOrCreateCurrentBowler(innings, match);

        if (striker == null || bowler == null) {
            logger.warn("Unable to resolve active players for match {}", matchId);
            return null;
        }

        // 2. Determine ball outcome (manual or simulated)
        int runsScored;
        ExtraType extraType;
        int extraRuns;
        boolean isWicket;
        WicketType wicketType = null;
        String customCommentary = null;

        if (manualRequest != null) {
            runsScored = manualRequest.getRunsScored();
            extraType = manualRequest.getExtraType() != null ? manualRequest.getExtraType() : ExtraType.NONE;
            extraRuns = manualRequest.getExtraRuns();
            isWicket = manualRequest.isWicket();
            wicketType = manualRequest.getWicketType();
            customCommentary = manualRequest.getCustomCommentary();
        } else {
            // Simulated outcome
            SimulationOutcome outcome = simulateBallOutcome(striker, bowler, match.getFormat());
            runsScored = outcome.runs;
            extraType = outcome.extraType;
            extraRuns = outcome.extraRuns;
            isWicket = outcome.isWicket;
            wicketType = outcome.wicketType;
        }

        boolean isLegalBall = (extraType != ExtraType.WIDE && extraType != ExtraType.NO_BALL);
        int totalBallRuns = runsScored + extraRuns;

        // 3. Update Innings & Bowler & Batsman stats
        innings.setTotalRuns(innings.getTotalRuns() + totalBallRuns);

        if (extraType == ExtraType.WIDE) {
            innings.setWides(innings.getWides() + extraRuns);
            innings.setTotalExtras(innings.getTotalExtras() + extraRuns);
            bowler.recordExtra(ExtraType.WIDE, extraRuns);
        } else if (extraType == ExtraType.NO_BALL) {
            innings.setNoBalls(innings.getNoBalls() + extraRuns);
            innings.setTotalExtras(innings.getTotalExtras() + extraRuns);
            bowler.recordExtra(ExtraType.NO_BALL, extraRuns);
            striker.addRuns(runsScored); // runs scored off the bat on no ball
        } else {
            // Legal ball
            innings.setLegalBallsCount(innings.getLegalBallsCount() + 1);
            bowler.recordLegalBall(totalBallRuns);
            striker.addRuns(runsScored);
        }

        // Over and Ball display calculations
        int completedOvers = innings.getLegalBallsCount() / 6;
        int ballInOver = innings.getLegalBallsCount() % 6;
        String ballDisplay = completedOvers + "." + (isLegalBall ? ballInOver : (ballInOver == 0 ? 6 : ballInOver));

        String outBatsmanName = null;
        if (isWicket) {
            innings.setTotalWickets(innings.getTotalWickets() + 1);
            bowler.recordWicket();

            striker.setOut(true);
            striker.setBatting(false);
            striker.setOnStrike(false);
            outBatsmanName = striker.getPlayerName();

            String dismissal = formatDismissal(wicketType, bowler.getPlayerName());
            striker.setDismissalInfo(dismissal);

            // Record Fall of Wicket
            FallOfWicket fow = new FallOfWicket(
                innings.getTotalWickets(),
                innings.getTotalRuns(),
                ballDisplay,
                striker.getPlayerName()
            );
            innings.getFallOfWickets().add(fow);

            // Bring next batsman to crease if available
            if (innings.getTotalWickets() < 10) {
                BatsmanScore nextBatsman = bringNextBatsmanToCrease(innings);
                if (nextBatsman != null) {
                    nextBatsman.setOnStrike(true);
                    nextBatsman.setBatting(true);
                    striker = nextBatsman;
                }
            }
        }

        // Tag for UI ball-by-ball tracker
        String ballTag;
        if (isWicket) ballTag = "W";
        else if (extraType == ExtraType.WIDE) ballTag = "Wd" + (extraRuns > 1 ? "+" + (extraRuns - 1) : "");
        else if (extraType == ExtraType.NO_BALL) ballTag = "Nb" + (runsScored > 0 ? "+" + runsScored : "");
        else ballTag = String.valueOf(runsScored);

        match.addRecentBall(ballTag);

        // 4. Strike rotation
        // On odd runs (1, 3), batsmen swap ends
        if (runsScored == 1 || runsScored == 3) {
            swapStrike(striker, nonStriker);
        }

        // End of over checks
        boolean isOverCompleted = isLegalBall && (ballInOver == 0);
        if (isOverCompleted) {
            // Swap strike at the end of each completed over
            swapStrike(striker, nonStriker);
            // Rotate bowler for next over
            rotateBowler(innings, match, bowler);
        }

        // 5. Generate commentary
        String commentary = customCommentary != null ? customCommentary :
                commentaryGenerator.generate(
                        striker.getPlayerName(),
                        bowler.getPlayerName(),
                        runsScored,
                        extraType,
                        extraRuns,
                        isWicket,
                        wicketType
                );

        // Save ball event
        BallEvent ballEvent = new BallEvent(
                match.getId(),
                innings.getInningsNumber(),
                completedOvers,
                ballInOver == 0 && isLegalBall ? 6 : ballInOver,
                ballDisplay,
                striker.getPlayerName(),
                bowler.getPlayerName(),
                runsScored,
                extraType,
                extraRuns,
                isLegalBall,
                isWicket,
                wicketType,
                outBatsmanName,
                commentary
        );
        ballEventRepository.save(ballEvent);

        // 6. Calculate Run Rates
        match.setCurrentRunRate(innings.getRunRate());

        if (match.getCurrentInningsNumber() == 2 && match.getTarget() != null) {
            int runsNeeded = match.getTarget() - innings.getTotalRuns();
            int ballsRemaining = (match.getTotalOvers() * 6) - innings.getLegalBallsCount();
            if (ballsRemaining > 0 && runsNeeded > 0) {
                double rrr = Math.round(((double) runsNeeded / ((double) ballsRemaining / 6.0)) * 100.0) / 100.0;
                match.setRequiredRunRate(rrr);
            } else {
                match.setRequiredRunRate(0.0);
            }
        }

        // 7. Check Innings / Match Completion
        checkMatchProgress(match, innings);

        // Persist match state
        matchRepository.save(match);

        // Broadcast to clients via WebSocket & SSE
        LiveScoreUpdateDTO update = createScoreUpdate(match, commentary);
        notificationService.broadcastLiveScore(update);

        return update;
    }

    private void checkMatchProgress(CricketMatch match, Innings innings) {
        boolean inningsOver = (innings.getTotalWickets() >= 10) ||
                              (innings.getLegalBallsCount() >= match.getTotalOvers() * 6);

        if (match.getCurrentInningsNumber() == 1) {
            if (inningsOver) {
                innings.setCompleted(true);
                match.setTarget(innings.getTotalRuns() + 1);
                match.setCurrentInningsNumber(2);
                match.setStatus(MatchStatus.INNINGS_BREAK);
                match.setResultNote(innings.getBattingTeamName() + " scored " + innings.getTotalRuns() +
                        "/" + innings.getTotalWickets() + ". " + innings.getBowlingTeamName() +
                        " needs " + match.getTarget() + " runs to win.");

                // Set up second innings
                initializeSecondInnings(match);
                logger.info("1st Innings completed for match {}. Target is {}", match.getId(), match.getTarget());
            }
        } else if (match.getCurrentInningsNumber() == 2) {
            // 2nd Innings Target Chase check
            if (match.getTarget() != null && innings.getTotalRuns() >= match.getTarget()) {
                // Chasing team won!
                match.setStatus(MatchStatus.COMPLETED);
                match.setSimulating(false);
                stopScheduledTask(match.getId());
                innings.setCompleted(true);

                int wicketsRemaining = 10 - innings.getTotalWickets();
                match.setWinnerTeamId(innings.getBattingTeamId());
                match.setWinnerTeamName(innings.getBattingTeamName());
                String result = innings.getBattingTeamName() + " won by " + wicketsRemaining +
                        " wickets with " + ((match.getTotalOvers() * 6) - innings.getLegalBallsCount()) + " balls remaining!";
                match.setResultNote(result);
                assignManOfTheMatch(match);
                logger.info("Match {} finished: {}", match.getId(), result);
            } else if (inningsOver) {
                // Defending team won or tied
                match.setStatus(MatchStatus.COMPLETED);
                match.setSimulating(false);
                stopScheduledTask(match.getId());
                innings.setCompleted(true);

                int firstInningsTotal = match.getFirstInnings().getTotalRuns();
                int secondInningsTotal = innings.getTotalRuns();

                if (secondInningsTotal < firstInningsTotal) {
                    int margin = firstInningsTotal - secondInningsTotal;
                    match.setWinnerTeamId(match.getFirstInnings().getBattingTeamId());
                    match.setWinnerTeamName(match.getFirstInnings().getBattingTeamName());
                    String result = match.getFirstInnings().getBattingTeamName() + " won by " + margin + " runs!";
                    match.setResultNote(result);
                } else if (secondInningsTotal == firstInningsTotal) {
                    match.setResultNote("Match TIED! What an absolute thriller!");
                }
                assignManOfTheMatch(match);
            }
        }
    }

    private void assignManOfTheMatch(CricketMatch match) {
        // Pick best performer from winning team or highest scorer
        BatsmanScore bestBatsman = null;
        for (BatsmanScore b : match.getFirstInnings().getBatsmen()) {
            if (bestBatsman == null || b.getRuns() > bestBatsman.getRuns()) {
                bestBatsman = b;
            }
        }
        if (match.getSecondInnings() != null) {
            for (BatsmanScore b : match.getSecondInnings().getBatsmen()) {
                if (bestBatsman == null || b.getRuns() > bestBatsman.getRuns()) {
                    bestBatsman = b;
                }
            }
        }
        if (bestBatsman != null) {
            match.setManOfTheMatch(bestBatsman.getPlayerName() + " (" + bestBatsman.getRuns() + " runs)");
        }
    }

    private SimulationOutcome simulateBallOutcome(BatsmanScore batsman, BowlerScore bowler, MatchFormat format) {
        int roll = random.nextInt(100);

        // Batsman strike aggression adjustment
        int aggression = format == MatchFormat.T20 ? 8 : 4;

        if (roll < 3) {
            return new SimulationOutcome(0, ExtraType.WIDE, 1, false, null);
        } else if (roll < 4) {
            return new SimulationOutcome(0, ExtraType.NO_BALL, 1, false, null);
        } else if (roll < (8 + (aggression / 2))) {
            // Wicket!
            WicketType[] types = {WicketType.CAUGHT, WicketType.BOWLED, WicketType.LBW, WicketType.RUN_OUT, WicketType.STUMPED};
            WicketType wt = types[random.nextInt(types.length)];
            return new SimulationOutcome(0, ExtraType.NONE, 0, true, wt);
        } else if (roll < 42) {
            // Dot ball
            return new SimulationOutcome(0, ExtraType.NONE, 0, false, null);
        } else if (roll < 72) {
            // 1 run
            return new SimulationOutcome(1, ExtraType.NONE, 0, false, null);
        } else if (roll < 82) {
            // 2 runs
            return new SimulationOutcome(2, ExtraType.NONE, 0, false, null);
        } else if (roll < 84) {
            // 3 runs
            return new SimulationOutcome(3, ExtraType.NONE, 0, false, null);
        } else if (roll < (93 + (aggression / 2))) {
            // 4 runs (Boundary!)
            return new SimulationOutcome(4, ExtraType.NONE, 0, false, null);
        } else {
            // 6 runs (Maximum!)
            return new SimulationOutcome(6, ExtraType.NONE, 0, false, null);
        }
    }

    private String formatDismissal(WicketType wt, String bowlerName) {
        if (wt == null) return "c Fielder b " + bowlerName;
        return switch (wt) {
            case BOWLED -> "b " + bowlerName;
            case LBW -> "lbw b " + bowlerName;
            case RUN_OUT -> "run out";
            case STUMPED -> "st Keeper b " + bowlerName;
            case CAUGHT -> "c Fielder b " + bowlerName;
            case HIT_WICKET -> "hit wicket b " + bowlerName;
        };
    }

    private void swapStrike(BatsmanScore striker, BatsmanScore nonStriker) {
        if (striker != null && nonStriker != null) {
            boolean wasStriker = striker.isOnStrike();
            striker.setOnStrike(!wasStriker);
            nonStriker.setOnStrike(wasStriker);
        }
    }

    private void rotateBowler(Innings innings, CricketMatch match, BowlerScore currentBowler) {
        if (currentBowler != null) {
            currentBowler.setCurrent(false);
        }

        List<BowlerScore> bowlers = innings.getBowlers();
        if (bowlers.isEmpty()) return;

        // Choose a different bowler who is not currentBowler
        for (BowlerScore b : bowlers) {
            if (!b.getPlayerName().equals(currentBowler.getPlayerName())) {
                b.setCurrent(true);
                return;
            }
        }
        // If only 1 bowler registered, fallback
        bowlers.get(0).setCurrent(true);
    }

    private BatsmanScore getOrCreateCurrentStriker(Innings innings) {
        for (BatsmanScore b : innings.getBatsmen()) {
            if (b.isBatting() && b.isOnStrike() && !b.isOut()) {
                return b;
            }
        }
        // Fallback: activate first batsman
        if (!innings.getBatsmen().isEmpty()) {
            BatsmanScore b1 = innings.getBatsmen().get(0);
            b1.setBatting(true);
            b1.setOnStrike(true);
            return b1;
        }
        return null;
    }

    private BatsmanScore getOrCreateCurrentNonStriker(Innings innings, BatsmanScore striker) {
        for (BatsmanScore b : innings.getBatsmen()) {
            if (b.isBatting() && !b.isOnStrike() && !b.isOut()) {
                return b;
            }
        }
        // Fallback: activate second batsman
        if (innings.getBatsmen().size() > 1) {
            BatsmanScore b2 = innings.getBatsmen().get(1);
            b2.setBatting(true);
            b2.setOnStrike(false);
            return b2;
        }
        return null;
    }

    private BowlerScore getOrCreateCurrentBowler(Innings innings, CricketMatch match) {
        for (BowlerScore b : innings.getBowlers()) {
            if (b.isCurrent()) {
                return b;
            }
        }
        if (!innings.getBowlers().isEmpty()) {
            BowlerScore b = innings.getBowlers().get(0);
            b.setCurrent(true);
            return b;
        }
        return null;
    }

    private BatsmanScore bringNextBatsmanToCrease(Innings innings) {
        for (BatsmanScore b : innings.getBatsmen()) {
            if (!b.isBatting() && !b.isOut()) {
                b.setBatting(true);
                return b;
            }
        }
        return null;
    }

    public void initializeInningsForMatch(CricketMatch match) {
        Team battingTeam = match.getTossChoice().equalsIgnoreCase("BAT") ?
                (match.getTossWinnerId().equals(match.getTeamA().getId()) ? match.getTeamA() : match.getTeamB()) :
                (match.getTossWinnerId().equals(match.getTeamA().getId()) ? match.getTeamB() : match.getTeamA());

        Team bowlingTeam = battingTeam.getId().equals(match.getTeamA().getId()) ? match.getTeamB() : match.getTeamA();

        Innings firstInnings = new Innings(1, battingTeam.getId(), battingTeam.getName(),
                bowlingTeam.getId(), bowlingTeam.getName(), match.getTotalOvers());

        populateInningsPlayers(firstInnings, battingTeam, bowlingTeam);
        match.setFirstInnings(firstInnings);
        match.setCurrentInningsNumber(1);
    }

    public void initializeSecondInnings(CricketMatch match) {
        Team battingTeam = match.getFirstInnings().getBowlingTeamId().equals(match.getTeamA().getId()) ?
                match.getTeamA() : match.getTeamB();
        Team bowlingTeam = match.getFirstInnings().getBattingTeamId().equals(match.getTeamA().getId()) ?
                match.getTeamA() : match.getTeamB();

        Innings secondInnings = new Innings(2, battingTeam.getId(), battingTeam.getName(),
                bowlingTeam.getId(), bowlingTeam.getName(), match.getTotalOvers());

        populateInningsPlayers(secondInnings, battingTeam, bowlingTeam);
        match.setSecondInnings(secondInnings);
    }

    public void populateInningsPlayers(Innings innings, Team battingTeam, Team bowlingTeam) {
        innings.getBatsmen().clear();
        innings.getBowlers().clear();

        List<Player> batPlayers = battingTeam.getPlayers();
        for (int i = 0; i < batPlayers.size(); i++) {
            Player p = batPlayers.get(i);
            BatsmanScore bs = new BatsmanScore(p.getId(), p.getName(), i + 1);
            if (i == 0) {
                bs.setBatting(true);
                bs.setOnStrike(true);
            } else if (i == 1) {
                bs.setBatting(true);
                bs.setOnStrike(false);
            }
            innings.getBatsmen().add(bs);
        }

        List<Player> bowlPlayers = bowlingTeam.getPlayers();
        // Select bowlers from bowling team (bowlers or allrounders or players from index 5+)
        int bowlerCount = 0;
        for (Player p : bowlPlayers) {
            if (p.getRole() == PlayerRole.BOWLER || p.getRole() == PlayerRole.ALL_ROUNDER || bowlerCount < 4) {
                BowlerScore bowlerScore = new BowlerScore(p.getId(), p.getName());
                if (bowlerCount == 0) bowlerScore.setCurrent(true);
                innings.getBowlers().add(bowlerScore);
                bowlerCount++;
            }
        }
    }

    private void stopScheduledTask(Long matchId) {
        ScheduledFuture<?> future = activeSimulations.remove(matchId);
        if (future != null) {
            future.cancel(false);
        }
    }

    public LiveScoreUpdateDTO createScoreUpdate(CricketMatch match, String commentary) {
        LiveScoreUpdateDTO dto = new LiveScoreUpdateDTO();
        dto.setMatchId(match.getId());
        dto.setTitle(match.getTitle());
        dto.setStatus(match.getStatus());
        dto.setCurrentInningsNumber(match.getCurrentInningsNumber());
        dto.setTotalOvers(match.getTotalOvers());
        dto.setResultNote(match.getResultNote());
        dto.setWinnerTeamName(match.getWinnerTeamName());
        dto.setRecentBalls(new ArrayList<>(match.getRecentBalls()));
        dto.setCurrentRunRate(match.getCurrentRunRate());
        dto.setRequiredRunRate(match.getRequiredRunRate());
        dto.setTarget(match.getTarget());
        dto.setLatestCommentary(commentary);

        Innings innings = match.getCurrentInnings();
        if (innings != null) {
            dto.setBattingTeamName(innings.getBattingTeamName());
            dto.setBowlingTeamName(innings.getBowlingTeamName());
            dto.setTotalRuns(innings.getTotalRuns());
            dto.setTotalWickets(innings.getTotalWickets());
            dto.setOversBowled(innings.getOversBowled());

            for (BatsmanScore b : innings.getBatsmen()) {
                if (b.isBatting() && !b.isOut()) {
                    if (b.isOnStrike()) {
                        dto.setStrikerName(b.getPlayerName());
                        dto.setStrikerRuns(b.getRuns());
                        dto.setStrikerBalls(b.getBalls());
                    } else {
                        dto.setNonStrikerName(b.getPlayerName());
                        dto.setNonStrikerRuns(b.getRuns());
                        dto.setNonStrikerBalls(b.getBalls());
                    }
                }
            }

            for (BowlerScore bw : innings.getBowlers()) {
                if (bw.isCurrent()) {
                    dto.setCurrentBowlerName(bw.getPlayerName());
                    dto.setCurrentBowlerOvers(bw.getOvers());
                    dto.setCurrentBowlerRuns(bw.getRunsConceded());
                    dto.setCurrentBowlerWickets(bw.getWickets());
                    break;
                }
            }
        }

        return dto;
    }

    private static class SimulationOutcome {
        int runs;
        ExtraType extraType;
        int extraRuns;
        boolean isWicket;
        WicketType wicketType;

        SimulationOutcome(int runs, ExtraType extraType, int extraRuns, boolean isWicket, WicketType wicketType) {
            this.runs = runs;
            this.extraType = extraType;
            this.extraRuns = extraRuns;
            this.isWicket = isWicket;
            this.wicketType = wicketType;
        }
    }
}
