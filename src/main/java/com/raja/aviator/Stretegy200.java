package com.raja.aviator;

public class Stretegy200 {

    private static final double HUNDRED = 100.0;

    private int betOffAfterOccurrence = Integer.MAX_VALUE;
    private boolean betButtonStatus = false;

    private int lastHundredBefore = 0;
    private int betOnCounter = 0;
    private int wonCount = 0;

    public boolean decisionMaker(double latestMultiplier) {
        boolean isHighMultiplier = latestMultiplier >= HUNDRED;
        int previousLastHundred = lastHundredBefore;

        // 1. Process Multiplier and Track Hits
        if (isHighMultiplier) {
            lastHundredBefore = 0;
            if (betButtonStatus) {
                wonCount++;
            }
        } else {
            lastHundredBefore++;
        }

        // 2. Start Betting Logic
        if (isHighMultiplier && !betButtonStatus && previousLastHundred >= 200 && previousLastHundred <= 402) {
            triggerBetOn(76);
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
            betOffAfterOccurrence = 135;
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