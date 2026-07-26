package com.raja.aviator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.raja.aviator.Constants.STRATEGYO10;
import static com.raja.aviator.Constants.STRATEGY_10;

public class StrategyO10 implements Strategy {
    private static final Logger log = LoggerFactory.getLogger(StrategyO10.class);

    private static State state = State.SEARCHING_PATTERN;
    private static final double HUNDRED = 10.0;

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

            state = State.SEARCHING_PATTERN;

            // If we are in SEARCHING_PATTERN, check if the newly updated list matches the trigger
            int size = list.size();
            int secondLast = list.get(size - 2);
            int last = list.get(size - 1);

            if (last > 10) {
                state = State.WAITING_A1;
                System.setProperty(STRATEGYO10, STRATEGYO10);
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
                        log.warn("----- Trigger condition was if (last > 10) --------------- now --");
                        log.warn("------- "+STRATEGYO10+" ------- BETS are ON from next {24 to 38}");
                    }
                    break;
                case BETTING_A1:
                    if (lastHundredBefore == 38) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                        log.warn(" ------------ "+STRATEGYO10+" ( 24 to 38 ) ------ TURNED OFF");
                        System.setProperty(STRATEGYO10, "");
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