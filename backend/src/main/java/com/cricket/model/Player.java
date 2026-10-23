package com.cricket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String country;

    @Enumerated(EnumType.STRING)
    private PlayerRole role;

    private int battingSkill = 75; // 0-100
    private int bowlingSkill = 60; // 0-100
    private int totalMatches = 0;
    private int totalRuns = 0;
    private int totalWickets = 0;
    private double highestScore = 0;
    private String bestBowling = "-";

    public Player() {}

    public Player(String name, String country, PlayerRole role, int battingSkill, int bowlingSkill) {
        this.name = name;
        this.country = country;
        this.role = role;
        this.battingSkill = battingSkill;
        this.bowlingSkill = bowlingSkill;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public PlayerRole getRole() { return role; }
    public void setRole(PlayerRole role) { this.role = role; }

    public int getBattingSkill() { return battingSkill; }
    public void setBattingSkill(int battingSkill) { this.battingSkill = battingSkill; }

    public int getBowlingSkill() { return bowlingSkill; }
    public void setBowlingSkill(int bowlingSkill) { this.bowlingSkill = bowlingSkill; }

    public int getTotalMatches() { return totalMatches; }
    public void setTotalMatches(int totalMatches) { this.totalMatches = totalMatches; }

    public int getTotalRuns() { return totalRuns; }
    public void setTotalRuns(int totalRuns) { this.totalRuns = totalRuns; }

    public int getTotalWickets() { return totalWickets; }
    public void setTotalWickets(int totalWickets) { this.totalWickets = totalWickets; }

    public double getHighestScore() { return highestScore; }
    public void setHighestScore(double highestScore) { this.highestScore = highestScore; }

    public String getBestBowling() { return bestBowling; }
    public void setBestBowling(String bestBowling) { this.bestBowling = bestBowling; }
}
