package com.cricket.dto;

import com.cricket.model.MatchFormat;

public class CreateMatchRequest {
    private String title;
    private String seriesName;
    private String venue;
    private MatchFormat format = MatchFormat.T20;
    private int totalOvers = 20;
    private Long teamAId;
    private Long teamBId;
    private Long tossWinnerId;
    private String tossChoice = "BAT"; // "BAT" or "BOWL"

    public CreateMatchRequest() {}

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

    public Long getTeamAId() { return teamAId; }
    public void setTeamAId(Long teamAId) { this.teamAId = teamAId; }

    public Long getTeamBId() { return teamBId; }
    public void setTeamBId(Long teamBId) { this.teamBId = teamBId; }

    public Long getTossWinnerId() { return tossWinnerId; }
    public void setTossWinnerId(Long tossWinnerId) { this.tossWinnerId = tossWinnerId; }

    public String getTossChoice() { return tossChoice; }
    public void setTossChoice(String tossChoice) { this.tossChoice = tossChoice; }
}
