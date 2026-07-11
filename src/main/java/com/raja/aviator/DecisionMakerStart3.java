package com.raja.aviator;

public class DecisionMakerStart3 {

    private static final double HUNDRED = 100.0;

    private int betOffAfterOccurrence = Integer.MAX_VALUE;
    private boolean betButtonStatus = false;
    private boolean start2Fired = false;

    private int lastHundredBefore = 0;
    private int betOnCounter = 0;
    private int wonCount = 0;

    public boolean decisionMaker(double latestMultiplier) {
        boolean isHighMultiplier = latestMultiplier >= HUNDRED;
        int previousLastHundred = lastHundredBefore;

        // 1. Process Multiplier and Track Hits
        if (isHighMultiplier) {
            lastHundredBefore = 0;
            start2Fired = false;
            if (betButtonStatus) {
                wonCount++;
            }
        } else {
            lastHundredBefore++;
        }

        // 2. Start Betting Logic
        if (isHighMultiplier && !betButtonStatus && !start2Fired && previousLastHundred >= 200) {
            triggerBetOn(76);
            start2Fired = true;
        }

        // 3. Process Active Betting Logic & Auto-Stop
        betOnCounter++;

        if (betOnCounter == betOffAfterOccurrence) {
            betButtonStatus = false;
            betOffAfterOccurrence = Integer.MAX_VALUE;
            wonCount = 0;
        }

        // 4. Adjust Future Durations and Early Stop Triggers based on Wins
        if (wonCount == 1) {
            betOffAfterOccurrence = 85;
            if (betOnCounter <= 20) betOnCounter = betOffAfterOccurrence - 1;
        } else if (wonCount == 2) {
            betOffAfterOccurrence = 120;
            if (betOnCounter <= 40) betOnCounter = betOffAfterOccurrence - 1;
        } else if (wonCount >= 3) {
            betOnCounter = betOffAfterOccurrence - 1;
        }

        return betButtonStatus;
    }

    private void triggerBetOn(int duration) {
        betButtonStatus = true;
        betOnCounter = 0;
        betOffAfterOccurrence = duration;
    }
}