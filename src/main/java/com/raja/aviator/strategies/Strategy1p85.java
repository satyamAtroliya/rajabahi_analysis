package com.raja.aviator.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.raja.aviator.Constants.STRATEGY_10;

public class Strategy1p85 implements Strategy {
    private static final Logger log = LoggerFactory.getLogger(Strategy1p85.class);

    private static State state = State.SEARCHING_PATTERN;

    private boolean betButtonStatus = false;
    private int lastHundredBefore = Integer.MAX_VALUE;

    double f1;
    double f2;
    double f3;

    public boolean decisionMaker(double latestMultiplier) {
        //  boolean isHighMultiplier = latestMultiplier >= HUNDRED;
        f3=f2;
        f2=f1;
        f1=latestMultiplier;

        if (!state.equals(State.WAITING_A1)) {

            lastHundredBefore = 0; // Reset counter
            betButtonStatus = false; // Always stop betting immediately on 100x

            // Handle State transitions based on the 100x hit
            switch (state) {
                case WAITING_A1:
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_A1:
                    state = State.SEARCHING_PATTERN;
                    break;
                case SEARCHING_PATTERN:
                    // Stay in searching mode
                    break;
            }

            // If we are in SEARCHING_PATTERN, check if the newly updated list matches the trigger
            if (state == State.SEARCHING_PATTERN) {

                if (f1<=1.85 && f2<=1.85 ) {
                    state = State.WAITING_A1;
                    System.setProperty(STRATEGY_10, STRATEGY_10);
                }

            }
        } else {
            // Normal tick (multiplier < 100)
            lastHundredBefore++;
            // Process tick durations for current state
            switch (state) {
                case WAITING_A1:
                    // Step 3: Wait until count 20
                    if (lastHundredBefore == 2) {
                        state = State.BETTING_A1;
                        betButtonStatus = true;
                    }
                    break;
                case BETTING_A1:
                    if (lastHundredBefore == 3) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                        // log.warn(" ------------ STRATEGY_10 ( 2 to 12 ) ------ TURNED OFF");
                    }
                    break;
                case SEARCHING_PATTERN:
                default:
                    betButtonStatus = false;
                    break;
            }
        }

        return betButtonStatus;
    }
}