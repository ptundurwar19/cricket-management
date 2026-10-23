package com.cricket.dto;

import com.cricket.model.MatchStatus;
import java.util.List;

public class LiveScoreUpdateDTO {
    private Long matchId;
    private String title;
    private MatchStatus status;
    private int currentInningsNumber;
    private String battingTeamName;
    private String bowlingTeamName;
    private int totalRuns;
    private int totalWickets;
    private double oversBowled;
    private int totalOvers;
    private double currentRunRate;
    private double requiredRunRate;
    private Integer target;
    private String strikerName;
    private int strikerRuns;
    private int strikerBalls;
    private String nonStrikerName;
    private int nonStrikerRuns;
    private int nonStrikerBalls;
    private String currentBowlerName;
    private double currentBowlerOvers;
    private int currentBowlerRuns;
    private int currentBowlerWickets;
    private String latestCommentary;
    private List<String> recentBalls;
    private String resultNote;
    private String winnerTeamName;

    public LiveScoreUpdateDTO() {}

    // Getters and Setters
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public MatchStatus getStatus() { return status; }
    public void setStatus(MatchStatus status) { this.status = status; }

    public int getCurrentInningsNumber() { return currentInningsNumber; }
    public void setCurrentInningsNumber(int currentInningsNumber) { this.currentInningsNumber = currentInningsNumber; }

    public String getBattingTeamName() { return battingTeamName; }
    public void setBattingTeamName(String battingTeamName) { this.battingTeamName = battingTeamName; }

    public String getBowlingTeamName() { return bowlingTeamName; }
    public void setBowlingTeamName(String bowlingTeamName) { this.bowlingTeamName = bowlingTeamName; }

    public int getTotalRuns() { return totalRuns; }
    public void setTotalRuns(int totalRuns) { this.totalRuns = totalRuns; }

    public int getTotalWickets() { return totalWickets; }
    public void setTotalWickets(int totalWickets) { this.totalWickets = totalWickets; }

    public double getOversBowled() { return oversBowled; }
    public void setOversBowled(double oversBowled) { this.oversBowled = oversBowled; }

    public int getTotalOvers() { return totalOvers; }
    public void setTotalOvers(int totalOvers) { this.totalOvers = totalOvers; }

    public double getCurrentRunRate() { return currentRunRate; }
    public void setCurrentRunRate(double currentRunRate) { this.currentRunRate = currentRunRate; }

    public double getRequiredRunRate() { return requiredRunRate; }
    public void setRequiredRunRate(double requiredRunRate) { this.requiredRunRate = requiredRunRate; }

    public Integer getTarget() { return target; }
    public void setTarget(Integer target) { this.target = target; }

    public String getStrikerName() { return strikerName; }
    public void setStrikerName(String strikerName) { this.strikerName = strikerName; }

    public int getStrikerRuns() { return strikerRuns; }
    public void setStrikerRuns(int strikerRuns) { this.strikerRuns = strikerRuns; }

    public int getStrikerBalls() { return strikerBalls; }
    public void setStrikerBalls(int strikerBalls) { this.strikerBalls = strikerBalls; }

    public String getNonStrikerName() { return nonStrikerName; }
    public void setNonStrikerName(String nonStrikerName) { this.nonStrikerName = nonStrikerName; }

    public int getNonStrikerRuns() { return nonStrikerRuns; }
    public void setNonStrikerRuns(int nonStrikerRuns) { this.nonStrikerRuns = nonStrikerRuns; }

    public int getNonStrikerBalls() { return nonStrikerBalls; }
    public void setNonStrikerBalls(int nonStrikerBalls) { this.nonStrikerBalls = nonStrikerBalls; }

    public String getCurrentBowlerName() { return currentBowlerName; }
    public void setCurrentBowlerName(String currentBowlerName) { this.currentBowlerName = currentBowlerName; }

    public double getCurrentBowlerOvers() { return currentBowlerOvers; }
    public void setCurrentBowlerOvers(double currentBowlerOvers) { this.currentBowlerOvers = currentBowlerOvers; }

    public int getCurrentBowlerRuns() { return currentBowlerRuns; }
    public void setCurrentBowlerRuns(int currentBowlerRuns) { this.currentBowlerRuns = currentBowlerRuns; }

    public int getCurrentBowlerWickets() { return currentBowlerWickets; }
    public void setCurrentBowlerWickets(int currentBowlerWickets) { this.currentBowlerWickets = currentBowlerWickets; }

    public String getLatestCommentary() { return latestCommentary; }
    public void setLatestCommentary(String latestCommentary) { this.latestCommentary = latestCommentary; }

    public List<String> getRecentBalls() { return recentBalls; }
    public void setRecentBalls(List<String> recentBalls) { this.recentBalls = recentBalls; }

    public String getResultNote() { return resultNote; }
    public void setResultNote(String resultNote) { this.resultNote = resultNote; }

    public String getWinnerTeamName() { return winnerTeamName; }
    public void setWinnerTeamName(String winnerTeamName) { this.winnerTeamName = winnerTeamName; }
}
