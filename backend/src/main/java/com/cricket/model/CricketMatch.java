package com.cricket.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
public class CricketMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String seriesName;
    private String venue;

    @Enumerated(EnumType.STRING)
    private MatchFormat format = MatchFormat.T20;

    private int totalOvers = 20;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_a_id")
    private Team teamA;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_b_id")
    private Team teamB;

    private Long tossWinnerId;
    private String tossChoice = "BAT"; // BAT or BOWL

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.UPCOMING;

    private int currentInningsNumber = 1;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "first_innings_id")
    private Innings firstInnings;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "second_innings_id")
    private Innings secondInnings;

    private Integer target;
    private double requiredRunRate = 0.0;
    private double currentRunRate = 0.0;

    private String resultNote = "Match not started yet";
    private Long winnerTeamId;
    private String winnerTeamName;
    private String manOfTheMatch;

    private int simulationSpeedMs = 1500;
    private boolean simulating = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "match_recent_balls", joinColumns = @JoinColumn(name = "match_id"))
    @Column(name = "ball_tag")
    private List<String> recentBalls = new ArrayList<>();

    public CricketMatch() {}

    public Innings getCurrentInnings() {
        if (currentInningsNumber == 1) {
            return firstInnings;
        } else if (currentInningsNumber == 2) {
            return secondInnings;
        }
        return null;
    }

    public void addRecentBall(String ballTag) {
        if (this.recentBalls == null) {
            this.recentBalls = new ArrayList<>();
        }
        this.recentBalls.add(ballTag);
        if (this.recentBalls.size() > 18) {
            this.recentBalls.remove(0);
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSeriesName() { return seriesName; }
    public void setSeriesName(String seriesName) { this.seriesName = seriesName; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public MatchFormat getFormat() { return format; }
    public void setFormat(MatchFormat format) { this.format = format; }

    public int getTotalOvers() { return totalOvers; }
    public void setTotalOvers(int totalOvers) { this.totalOvers = totalOvers; }

    public Team getTeamA() { return teamA; }
    public void setTeamA(Team teamA) { this.teamA = teamA; }

    public Team getTeamB() { return teamB; }
    public void setTeamB(Team teamB) { this.teamB = teamB; }

    public Long getTossWinnerId() { return tossWinnerId; }
    public void setTossWinnerId(Long tossWinnerId) { this.tossWinnerId = tossWinnerId; }

    public String getTossChoice() { return tossChoice; }
    public void setTossChoice(String tossChoice) { this.tossChoice = tossChoice; }

    public MatchStatus getStatus() { return status; }
    public void setStatus(MatchStatus status) { this.status = status; }

    public int getCurrentInningsNumber() { return currentInningsNumber; }
    public void setCurrentInningsNumber(int currentInningsNumber) { this.currentInningsNumber = currentInningsNumber; }

    public Innings getFirstInnings() { return firstInnings; }
    public void setFirstInnings(Innings firstInnings) { this.firstInnings = firstInnings; }

    public Innings getSecondInnings() { return secondInnings; }
    public void setSecondInnings(Innings secondInnings) { this.secondInnings = secondInnings; }

    public Integer getTarget() { return target; }
    public void setTarget(Integer target) { this.target = target; }

    public double getRequiredRunRate() { return requiredRunRate; }
    public void setRequiredRunRate(double requiredRunRate) { this.requiredRunRate = requiredRunRate; }

    public double getCurrentRunRate() { return currentRunRate; }
    public void setCurrentRunRate(double currentRunRate) { this.currentRunRate = currentRunRate; }

    public String getResultNote() { return resultNote; }
    public void setResultNote(String resultNote) { this.resultNote = resultNote; }

    public Long getWinnerTeamId() { return winnerTeamId; }
    public void setWinnerTeamId(Long winnerTeamId) { this.winnerTeamId = winnerTeamId; }

    public String getWinnerTeamName() { return winnerTeamName; }
    public void setWinnerTeamName(String winnerTeamName) { this.winnerTeamName = winnerTeamName; }

    public String getManOfTheMatch() { return manOfTheMatch; }
    public void setManOfTheMatch(String manOfTheMatch) { this.manOfTheMatch = manOfTheMatch; }

    public int getSimulationSpeedMs() { return simulationSpeedMs; }
    public void setSimulationSpeedMs(int simulationSpeedMs) { this.simulationSpeedMs = simulationSpeedMs; }

    public boolean isSimulating() { return simulating; }
    public void setSimulating(boolean simulating) { this.simulating = simulating; }

    public List<String> getRecentBalls() { return recentBalls; }
    public void setRecentBalls(List<String> recentBalls) { this.recentBalls = recentBalls; }
}
