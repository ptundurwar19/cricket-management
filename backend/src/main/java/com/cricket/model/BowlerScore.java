package com.cricket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bowler_scores")
public class BowlerScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long playerId;
    private String playerName;
    private int ballsBowled = 0;
    private double overs = 0.0;
    private int maidens = 0;
    private int runsConceded = 0;
    private int wickets = 0;
    private int wides = 0;
    private int noBalls = 0;
    private double economy = 0.0;
    private boolean isCurrent = false;

    public BowlerScore() {}

    public BowlerScore(Long playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    public void recordLegalBall(int runs) {
        this.ballsBowled += 1;
        this.runsConceded += runs;
        updateOversAndEconomy();
    }

    public void recordExtra(ExtraType type, int extraRuns) {
        this.runsConceded += extraRuns;
        if (type == ExtraType.WIDE) {
            this.wides += 1;
        } else if (type == ExtraType.NO_BALL) {
            this.noBalls += 1;
        }
        updateOversAndEconomy();
    }

    public void recordWicket() {
        this.wickets += 1;
    }

    public void updateOversAndEconomy() {
        int fullOvers = this.ballsBowled / 6;
        int remainingBalls = this.ballsBowled % 6;
        this.overs = fullOvers + (remainingBalls * 0.1);

        if (this.ballsBowled > 0) {
            double oversFraction = (double) this.ballsBowled / 6.0;
            this.economy = Math.round((this.runsConceded / oversFraction) * 100.0) / 100.0;
        } else {
            this.economy = 0.0;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getBallsBowled() { return ballsBowled; }
    public void setBallsBowled(int ballsBowled) { this.ballsBowled = ballsBowled; updateOversAndEconomy(); }

    public double getOvers() { return overs; }
    public void setOvers(double overs) { this.overs = overs; }

    public int getMaidens() { return maidens; }
    public void setMaidens(int maidens) { this.maidens = maidens; }

    public int getRunsConceded() { return runsConceded; }
    public void setRunsConceded(int runsConceded) { this.runsConceded = runsConceded; updateOversAndEconomy(); }

    public int getWickets() { return wickets; }
    public void setWickets(int wickets) { this.wickets = wickets; }

    public int getWides() { return wides; }
    public void setWides(int wides) { this.wides = wides; }

    public int getNoBalls() { return noBalls; }
    public void setNoBalls(int noBalls) { this.noBalls = noBalls; }

    public double getEconomy() { return economy; }
    public void setEconomy(double economy) { this.economy = economy; }

    public boolean isCurrent() { return isCurrent; }
    public void setCurrent(boolean current) { isCurrent = current; }
}
