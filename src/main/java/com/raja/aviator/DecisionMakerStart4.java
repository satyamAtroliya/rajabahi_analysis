package com.raja.aviator;

import java.util.ArrayList;
import java.util.List;

public class DecisionMakerStart4 {

    // Define explicit states for each step of your requirements
    public enum State {
        SEARCHING_PATTERN, // Looking for [>=100, <100, <100] (Steps 1 & 2)
        WAITING_A1,         // Step 3: Waiting for 20 counts
        BETTING_A1,         // Step 4: Betting for 70 counts
        WAITING_A2,         // Step 6: Waiting for 20 counts after a win
        BETTING_A2,          // Step 7: Betting for 70 counts,         // Step 4: Betting for 70 counts
        WAITING_A3,         // Step 6: Waiting for 20 counts after a win
        BETTING_A3,          // Step 7: Betting for 70 counts,         // Step 4: Betting for 70 counts
        WAITING_B1,         // Step 6: Waiting for 20 counts after a win
        BETTING_B1,          // Step 7: Betting for 70 counts,         // Step 4: Betting for 70 counts
        WAITING_B2,         // Step 6: Waiting for 20 counts after a win
        BETTING_B2,              // Step 7: Betting for 70 counts,         // Step 4: Betting for 70 counts
        WAITING_B3,         // Step 6: Waiting for 20 counts after a win
        BETTING_B3          // Step 7: Betting for 70 counts,              // Step 7: Betting for 70 counts,         // Step 4: Betting for 70 counts
    }

    private State state = State.SEARCHING_PATTERN;
    private static final double HUNDRED = 100.0;

    private boolean betButtonStatus = false;
    private int lastHundredBefore = Integer.MAX_VALUE;

    // Initialize with three dummy 3-digit numbers to prevent IndexOutOfBounds exceptions
    // and prevent false-positive pattern matches on startup.
    private List<Integer> list = new ArrayList<>(List.of(150, 150, 150));
    boolean flag = false;

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
                case WAITING_A2:
                    // If 100x hits during Step 3 or 6, reset everything
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_A1:
                    // Step 5: 100x found during bet, move to next waiting phase (Step 6)
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_A2:
                    // Step 7: 100x found during bet cycle, cycle is over, reset
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_A3:
                    // Step 7: 100x found during bet cycle, cycle is over, reset
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_B1:
                    // Step 7: 100x found during bet cycle, cycle is over, reset
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_B2:
                    // Step 7: 100x found during bet cycle, cycle is over, reset
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_B3:
                    // Step 7: 100x found during bet cycle, cycle is over, reset
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

                if (last < 90) {
                    state = State.WAITING_A1; // Trigger Step 3
                }

                // Higher Precedence
                // Step 1 & 2: 3-digit (>= 100) followed by two 2-digit (< 100)
                if (thirdLast >= 100 && secondLast < 100 && last < 100) {
                    state = State.WAITING_B1; // Trigger Step 3
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
                        betButtonStatus = true; // Turn bet ON
                    }
                    break;
                case BETTING_A1:
                    // Step 5 alternative: 20 wait + 70 bet = 90 total.
                    // If we reach 90 and no 100x was found, reset everything.
                    if (lastHundredBefore == 10) {
                        state = State.WAITING_A2;
                        betButtonStatus = false;
                    }
                    break;
                case WAITING_A2:
                    // Step 6: Wait until count 20
                    if (lastHundredBefore == 15) {
                        state = State.BETTING_A2;
                        // if(flag)
                        betButtonStatus = true; // Turn bet ON
                    }
                    break;
                case BETTING_A2:
                    // Step 7 alternative: 20 wait + 70 bet = 90 total. Reset when done.
                    if (lastHundredBefore == 25) {
                        state = State.WAITING_A3;
                        betButtonStatus = false;
                    }
                    break;
                case WAITING_A3:
                    // Step 6: Wait until count 20
                    if (lastHundredBefore == 30) {
                        state = State.BETTING_A3;
                        betButtonStatus = true; // Turn bet ON
                    }
                    break;
                case BETTING_A3:
                    // Step 7 alternative: 20 wait + 70 bet = 90 total. Reset when done.
                    if (lastHundredBefore == 35) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                    }
                    break;
                case WAITING_B1: // independent condition
                    // Step 6: Wait until count 20
                    if (lastHundredBefore == 20) {
                        state = State.BETTING_B1;
                        // if(flag)
                        betButtonStatus = true; // Turn bet ON
                    }
                    break;
                case BETTING_B1:
                    // Step 7 alternative: 20 wait + 70 bet = 90 total. Reset when done.
                    if (lastHundredBefore == 40) {
                        state = State.WAITING_B2;
                        betButtonStatus = false;
                    }
                    break;
                case WAITING_B2:
                    // Step 6: Wait until count 20
                    if (lastHundredBefore == 125) {
                        state = State.BETTING_B2;
                        // if(flag)
                        betButtonStatus = true; // Turn bet ON
                    }
                    break;
                case BETTING_B2:
                    // Step 7 alternative: 20 wait + 70 bet = 90 total. Reset when done.
                    if (lastHundredBefore == 130) {
                        state = State.WAITING_B3;
                        betButtonStatus = false;
                    }
                    break;
                case WAITING_B3:
                    // Step 6: Wait until count 20
                    if (lastHundredBefore == 150) {
                        state = State.BETTING_B3;
                        // if(flag)
                        betButtonStatus = true; // Turn bet ON
                    }
                    break;
                case BETTING_B3:
                    // Step 7 alternative: 20 wait + 70 bet = 90 total. Reset when done.
                    if (lastHundredBefore == 170) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                    }
                    break;
                case SEARCHING_PATTERN:
                default:
                    // Ensure bets are strictly off while searching
                    betButtonStatus = false;
                    break;
            }
        }

        return betButtonStatus;
    }
}