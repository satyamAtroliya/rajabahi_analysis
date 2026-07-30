package com.raja.aviator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.raja.aviator.Constants.*;
import static com.raja.aviator.Constants.STRATEGY_100;

public class StrategyO10 implements Strategy {
    private static final Logger log = LoggerFactory.getLogger(StrategyO10.class);

    private static State state = State.SEARCHING_PATTERN;

    static final double HUNDRED = 10;
    private boolean betButtonStatus = false;
    private int lastHundredBefore = Integer.MAX_VALUE;

    private List<Integer> list = new ArrayList<>(List.of(150, 150, 150));

    public boolean decisionMaker(double latestMultiplier) {
        boolean isHighMultiplier = latestMultiplier >= HUNDRED;

        if (isHighMultiplier) {
            list.add(lastHundredBefore);
            lastHundredBefore = 0; // Reset counter
            betButtonStatus = false; // Always stop betting immediately on 100x

            // Handle State transitions based on the 100x hit
            switch (state) {
                case WAITING_A1:
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

                if ( secondLast + last < 65) {
                    state = State.WAITING_A1;
                    System.setProperty(STRATEGYO10, STRATEGYO10);
                    log.warn(" ------------ (SLH) '{}'  + (LH) '{}' < 65 ----- ", secondLast, last );
                    log.warn("------- STRATEGYO10 ------- BETS will Start from next { 24 to 34 } Round ");
                }
            }
        } else {
            // Normal tick (multiplier < 100)
            lastHundredBefore++;
            // Process tick durations for current state
            switch (state) {
                case WAITING_A1:
                    // Step 3: Wait until count 20
                    if (lastHundredBefore == 24) {
                        state = State.BETTING_A1;
                        betButtonStatus = true;
                    }
                    break;
                case BETTING_A1:
                    if (lastHundredBefore == 34) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                        log.warn(" ------------ STRATEGYO10 ( 24 to 34 ) ------ TURNED OFF");
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