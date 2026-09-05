package com.raja.aviator.strategies;


//Individually making 50k and group making 10k// use full
public class StrategyGapTap {

    private int roundsSince100x = -1;

    /**
     * Returns:
     * true  -> place/continue bet on the next round
     * false -> don't bet on the next round
     * <p>
     * Strategy:
     * 20-24
     * 28-32
     * 48-52
     * 65-69
     * <p>
     * A multiplier >= 100x resets the cycle.
     */
    public boolean decisionMaker(double multiplier) {

        // A new >100x result starts a new cycle.
        if (multiplier >= 100.0) {
            roundsSince100x = 0;
            return false;
        }

        // We don't have a >100x reference yet.
        if (roundsSince100x < 0) {
            return false;
        }

        // Move to the next round after the previous result.
        roundsSince100x++;

        // Stop this cycle after round 69.
        if (roundsSince100x > 187) {
            return false;
        }

        // Betting windows.
        return isBettingWindow(roundsSince100x);
    }

    private boolean isBettingWindow(int round) {
        return
                (round >= 23 && round <= 24)
                        || (round >= 29 && round <= 30)
                        || (round >= 48 && round <= 52)
                        || (round >= 62 && round <= 66)
                        || (round >= 171 && round <= 185);
    }
}