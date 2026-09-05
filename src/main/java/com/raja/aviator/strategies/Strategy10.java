package com.raja.aviator.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.raja.aviator.Constants.STRATEGY_10;

public class Strategy10 implements Strategy {
    private static final Logger log = LoggerFactory.getLogger(Strategy10.class);

    private static State state = State.SEARCHING_PATTERN;
    private static final double HUNDRED = 100.0;

    private boolean betButtonStatus = false;
    private int lastHundredBefore = Integer.MAX_VALUE;

    // Initialize with three dummy 3-digit numbers to prevent IndexOutOfBounds exceptions
    // and prevent false-positive pattern matches on startup.
    private List<Integer> list = new ArrayList<>(List.of(150, 150, 150));

    public boolean decisionMaker(double latestMultiplier) {
        boolean isHighMultiplier = latestMultiplier >= HUNDRED;

        if (isHighMultiplier) {
            // A 100x multiplier hit!
            list.add(lastHundredBefore);
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
                int size = list.size();
                int thirdLast = list.get(size - 3);
                int secondLast = list.get(size - 2);
                int last = list.get(size - 1);

                if (last > 25 && last < 70) {
                    state = State.WAITING_A1;
                    System.setProperty(STRATEGY_10, STRATEGY_10);
                    // log.warn(" ------------ 25 < (LH) '{}' < 75 ----- ", last);
                    //log.warn("----- STRATEGY_10 ------ Bets will remain ON till next { 12 } Round");
                }

            }
        } else {
            // Normal tick (multiplier < 100)
            lastHundredBefore++;
            // Process tick durations for current state
            switch (state) {
                case WAITING_A1:
                    // Step 3: Wait until count 20
                    if (lastHundredBefore == 1) {
                        state = State.BETTING_A1;
                        betButtonStatus = true;
                    }
                    break;
                case BETTING_A1:
                    if (lastHundredBefore == 12) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                        //  log.warn(" ------------ STRATEGY_10 ( 2 to 12 ) ------ TURNED OFF");
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