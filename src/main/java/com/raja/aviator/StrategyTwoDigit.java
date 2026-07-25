package com.raja.aviator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.raja.aviator.Constants.STRATEGY_TD_A1;
import static com.raja.aviator.Constants.STRATEGY_TD_B1;

public class StrategyTwoDigit implements Strategy {
    private static final Logger log = LoggerFactory.getLogger(StrategyTwoDigit.class);

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

    private static State state = State.SEARCHING_PATTERN;
    private static final double HUNDRED = 100.0;

    private boolean betButtonStatus = false;
    private int lastHundredBefore = Integer.MAX_VALUE;

    // Initialize with three dummy 3-digit numbers to prevent IndexOutOfBounds exceptions
    // and prevent false-positive pattern matches on startup.
    private List<Integer> list = new ArrayList<>(List.of(150, 150, 150));
    boolean flag = false;
    int round = 0;

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
                case WAITING_A2:
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_A1:
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_A2:
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_A3:
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_B1:
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

                if (secondLast < 90 && last < 90 && flag) {
                    state = State.WAITING_A1;
                    System.setProperty(STRATEGY_TD_A1, STRATEGY_TD_A1);
                    log.warn("----- (SLH) '{}' < 90 AND (LH) '{}' < 90  ", secondLast, last);
                    log.warn("------- STRATEGY_TD_A1 ------- BETS will ON from next {30 to 40} , {70 to 90} , {125 to 180} Round");
                    round++;
                    if (round == 4)
                        flag = false;
                }

                if (last > 100) {
                    flag = true;
                    round = 0;
                }
                // Higher Precedence
                if (last > 100 && last < 160) {
                    state = State.WAITING_B1;
                    System.setProperty(STRATEGY_TD_B1, STRATEGY_TD_B1);
                    log.warn(" ------------ 100 < (LH) '{}' < 160 ----- ", last);
                    log.warn("------- STRATEGY_TD_B1 ------- BETS will ON from next { 150 to 165 } Rounf");
                }
            }
        } else {
            // Normal tick (multiplier < 100)
            lastHundredBefore++;
            // Process tick durations for current state
            switch (state) {
                case WAITING_A1:
                    // Step 3: Wait until count 20
                    if (lastHundredBefore == 30) {
                        state = State.BETTING_A1;
                        betButtonStatus = true;
                    }
                    break;
                case BETTING_A1:
                    if (lastHundredBefore == 40) {
                        state = State.WAITING_A2;
                        betButtonStatus = false;
                        log.warn(" ------------ STRATEGY_TD_A1 ( 30 to 40 ) ------ TURNED OFF");
                        System.setProperty(STRATEGY_TD_A1, "");
                    }
                    break;
                case WAITING_A2:
                    if (lastHundredBefore == 70) {
                        state = State.BETTING_A2;
                        betButtonStatus = true;
                    }
                    break;
                case BETTING_A2:
                    if (lastHundredBefore == 90) {
                        state = State.WAITING_A3;
                        betButtonStatus = false;
                        log.warn(" ------------ STRATEGY_TD_A1 ( 70 to 90 ) ------ TURNED OFF");
                        System.setProperty(STRATEGY_TD_A1, "");
                    }
                    break;
                case WAITING_A3:
                    if (lastHundredBefore == 125) {
                        state = State.BETTING_A3;
                        betButtonStatus = true;
                    }
                    break;
                case BETTING_A3:
                    if (lastHundredBefore == 180) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                        log.warn(" ------------ STRATEGY_TD_A1 ( 125 to 180 ) ------ TURNED OFF");
                        System.setProperty(STRATEGY_TD_A1, "");
                    }
                    break;
                case WAITING_B1: // independent condition
                    if (lastHundredBefore == 150) {
                        state = State.BETTING_B1;
                        betButtonStatus = true;
                    }
                    break;
                case BETTING_B1:
                    if (lastHundredBefore == 165) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                        log.warn(" ------------ STRATEGY_TD_B1 ( 150 to 165 ) ------ TURNED OFF");
                        System.setProperty(STRATEGY_TD_B1, "");
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