package com.cricket.service;

import com.cricket.dto.CreateMatchRequest;
import com.cricket.dto.LiveScoreUpdateDTO;
import com.cricket.dto.ManualBallRequest;
import com.cricket.model.*;
import com.cricket.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final BallEventRepository ballEventRepository;
    private final SimulationEngine simulationEngine;

    public MatchService(MatchRepository matchRepository,
                        TeamRepository teamRepository,
                        PlayerRepository playerRepository,
                        BallEventRepository ballEventRepository,
                        SimulationEngine simulationEngine) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.ballEventRepository = ballEventRepository;
        this.simulationEngine = simulationEngine;
    }

    public List<CricketMatch> getAllMatches(MatchStatus status) {
        if (status != null) {
            return matchRepository.findByStatus(status);
        }
        return matchRepository.findAll();
    }

    public CricketMatch getMatchById(Long id) {
        return matchRepository.findById(id).orElse(null);
    }

    @Transactional
    public CricketMatch createMatch(CreateMatchRequest req) {
        Team teamA = teamRepository.findById(req.getTeamAId())
                .orElseThrow(() -> new IllegalArgumentException("Team A not found with id: " + req.getTeamAId()));
        Team teamB = teamRepository.findById(req.getTeamBId())
                .orElseThrow(() -> new IllegalArgumentException("Team B not found with id: " + req.getTeamBId()));

        CricketMatch match = new CricketMatch();
        match.setTitle(req.getTitle() != null ? req.getTitle() : teamA.getName() + " vs " + teamB.getName());
        match.setSeriesName(req.getSeriesName() != null ? req.getSeriesName() : "International Series 2026");
        match.setVenue(req.getVenue() != null ? req.getVenue() : "Melbourne Cricket Ground");
        match.setFormat(req.getFormat() != null ? req.getFormat() : MatchFormat.T20);
        match.setTotalOvers(req.getTotalOvers() > 0 ? req.getTotalOvers() : match.getFormat().getDefaultOvers());
        match.setTeamA(teamA);
        match.setTeamB(teamB);

        Long tossWinnerId = req.getTossWinnerId() != null ? req.getTossWinnerId() : teamA.getId();
        match.setTossWinnerId(tossWinnerId);
        match.setTossChoice(req.getTossChoice() != null ? req.getTossChoice() : "BAT");
        match.setStatus(MatchStatus.UPCOMING);

        simulationEngine.initializeInningsForMatch(match);
        return matchRepository.save(match);
    }

    public void startSimulation(Long id, Integer speedMs) {
        simulationEngine.startSimulation(id, speedMs);
    }

    public void pauseSimulation(Long id) {
        simulationEngine.pauseSimulation(id);
    }

    public void resumeSimulation(Long id) {
        simulationEngine.resumeSimulation(id);
    }

    public void resetMatch(Long id) {
        simulationEngine.resetMatch(id);
    }

    public LiveScoreUpdateDTO processManualBall(Long id, ManualBallRequest req) {
        return simulationEngine.processNextBall(id, req);
    }

    public List<BallEvent> getCommentary(Long matchId) {
        return ballEventRepository.findByMatchIdOrderByTimestampDesc(matchId);
    }

    public Map<String, Object> getLeaderboard() {
        Map<String, Object> leaderboard = new HashMap<>();
        leaderboard.put("topRunScorers", playerRepository.findTop10ByOrderByTotalRunsDesc());
        leaderboard.put("topWicketTakers", playerRepository.findTop10ByOrderByTotalWicketsDesc());
        return leaderboard;
    }
}
