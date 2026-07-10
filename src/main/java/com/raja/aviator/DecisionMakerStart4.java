package com.raja.aviator;

import java.util.ArrayList;
import java.util.List;

public class DecisionMakerStart4 {

    // Define explicit states for each step of your requirements
    public enum State {
        SEARCHING_PATTERN, // Looking for [>=100, <100, <100] (Steps 1 & 2)
        WAITING_1,         // Step 3: Waiting for 20 counts
        BETTING_1,         // Step 4: Betting for 70 counts
        WAITING_2,         // Step 6: Waiting for 20 counts after a win
        BETTING_2          // Step 7: Betting for 70 counts
    }

    private State state = State.SEARCHING_PATTERN;
    private static final double HUNDRED = 100.0;

    private boolean betButtonStatus = false;
    private int lastHundredBefore = 0;

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
                case WAITING_1:
                case WAITING_2:
                    // If 100x hits during Step 3 or 6, reset everything
                    state = State.SEARCHING_PATTERN;
                    break;
                case BETTING_1:
                    // Step 5: 100x found during bet, move to next waiting phase (Step 6)
                    state = State.WAITING_2;
                    break;
                case BETTING_2:
                    // Step 7: 100x found during 2nd bet cycle, cycle is over, reset
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

                // Step 1 & 2: 3-digit (>= 100) followed by two 2-digit (< 100)
                if (thirdLast >= 100 && secondLast < 100 && last < 100) {
                    state = State.WAITING_1; // Trigger Step 3
                }
            }

        } else {
            // Normal tick (multiplier < 100)
            lastHundredBefore++;

            // Process tick durations for current state
            switch (state) {
                case WAITING_1:
                    // Step 3: Wait until count 20
                    if (lastHundredBefore == 20) {
                        state = State.BETTING_1;
                        betButtonStatus = true; // Turn bet ON
                    }
                    break;
                case BETTING_1:
                    // Step 5 alternative: 20 wait + 70 bet = 90 total.
                    // If we reach 90 and no 100x was found, reset everything.
                    if (lastHundredBefore == 80) {
                        state = State.SEARCHING_PATTERN;
                        betButtonStatus = false;
                    }
                    break;
                case WAITING_2:
                    // Step 6: Wait until count 20
                    if (lastHundredBefore == 60) {
                        state = State.BETTING_2;
                        betButtonStatus = true; // Turn bet ON
                    }
                    break;
                case BETTING_2:
                    // Step 7 alternative: 20 wait + 70 bet = 90 total. Reset when done.
                    if (lastHundredBefore == 85) {
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