package com.raja.aviator;

import com.city.detective.model.ButtonController;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;


public class DecisionMakerStart3 {

    private static final double HUNDRED = 100;

    private int betOffAfterOccurrence = Integer.MAX_VALUE;
    private boolean betButtonStatus = false;

    // NEW FLAG: Tracks if the start2 logic has already fired for the current 100x cycle
    private boolean start2Fired = false;

    private final AtomicInteger lastHundredBefore = new AtomicInteger(0);
    private final AtomicInteger betOnCounter = new AtomicInteger(0);
    private final ButtonController btn = new ButtonController(false, false);
    private int wonCount = 0;

    private int hold100hand = 0;

    private double target=100;
    private TurnTracker tt = new TurnTracker();

    public double getTargetMultiplier(){
        return  target;
    }

    double lastMulti = 0;
    public boolean decisionMaker(double latestMultiplier) {
        btn.setAutoBetOn(false);
        btn.setAutoBetOff(false);

        boolean isHighMultiplier = latestMultiplier >= HUNDRED;
        hold100hand=lastHundredBefore.get();
        if (isHighMultiplier) {
            lastHundredBefore.set(0);
            start2Fired = false; // RESET FLAG: Allow start2 to run again for the NEXT 100x block
            if (betButtonStatus) {
                wonCount++;
            }
        } else {
            lastHundredBefore.getAndIncrement();
        }

        // Trigger Auto Bet On conditions
        if (tt.addTurnAndGetScore(latestMultiplier) >= 0.95 && isHighMultiplier && !betButtonStatus && !start2Fired && hold100hand >=200) {
            triggerBetOn(65);
            start2Fired = true; // LOCK FLAG: Blocks this block from executing again
        }

        betOnCounter.getAndIncrement();

        // Trigger Auto Bet Off conditions
        if (betOnCounter.get() == betOffAfterOccurrence) {
            btn.setAutoBetOff(true);
            betOffAfterOccurrence = Integer.MAX_VALUE;
        }

        if (wonCount == 1) {
            betOffAfterOccurrence=85;
        }
        if (wonCount == 2) {
            betOffAfterOccurrence=125;
        }
        if (wonCount == 3) {
            wonCount = 0;
            betOnCounter.set(betOffAfterOccurrence - 1);
        }

        lastMulti=latestMultiplier;

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
    }
}

