package com.raja.aviator;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.raja.aviator.Constants.STRATEGY_SS_70;

public class StrategySentiment implements Strategy {

    private static final int WINDOW_SIZE = 8;
    private static final int BET_ROUNDS = 7;

    private static final double BIG_THRESHOLD = 10.0;
    private static final double VERY_BIG_THRESHOLD = 29.0;

    private final Deque<Double> recentResults = new ArrayDeque<>();

    // Number of future rounds for which betting remains active.
    private int bettingRoundsRemaining = 0;

    /**
     * Accepts the latest Aviator multiplier.
     *
     * @param multiplier latest Aviator result
     * @return true  -> bet on the NEXT round
     *         false -> do not bet on the NEXT round
     */
    public boolean decisionMaker(double multiplier) {

        if (multiplier <= 0) {
            throw new IllegalArgumentException(
                    "Multiplier must be greater than 0"
            );
        }

        /*
         * If we are already inside an active betting window,
         * keep betting for the configured number of rounds.
         */
        if (bettingRoundsRemaining > 0) {

            bettingRoundsRemaining--;

            /*
             * Add current result after processing the active bet period.
             */
            addResult(multiplier);

            return true;
        }

        /*
         * We are currently NOT betting.
         * Add the latest result to history.
         */
        addResult(multiplier);

        /*
         * Need at least 10 rounds before sentiment can be calculated.
         */
        if (recentResults.size() < WINDOW_SIZE) {
            return false;
        }

        /*
         * GREEN condition:
         *
         * At least 3 results >= 10x
         * AND
         * At least 1 result >= 20x
         */
        int count10x = 0;
        int count20x = 0;

        for (double result : recentResults) {

            if (result >= BIG_THRESHOLD) {
                count10x++;
            }

            if (result >= VERY_BIG_THRESHOLD) {
                count20x++;
            }
        }

        boolean green =
                count10x >= 2 &&
                        count20x >= 1;

        if (green) {

            /*
             * Start betting on the NEXT 10 rounds.
             */
            bettingRoundsRemaining = BET_ROUNDS;
            System.setProperty(STRATEGY_SS_70, STRATEGY_SS_70);
            return true;
        }
        else
            System.setProperty(STRATEGY_SS_70, "");

        return false;
    }

    /**
     * Adds result to rolling 10-round window.
     */
    private void addResult(double multiplier) {

        recentResults.addLast(multiplier);

        if (recentResults.size() > WINDOW_SIZE) {
            recentResults.removeFirst();
        }
    }

    /**
     * Returns how many rounds are currently remaining
     * in the active betting window.
     */
    public int getBettingRoundsRemaining() {
        return bettingRoundsRemaining;
    }

    /**
     * Clears all history and resets the strategy.
     */
    public void reset() {
        recentResults.clear();
        bettingRoundsRemaining = 0;
    }
}