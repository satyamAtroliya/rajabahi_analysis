package com.raja.aviator.strategies;

public interface Strategy {

    static final double HUNDRED = 100;
    // Define explicit states for each step of your requirements
    public enum State {
        SEARCHING_PATTERN,
        WAITING_A1,
        BETTING_A1,
        WAITING_A2,
        BETTING_A2,
        WAITING_A3,
        BETTING_A3,
        WAITING_B1,
        BETTING_B1
    }

    boolean decisionMaker(double latestMultiplier);
}
