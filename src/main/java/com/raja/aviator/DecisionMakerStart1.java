package com.raja.aviator;

import com.city.detective.model.ButtonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class DecisionMakerStart1 {

    private static final double HUNDRED = 100.0;

    private int windowStart = Integer.MAX_VALUE;
    private int betOffAfterOccurrence = Integer.MAX_VALUE;
    private int wonCount = 0;
    private int balance = 3000;
    private boolean betButtonStatus = false;

    // NEW FLAG: Tracks if the start2 logic has already fired for the current 100x cycle
    private boolean start2Fired = false;

    private final AtomicInteger lastHundredBefore = new AtomicInteger(0);
    private final AtomicInteger betOnCounter = new AtomicInteger(0);
    private final ButtonController btn = new ButtonController(false, false);

    public boolean decisionMaker(double latestMultiplier) {
        btn.setAutoBetOn(false);
        btn.setAutoBetOff(false);

        boolean isHighMultiplier = latestMultiplier >= HUNDRED;
        boolean isTrackerUnder15 = false;

        if (isHighMultiplier) {
            isTrackerUnder15 = lastHundredBefore.get() < 20;
            lastHundredBefore.set(0);
            start2Fired = false; // RESET FLAG: Allow start2 to run again for the NEXT 100x block

            if (betButtonStatus) {
                wonCount++;
            } else {
                windowStart = 300;
            }
        } else {
            lastHundredBefore.getAndIncrement();
        }

        int currentTracker = lastHundredBefore.get();

        // Trigger Auto Bet On conditions
        if (currentTracker == windowStart) {
            triggerBetOn(150);
        }
        // FIXED: Added !start2Fired to guarantee it executes exactly once per valid 100x drop
        /*else if (isTrackerUnder15 && !betButtonStatus && !start2Fired) {
            triggerBetOn(45);
            start2Fired = true; // LOCK FLAG: Blocks this block from executing again
        }*/
        betOnCounter.getAndIncrement();

        // Trigger Auto Bet Off conditions
        if (betOnCounter.get() == betOffAfterOccurrence) {
            btn.setAutoBetOff(true);
            betOffAfterOccurrence = Integer.MAX_VALUE;
            wonCount = 0;
        }

        if (wonCount == 2) {
            wonCount = 0;
            betOnCounter.set(betOffAfterOccurrence - 1);
        }

        if (wonCount == 1 && betOnCounter.get() == 80) {
            betOnCounter.set(betOffAfterOccurrence - 1);
        }

        if (btn.isAutoBetOn()) {
            betButtonStatus = true;
        }
        if (btn.isAutoBetOff()) {
            betButtonStatus = false;
        }
        return betButtonStatus;
    }

    private void triggerBetOn(int duration) {
        btn.setAutoBetOn(true);
        betOnCounter.set(0);
        betOffAfterOccurrence = duration;
        wonCount = 0;
    }

}