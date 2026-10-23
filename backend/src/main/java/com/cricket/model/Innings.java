package com.cricket.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "innings")
public class Innings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int inningsNumber;
    private Long battingTeamId;
    private String battingTeamName;
    private Long bowlingTeamId;
    private String bowlingTeamName;

    private int totalRuns = 0;
    private int totalWickets = 0;
    private int totalOvers = 20; // format overs limit
    private double oversBowled = 0.0;
    private int legalBallsCount = 0;

    private int wides = 0;
    private int noBalls = 0;
    private int byes = 0;
    private int legByes = 0;
    private int totalExtras = 0;

    private boolean isCompleted = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "innings_id")
    private List<BatsmanScore> batsmen = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "innings_id")
    private List<BowlerScore> bowlers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "innings_id")
    private List<FallOfWicket> fallOfWickets = new ArrayList<>();

    public Innings() {}

    public Innings(int inningsNumber, Long battingTeamId, String battingTeamName,
                   Long bowlingTeamId, String bowlingTeamName, int totalOvers) {
        this.inningsNumber = inningsNumber;
        this.battingTeamId = battingTeamId;
        this.battingTeamName = battingTeamName;
        this.bowlingTeamId = bowlingTeamId;
        this.bowlingTeamName = bowlingTeamName;
        this.totalOvers = totalOvers;
    }

    public void updateOversBowled() {
        int fullOvers = this.legalBallsCount / 6;
        int remainingBalls = this.legalBallsCount % 6;
        this.oversBowled = fullOvers + (remainingBalls * 0.1);
    }

    public double getRunRate() {
        if (this.legalBallsCount > 0) {
            double oversFraction = (double) this.legalBallsCount / 6.0;
            return Math.round((this.totalRuns / oversFraction) * 100.0) / 100.0;
        }
        return 0.0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getInningsNumber() { return inningsNumber; }
    public void setInningsNumber(int inningsNumber) { this.inningsNumber = inningsNumber; }

    public Long getBattingTeamId() { return battingTeamId; }
    public void setBattingTeamId(Long battingTeamId) { this.battingTeamId = battingTeamId; }

    public String getBattingTeamName() { return battingTeamName; }
    public void setBattingTeamName(String battingTeamName) { this.battingTeamName = battingTeamName; }

    public Long getBowlingTeamId() { return bowlingTeamId; }
    public void setBowlingTeamId(Long bowlingTeamId) { this.bowlingTeamId = bowlingTeamId; }

    public String getBowlingTeamName() { return bowlingTeamName; }
    public void setBowlingTeamName(String bowlingTeamName) { this.bowlingTeamName = bowlingTeamName; }

    public int getTotalRuns() { return totalRuns; }
    public void setTotalRuns(int totalRuns) { this.totalRuns = totalRuns; }

    public int getTotalWickets() { return totalWickets; }
    public void setTotalWickets(int totalWickets) { this.totalWickets = totalWickets; }

    public int getTotalOvers() { return totalOvers; }
    public void setTotalOvers(int totalOvers) { this.totalOvers = totalOvers; }

    public double getOversBowled() { return oversBowled; }
    public void setOversBowled(double oversBowled) { this.oversBowled = oversBowled; }

    public int getLegalBallsCount() { return legalBallsCount; }
    public void setLegalBallsCount(int legalBallsCount) { this.legalBallsCount = legalBallsCount; updateOversBowled(); }

    public int getWides() { return wides; }
    public void setWides(int wides) { this.wides = wides; }

    public int getNoBalls() { return noBalls; }
    public void setNoBalls(int noBalls) { this.noBalls = noBalls; }

    public int getByes() { return byes; }
    public void setByes(int byes) { this.byes = byes; }

    public int getLegByes() { return legByes; }
    public void setLegByes(int legByes) { this.legByes = legByes; }

    public int getTotalExtras() { return totalExtras; }
    public void setTotalExtras(int totalExtras) { this.totalExtras = totalExtras; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public List<BatsmanScore> getBatsmen() { return batsmen; }
    public void setBatsmen(List<BatsmanScore> batsmen) { this.batsmen = batsmen; }

    public List<BowlerScore> getBowlers() { return bowlers; }
    public void setBowlers(List<BowlerScore> bowlers) { this.bowlers = bowlers; }

    public List<FallOfWicket> getFallOfWickets() { return fallOfWickets; }
    public void setFallOfWickets(List<FallOfWicket> fallOfWickets) { this.fallOfWickets = fallOfWickets; }
}
