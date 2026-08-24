package com.raja.aviator;

public class Strategy30x implements Strategy {

    private static final double TRIGGER = 30.0;

    // After a 20x+ trigger:
    private static final int WAIT_ROUNDS = 72;
    private static final int BET_ROUNDS = 8;

    private int waitCount = 0;
    private int betCount = 0;
    private boolean waiting = false;
    private boolean betting = false;

    public boolean decisionMaker(double multip) {

        // -----------------------------------------
        // Currently in betting window
        // -----------------------------------------
        if (betting) {

            // Bet on this round
            betCount++;

            if (betCount >= BET_ROUNDS) {
                betting = false;
                betCount = 0;
            }

            return true;
        }

        // -----------------------------------------
        // Currently in waiting window
        // -----------------------------------------
        if (waiting) {

            // If another 20x+ appears during waiting:
            // cancel this setup and use this new 20x+
            // as the new starting point.
            if (multip >= TRIGGER) {
                waitCount = 0;
                betCount = 0;

                // Start a fresh 67-round waiting period
                waiting = true;
                return false;
            }

            waitCount++;

            // Waiting completed
            if (waitCount >= WAIT_ROUNDS) {
                waiting = false;
                betting = true;
                betCount = 0;
            }

            return false;
        }

        // -----------------------------------------
        // No active setup
        // Look for starting 20x+
        // -----------------------------------------
        if (multip >= TRIGGER && multip <= 40) {
            waiting = true;
            waitCount = 0;
            betCount = 0;
        }

        return false;
    }
}