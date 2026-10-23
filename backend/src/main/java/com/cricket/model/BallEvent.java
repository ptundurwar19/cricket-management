package com.cricket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ball_events")
public class BallEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long matchId;
    private int inningsNumber;
    private int overNumber;
    private int ballNumberInOver;
    private String ballDisplay; // e.g., "14.2"
    private String batsmanName;
    private String bowlerName;
    private int runsScored = 0;

    @Enumerated(EnumType.STRING)
    private ExtraType extraType = ExtraType.NONE;

    private int extraRuns = 0;
    private int totalBallRuns = 0;
    private boolean isLegalBall = true;
    private boolean isWicket = false;

    @Enumerated(EnumType.STRING)
    private WicketType wicketType;

    private String outBatsmanName;

    @Column(length = 1000)
    private String commentary;

    private long timestamp = System.currentTimeMillis();

    public BallEvent() {}

    public BallEvent(Long matchId, int inningsNumber, int overNumber, int ballNumberInOver,
                     String ballDisplay, String batsmanName, String bowlerName,
                     int runsScored, ExtraType extraType, int extraRuns,
                     boolean isLegalBall, boolean isWicket, WicketType wicketType,
                     String outBatsmanName, String commentary) {
        this.matchId = matchId;
        this.inningsNumber = inningsNumber;
        this.overNumber = overNumber;
        this.ballNumberInOver = ballNumberInOver;
        this.ballDisplay = ballDisplay;
        this.batsmanName = batsmanName;
        this.bowlerName = bowlerName;
        this.runsScored = runsScored;
        this.extraType = extraType != null ? extraType : ExtraType.NONE;
        this.extraRuns = extraRuns;
        this.totalBallRuns = runsScored + extraRuns;
        this.isLegalBall = isLegalBall;
        this.isWicket = isWicket;
        this.wicketType = wicketType;
        this.outBatsmanName = outBatsmanName;
        this.commentary = commentary;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public int getInningsNumber() { return inningsNumber; }
    public void setInningsNumber(int inningsNumber) { this.inningsNumber = inningsNumber; }

    public int getOverNumber() { return overNumber; }
    public void setOverNumber(int overNumber) { this.overNumber = overNumber; }

    public int getBallNumberInOver() { return ballNumberInOver; }
    public void setBallNumberInOver(int ballNumberInOver) { this.ballNumberInOver = ballNumberInOver; }

    public String getBallDisplay() { return ballDisplay; }
    public void setBallDisplay(String ballDisplay) { this.ballDisplay = ballDisplay; }

    public String getBatsmanName() { return batsmanName; }
    public void setBatsmanName(String batsmanName) { this.batsmanName = batsmanName; }

    public String getBowlerName() { return bowlerName; }
    public void setBowlerName(String bowlerName) { this.bowlerName = bowlerName; }

    public int getRunsScored() { return runsScored; }
    public void setRunsScored(int runsScored) { this.runsScored = runsScored; }

    public ExtraType getExtraType() { return extraType; }
    public void setExtraType(ExtraType extraType) { this.extraType = extraType; }

    public int getExtraRuns() { return extraRuns; }
    public void setExtraRuns(int extraRuns) { this.extraRuns = extraRuns; }

    public int getTotalBallRuns() { return totalBallRuns; }
    public void setTotalBallRuns(int totalBallRuns) { this.totalBallRuns = totalBallRuns; }

    public boolean isLegalBall() { return isLegalBall; }
    public void setLegalBall(boolean legalBall) { isLegalBall = legalBall; }

    public boolean isWicket() { return isWicket; }
    public void setWicket(boolean wicket) { isWicket = wicket; }

    public WicketType getWicketType() { return wicketType; }
    public void setWicketType(WicketType wicketType) { this.wicketType = wicketType; }

    public String getOutBatsmanName() { return outBatsmanName; }
    public void setOutBatsmanName(String outBatsmanName) { this.outBatsmanName = outBatsmanName; }

    public String getCommentary() { return commentary; }
    public void setCommentary(String commentary) { this.commentary = commentary; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
