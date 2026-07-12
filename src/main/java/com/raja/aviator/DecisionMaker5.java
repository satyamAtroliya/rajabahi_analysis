package com.raja.aviator;

public class DecisionMaker5 {

    // Made instance variables private and final where applicable for better encapsulation
    private final DecisionMakerStart3 dm3 = new DecisionMakerStart3();
    private final DecisionMakerStart4 dm4 = new DecisionMakerStart4();

    private double betAmount = 10;

    // Renamed from getamt() to follow standard Java naming conventions
    public double getBetAmount() {
        return betAmount;
    }

    // Renamed 'lm' to 'latestMultiplier' for better readability
    public boolean decisionMaker(double latestMultiplier) {

        // Moved btn3 and btn4 to local variables.
        // There is no need to store these at the class level since they are only used here.
        boolean isDm3Betting = dm3.decisionMaker(latestMultiplier);
        boolean isDm4Betting = dm4.decisionMaker(latestMultiplier);

        if (isDm3Betting && isDm4Betting) {
            betAmount = 10;
            return true;
        } else if (isDm3Betting || isDm4Betting) {
            // Note: If only one is true, betAmount remains whatever it was previously (10 or 20).
            return true;
        } else {
            // Replaced the redundant 'if (!btn3 && !btn4)' with a simple 'else' fallback
            betAmount = 10;
            return false;
        }
    }
}