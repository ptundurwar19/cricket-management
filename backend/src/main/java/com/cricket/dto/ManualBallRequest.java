package com.cricket.dto;

import com.cricket.model.ExtraType;
import com.cricket.model.WicketType;

public class ManualBallRequest {
    private int runsScored = 0;
    private ExtraType extraType = ExtraType.NONE;
    private int extraRuns = 0;
    private boolean isWicket = false;
    private WicketType wicketType;
    private String customCommentary;

    public ManualBallRequest() {}

    public int getRunsScored() { return runsScored; }
    public void setRunsScored(int runsScored) { this.runsScored = runsScored; }

    public ExtraType getExtraType() { return extraType; }
    public void setExtraType(ExtraType extraType) { this.extraType = extraType; }

    public int getExtraRuns() { return extraRuns; }
    public void setExtraRuns(int extraRuns) { this.extraRuns = extraRuns; }

    public boolean isWicket() { return isWicket; }
    public void setWicket(boolean wicket) { isWicket = wicket; }

    public WicketType getWicketType() { return wicketType; }
    public void setWicketType(WicketType wicketType) { this.wicketType = wicketType; }

    public String getCustomCommentary() { return customCommentary; }
    public void setCustomCommentary(String customCommentary) { this.customCommentary = customCommentary; }
}
