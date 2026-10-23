package com.cricket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "batsman_scores")
public class BatsmanScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;
    private String playerName;
    private int battingOrder;
    private int runs = 0;
    private int balls = 0;
    private int fours = 0;
    private int sixes = 0;
    private double strikeRate = 0.0;
    private boolean isOut = false;
    private String dismissalInfo = "not out";
    private boolean isOnStrike = false;
    private boolean isBatting = false; // currently at crease

    public BatsmanScore() {}

    public BatsmanScore(Long playerId, String playerName, int battingOrder) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.battingOrder = battingOrder;
    }

    public void addRuns(int scoredRuns) {
        this.runs += scoredRuns;
        this.balls += 1;
        if (scoredRuns == 4) this.fours += 1;
        if (scoredRuns == 6) this.sixes += 1;
        calculateStrikeRate();
    }

    public void addDotBall() {
        this.balls += 1;
        calculateStrikeRate();
    }

    public void calculateStrikeRate() {
        if (this.balls > 0) {
            this.strikeRate = Math.round(((double) this.runs / this.balls) * 10000.0) / 100.0;
        } else {
            this.strikeRate = 0.0;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getBattingOrder() { return battingOrder; }
    public void setBattingOrder(int battingOrder) { this.battingOrder = battingOrder; }

    public int getRuns() { return runs; }
    public void setRuns(int runs) { this.runs = runs; calculateStrikeRate(); }

    public int getBalls() { return balls; }
    public void setBalls(int balls) { this.balls = balls; calculateStrikeRate(); }

    public int getFours() { return fours; }
    public void setFours(int fours) { this.fours = fours; }

    public int getSixes() { return sixes; }
    public void setSixes(int sixes) { this.sixes = sixes; }

    public double getStrikeRate() { return strikeRate; }
    public void setStrikeRate(double strikeRate) { this.strikeRate = strikeRate; }

    public boolean isOut() { return isOut; }
    public void setOut(boolean out) { isOut = out; }

    public String getDismissalInfo() { return dismissalInfo; }
    public void setDismissalInfo(String dismissalInfo) { this.dismissalInfo = dismissalInfo; }

    public boolean isOnStrike() { return isOnStrike; }
    public void setOnStrike(boolean onStrike) { isOnStrike = onStrike; }

    public boolean isBatting() { return isBatting; }
    public void setBatting(boolean batting) { isBatting = batting; }
}
