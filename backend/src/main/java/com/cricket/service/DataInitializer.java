package com.cricket.service;

import com.cricket.model.*;
import com.cricket.repository.BallEventRepository;
import com.cricket.repository.MatchRepository;
import com.cricket.repository.PlayerRepository;
import com.cricket.repository.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final BallEventRepository ballEventRepository;
    private final SimulationEngine simulationEngine;

    public DataInitializer(PlayerRepository playerRepository,
                           TeamRepository teamRepository,
                           MatchRepository matchRepository,
                           BallEventRepository ballEventRepository,
                           SimulationEngine simulationEngine) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.ballEventRepository = ballEventRepository;
        this.simulationEngine = simulationEngine;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (teamRepository.count() > 0) return;

        // 1. Create Players & Teams
        Team india = createTeam("India", "IND", "🇮🇳", "#0077B6", "#FF9F1C");
        addPlayer(india, "Rohit Sharma", "India", PlayerRole.BATSMAN, 92, 30, 423, 14200, 15, 264.0, "2/27");
        addPlayer(india, "Virat Kohli", "India", PlayerRole.BATSMAN, 98, 25, 520, 26800, 9, 183.0, "1/13");
        addPlayer(india, "Suryakumar Yadav", "India", PlayerRole.BATSMAN, 94, 20, 110, 3100, 0, 117.0, "-");
        addPlayer(india, "Rishabh Pant", "India", PlayerRole.WICKET_KEEPER, 88, 10, 140, 4200, 0, 159.0, "-");
        addPlayer(india, "Hardik Pandya", "India", PlayerRole.ALL_ROUNDER, 87, 85, 195, 3600, 160, 92.0, "4/16");
        addPlayer(india, "Ravindra Jadeja", "India", PlayerRole.ALL_ROUNDER, 82, 88, 310, 5800, 520, 175.0, "7/42");
        addPlayer(india, "Axar Patel", "India", PlayerRole.ALL_ROUNDER, 79, 86, 120, 1900, 180, 84.0, "6/38");
        addPlayer(india, "Kuldeep Yadav", "India", PlayerRole.BOWLER, 40, 92, 135, 450, 260, 19.0, "6/25");
        addPlayer(india, "Jasprit Bumrah", "India", PlayerRole.BOWLER, 30, 99, 180, 310, 390, 34.0, "6/19");
        addPlayer(india, "Mohammed Siraj", "India", PlayerRole.BOWLER, 25, 89, 90, 120, 175, 16.0, "6/21");
        addPlayer(india, "Arshdeep Singh", "India", PlayerRole.BOWLER, 22, 88, 65, 80, 110, 12.0, "5/37");
        teamRepository.save(india);

        Team australia = createTeam("Australia", "AUS", "🇦🇺", "#FFD166", "#06D6A0");
        addPlayer(australia, "Travis Head", "Australia", PlayerRole.BATSMAN, 93, 40, 140, 4800, 22, 163.0, "2/25");
        addPlayer(australia, "David Warner", "Australia", PlayerRole.BATSMAN, 91, 15, 380, 12800, 4, 335.0, "-");
        addPlayer(australia, "Mitchell Marsh", "Australia", PlayerRole.ALL_ROUNDER, 86, 82, 180, 4400, 125, 177.0, "3/18");
        addPlayer(australia, "Steve Smith", "Australia", PlayerRole.BATSMAN, 94, 45, 320, 15400, 60, 239.0, "3/16");
        addPlayer(australia, "Glenn Maxwell", "Australia", PlayerRole.ALL_ROUNDER, 89, 78, 230, 5600, 140, 201.0, "4/40");
        addPlayer(australia, "Marcus Stoinis", "Australia", PlayerRole.ALL_ROUNDER, 83, 81, 150, 3300, 98, 146.0, "3/16");
        addPlayer(australia, "Josh Inglis", "Australia", PlayerRole.WICKET_KEEPER, 82, 10, 55, 1400, 0, 110.0, "-");
        addPlayer(australia, "Pat Cummins", "Australia", PlayerRole.ALL_ROUNDER, 70, 95, 210, 1900, 440, 64.0, "6/23");
        addPlayer(australia, "Mitchell Starc", "Australia", PlayerRole.BOWLER, 55, 96, 260, 2100, 660, 99.0, "6/28");
        addPlayer(australia, "Adam Zampa", "Australia", PlayerRole.BOWLER, 25, 91, 170, 290, 280, 22.0, "5/19");
        addPlayer(australia, "Josh Hazlewood", "Australia", PlayerRole.BOWLER, 20, 93, 165, 180, 380, 12.0, "6/52");
        teamRepository.save(australia);

        Team england = createTeam("England", "ENG", "🏴󠁧󠁢󠁥󠁮󠁧󠁿", "#E63946", "#1D3557");
        addPlayer(england, "Jos Buttler", "England", PlayerRole.WICKET_KEEPER, 93, 10, 280, 8900, 0, 162.0, "-");
        addPlayer(england, "Phil Salt", "England", PlayerRole.BATSMAN, 89, 15, 60, 1800, 0, 119.0, "-");
        addPlayer(england, "Harry Brook", "England", PlayerRole.BATSMAN, 88, 20, 75, 2400, 2, 186.0, "-");
        addPlayer(england, "Liam Livingstone", "England", PlayerRole.ALL_ROUNDER, 85, 80, 95, 2100, 45, 103.0, "3/17");
        addPlayer(england, "Moeen Ali", "England", PlayerRole.ALL_ROUNDER, 83, 82, 240, 5100, 280, 155.0, "6/53");
        addPlayer(england, "Sam Curran", "England", PlayerRole.ALL_ROUNDER, 78, 85, 110, 1700, 130, 95.0, "5/10");
        addPlayer(england, "Chris Jordan", "England", PlayerRole.BOWLER, 50, 83, 125, 800, 140, 43.0, "4/6");
        addPlayer(england, "Jofra Archer", "England", PlayerRole.BOWLER, 45, 95, 85, 600, 155, 30.0, "6/40");
        addPlayer(england, "Adil Rashid", "England", PlayerRole.BOWLER, 30, 91, 215, 950, 330, 38.0, "5/27");
        addPlayer(england, "Mark Wood", "England", PlayerRole.BOWLER, 25, 92, 105, 400, 185, 28.0, "6/37");
        addPlayer(england, "Reece Topley", "England", PlayerRole.BOWLER, 15, 86, 60, 90, 85, 8.0, "6/24");
        teamRepository.save(england);

        Team southAfrica = createTeam("South Africa", "SA", "🇿🇦", "#2A9D8F", "#E76F51");
        addPlayer(southAfrica, "Quinton de Kock", "South Africa", PlayerRole.WICKET_KEEPER, 92, 10, 310, 11200, 0, 178.0, "-");
        addPlayer(southAfrica, "Reeza Hendricks", "South Africa", PlayerRole.BATSMAN, 84, 15, 80, 2100, 1, 102.0, "-");
        addPlayer(southAfrica, "Aiden Markram", "South Africa", PlayerRole.ALL_ROUNDER, 88, 75, 140, 4100, 40, 175.0, "2/18");
        addPlayer(southAfrica, "Heinrich Klaasen", "South Africa", PlayerRole.BATSMAN, 93, 10, 110, 3400, 0, 174.0, "-");
        addPlayer(southAfrica, "David Miller", "South Africa", PlayerRole.BATSMAN, 89, 10, 260, 7100, 0, 138.0, "-");
        addPlayer(southAfrica, "Tristan Stubbs", "South Africa", PlayerRole.BATSMAN, 86, 30, 50, 1200, 3, 76.0, "-");
        addPlayer(southAfrica, "Marco Jansen", "South Africa", PlayerRole.ALL_ROUNDER, 74, 88, 70, 1100, 110, 84.0, "5/39");
        addPlayer(southAfrica, "Keshav Maharaj", "South Africa", PlayerRole.BOWLER, 40, 89, 120, 700, 180, 40.0, "7/32");
        addPlayer(southAfrica, "Kagiso Rabada", "South Africa", PlayerRole.BOWLER, 35, 96, 210, 1200, 490, 34.0, "7/112");
        addPlayer(southAfrica, "Anrich Nortje", "South Africa", PlayerRole.BOWLER, 20, 94, 80, 180, 150, 10.0, "6/56");
        addPlayer(southAfrica, "Tabraiz Shamsi", "South Africa", PlayerRole.BOWLER, 15, 88, 95, 80, 135, 6.0, "5/24");
        teamRepository.save(southAfrica);

        // 2. Seed Matches
        seedMatch1_WorldCupFinalLive(india, australia);
        seedMatch2_TheAshesUpcoming(england, australia);
        seedMatch3_ChampionshipThrillerCompleted(india, southAfrica);
    }

    private void seedMatch1_WorldCupFinalLive(Team india, Team australia) {
        CricketMatch match = new CricketMatch();
        match.setTitle("India vs Australia - World Cup Final 2026");
        match.setSeriesName("ICC Men's T20 World Cup 2026");
        match.setVenue("Melbourne Cricket Ground, Australia");
        match.setFormat(MatchFormat.T20);
        match.setTotalOvers(20);
        match.setTeamA(india);
        match.setTeamB(australia);
        match.setTossWinnerId(australia.getId());
        match.setTossChoice("BAT");
        match.setStatus(MatchStatus.LIVE);
        match.setCurrentInningsNumber(2);
        match.setTarget(177);

        // Innings 1: Australia 176/6 (20.0 overs)
        Innings inn1 = new Innings(1, australia.getId(), "Australia", india.getId(), "India", 20);
        inn1.setTotalRuns(176);
        inn1.setTotalWickets(6);
        inn1.setCompleted(true);
        inn1.setLegalBallsCount(120);
        inn1.setOversBowled(20.0);
        inn1.setWides(5);
        inn1.setNoBalls(1);
        inn1.setTotalExtras(8);

        // Batsmen for Innings 1
        inn1.getBatsmen().add(createBatsmanScore(australia.getPlayers().get(0), 1, 48, 28, 5, 2, true, "c Pant b Bumrah"));
        inn1.getBatsmen().add(createBatsmanScore(australia.getPlayers().get(1), 2, 24, 18, 3, 1, true, "b Siraj"));
        inn1.getBatsmen().add(createBatsmanScore(australia.getPlayers().get(2), 3, 35, 22, 4, 1, true, "c Jadeja b Kuldeep"));
        inn1.getBatsmen().add(createBatsmanScore(australia.getPlayers().get(3), 4, 18, 14, 2, 0, true, "lbw b Axar"));
        inn1.getBatsmen().add(createBatsmanScore(australia.getPlayers().get(4), 5, 26, 15, 2, 2, true, "c Rohit b Bumrah"));
        inn1.getBatsmen().add(createBatsmanScore(australia.getPlayers().get(5), 6, 15, 12, 1, 0, false, "not out"));
        inn1.getBatsmen().add(createBatsmanScore(australia.getPlayers().get(6), 7, 6, 4, 1, 0, true, "run out"));
        inn1.getBatsmen().add(createBatsmanScore(australia.getPlayers().get(7), 8, 4, 3, 0, 0, false, "not out"));

        // Bowlers for Innings 1
        inn1.getBowlers().add(createBowlerScore(india.getPlayers().get(8), 24, 32, 2, 8.0, false)); // Bumrah
        inn1.getBowlers().add(createBowlerScore(india.getPlayers().get(9), 24, 41, 1, 10.25, false)); // Siraj
        inn1.getBowlers().add(createBowlerScore(india.getPlayers().get(7), 24, 28, 1, 7.0, false)); // Kuldeep
        inn1.getBowlers().add(createBowlerScore(india.getPlayers().get(6), 24, 35, 1, 8.75, false)); // Axar
        inn1.getBowlers().add(createBowlerScore(india.getPlayers().get(4), 24, 34, 0, 8.5, false)); // Hardik

        match.setFirstInnings(inn1);

        // Innings 2: India 128/3 (14.2 overs)
        Innings inn2 = new Innings(2, india.getId(), "India", australia.getId(), "Australia", 20);
        inn2.setTotalRuns(128);
        inn2.setTotalWickets(3);
        inn2.setLegalBallsCount(86);
        inn2.setOversBowled(14.2);
        inn2.setWides(4);
        inn2.setNoBalls(0);
        inn2.setTotalExtras(6);

        // Indian Batsmen
        inn2.getBatsmen().add(createBatsmanScore(india.getPlayers().get(0), 1, 38, 24, 5, 1, true, "c Smith b Starc"));
        BatsmanScore virat = createBatsmanScore(india.getPlayers().get(1), 2, 54, 38, 6, 1, false, "not out");
        virat.setBatting(true);
        virat.setOnStrike(true);
        inn2.getBatsmen().add(virat);

        inn2.getBatsmen().add(createBatsmanScore(india.getPlayers().get(2), 3, 14, 12, 1, 1, true, "c Warner b Zampa"));

        BatsmanScore hardik = createBatsmanScore(india.getPlayers().get(4), 4, 22, 12, 2, 1, false, "not out");
        hardik.setBatting(true);
        hardik.setOnStrike(false);
        inn2.getBatsmen().add(hardik);

        // Remaining Indian batsmen ready in dugout
        for (int i = 5; i < india.getPlayers().size(); i++) {
            inn2.getBatsmen().add(new BatsmanScore(india.getPlayers().get(i).getId(), india.getPlayers().get(i).getName(), i + 1));
        }

        // Australian Bowlers
        BowlerScore starc = createBowlerScore(australia.getPlayers().get(8), 18, 28, 1, 9.33, true);
        inn2.getBowlers().add(starc);
        inn2.getBowlers().add(createBowlerScore(australia.getPlayers().get(7), 18, 24, 0, 8.0, false)); // Cummins
        inn2.getBowlers().add(createBowlerScore(australia.getPlayers().get(9), 18, 31, 1, 10.33, false)); // Zampa
        inn2.getBowlers().add(createBowlerScore(australia.getPlayers().get(10), 18, 26, 0, 8.67, false)); // Hazlewood
        inn2.getBowlers().add(createBowlerScore(australia.getPlayers().get(4), 14, 15, 0, 6.42, false)); // Maxwell

        // Fall of wickets for Innings 2
        inn2.getFallOfWickets().add(new FallOfWicket(1, 48, "5.2", "Rohit Sharma"));
        inn2.getFallOfWickets().add(new FallOfWicket(2, 75, "8.4", "Suryakumar Yadav"));
        inn2.getFallOfWickets().add(new FallOfWicket(3, 98, "11.1", "Rishabh Pant"));

        match.setSecondInnings(inn2);
        match.setCurrentRunRate(8.93);
        match.setRequiredRunRate(8.65);
        match.setResultNote("India need 49 runs from 34 balls with 7 wickets in hand.");

        // Recent balls feed
        match.getRecentBalls().addAll(List.of("1", "4", "0", "2", "6", "1", "0", "1", "4", "W", "1", "2"));

        matchRepository.save(match);

        // Seed some sample ball events
        BallEvent b1 = new BallEvent(match.getId(), 2, 14, 1, "14.1", "Virat Kohli", "Mitchell Starc",
                1, ExtraType.NONE, 0, true, false, null, null,
                "Virat Kohli plays a deft dab towards third man and rotates the strike effortlessly.");
        BallEvent b2 = new BallEvent(match.getId(), 2, 14, 2, "14.2", "Hardik Pandya", "Mitchell Starc",
                2, ExtraType.NONE, 0, true, false, null, null,
                "Hardik Pandya pushes hard into the deep cover pocket, excellent sprint to convert one into two!");
        ballEventRepository.save(b1);
        ballEventRepository.save(b2);
    }

    private void seedMatch2_TheAshesUpcoming(Team england, Team australia) {
        CricketMatch match = new CricketMatch();
        match.setTitle("England vs Australia - The Ashes T20 Classic");
        match.setSeriesName("The Ashes T20 International Series");
        match.setVenue("Lord's Cricket Ground, London");
        match.setFormat(MatchFormat.T20);
        match.setTotalOvers(20);
        match.setTeamA(england);
        match.setTeamB(australia);
        match.setTossWinnerId(england.getId());
        match.setTossChoice("BAT");
        match.setStatus(MatchStatus.UPCOMING);
        match.setResultNote("Match scheduled to begin shortly. Toss won by England, elected to bat first.");

        simulationEngine.initializeInningsForMatch(match);
        matchRepository.save(match);
    }

    private void seedMatch3_ChampionshipThrillerCompleted(Team india, Team southAfrica) {
        CricketMatch match = new CricketMatch();
        match.setTitle("India vs South Africa - Championship Thriller");
        match.setSeriesName("T20 World Cup Final");
        match.setVenue("Kensington Oval, Barbados");
        match.setFormat(MatchFormat.T20);
        match.setTotalOvers(20);
        match.setTeamA(india);
        match.setTeamB(southAfrica);
        match.setTossWinnerId(india.getId());
        match.setTossChoice("BAT");
        match.setStatus(MatchStatus.COMPLETED);
        match.setCurrentInningsNumber(2);
        match.setTarget(177);
        match.setWinnerTeamId(india.getId());
        match.setWinnerTeamName("India");
        match.setResultNote("India won by 7 runs in a historic thriller to lift the trophy!");
        match.setManOfTheMatch("Virat Kohli (76 runs off 59 balls)");

        // Innings 1: India 176/7 (20.0)
        Innings inn1 = new Innings(1, india.getId(), "India", southAfrica.getId(), "South Africa", 20);
        inn1.setTotalRuns(176);
        inn1.setTotalWickets(7);
        inn1.setCompleted(true);
        inn1.setLegalBallsCount(120);
        inn1.setOversBowled(20.0);
        inn1.getBatsmen().add(createBatsmanScore(india.getPlayers().get(1), 1, 76, 59, 6, 2, true, "c Rabada b Jansen"));
        inn1.getBatsmen().add(createBatsmanScore(india.getPlayers().get(6), 2, 47, 31, 1, 4, true, "run out"));
        inn1.getBatsmen().add(createBatsmanScore(india.getPlayers().get(2), 3, 3, 4, 0, 0, true, "c Klaasen b Maharaj"));
        match.setFirstInnings(inn1);

        // Innings 2: South Africa 169/8 (20.0)
        Innings inn2 = new Innings(2, southAfrica.getId(), "South Africa", india.getId(), "India", 20);
        inn2.setTotalRuns(169);
        inn2.setTotalWickets(8);
        inn2.setCompleted(true);
        inn2.setLegalBallsCount(120);
        inn2.setOversBowled(20.0);
        inn2.getBatsmen().add(createBatsmanScore(southAfrica.getPlayers().get(3), 1, 52, 27, 2, 5, true, "c Pant b Hardik"));
        inn2.getBatsmen().add(createBatsmanScore(southAfrica.getPlayers().get(4), 2, 21, 17, 1, 1, true, "c Suryakumar b Hardik"));
        match.setSecondInnings(inn2);

        matchRepository.save(match);
    }

    private Team createTeam(String name, String shortName, String emoji, String primary, String secondary) {
        return new Team(name, shortName, emoji, primary, secondary);
    }

    private void addPlayer(Team team, String name, String country, PlayerRole role, int bat, int bowl,
                           int matches, int runs, int wickets, double hs, String bb) {
        Player p = new Player(name, country, role, bat, bowl);
        p.setTotalMatches(matches);
        p.setTotalRuns(runs);
        p.setTotalWickets(wickets);
        p.setHighestScore(hs);
        p.setBestBowling(bb);
        playerRepository.save(p);
        team.getPlayers().add(p);
    }

    private BatsmanScore createBatsmanScore(Player p, int order, int runs, int balls, int fours, int sixes, boolean out, String dismissal) {
        BatsmanScore bs = new BatsmanScore(p.getId(), p.getName(), order);
        bs.setRuns(runs);
        bs.setBalls(balls);
        bs.setFours(fours);
        bs.setSixes(sixes);
        bs.setOut(out);
        bs.setDismissalInfo(dismissal);
        bs.calculateStrikeRate();
        return bs;
    }

    private BowlerScore createBowlerScore(Player p, int balls, int runs, int wickets, double econ, boolean isCurrent) {
        BowlerScore b = new BowlerScore(p.getId(), p.getName());
        b.setBallsBowled(balls);
        b.setRunsConceded(runs);
        b.setWickets(wickets);
        b.setEconomy(econ);
        b.setCurrent(isCurrent);
        b.updateOversAndEconomy();
        return b;
    }
}
