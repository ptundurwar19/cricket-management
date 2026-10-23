package com.cricket.model;

public enum MatchFormat {
    T20(20, 4),
    ODI(50, 10),
    TEST(90, 90),
    BLITZ(10, 2);

    private final int defaultOvers;
    private final int maxOversPerBowler;

    MatchFormat(int defaultOvers, int maxOversPerBowler) {
        this.defaultOvers = defaultOvers;
        this.maxOversPerBowler = maxOversPerBowler;
    }

    public int getDefaultOvers() {
        return defaultOvers;
    }

    public int getMaxOversPerBowler() {
        return maxOversPerBowler;
    }
}
