package com.raja.aviator;

import com.city.detective.model.ButtonController;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;


public class DecisionMakerStart3 {

    public enum State {
        ANY, START1, START2, START3, START4,
    }

    private State state;

    private static final double HUNDRED = 100;

    private int betOffAfterOccurrence = Integer.MAX_VALUE;
    private boolean betButtonStatus = false;

    // NEW FLAG: Tracks if the start2 logic has already fired for the current 100x cycle
    private boolean start2Fired = false;
    private double startAndWin = Integer.MAX_VALUE;

    private final AtomicInteger lastHundredBefore = new AtomicInteger(0);
    private final AtomicInteger betOnCounter = new AtomicInteger(0);
    private final ButtonController btn = new ButtonController(false, false);
    private int wonCount = 0;

    private int lastHundredBeforeHolder = 0;
    private double target = 100;


    private TurnTracker tt = new TurnTracker();

    public double getTargetMultiplier() {
        return target;
    }

    double lastMulti = 0;

    public boolean decisionMaker(double latestMultiplier) {
        btn.setAutoBetOn(false);
        btn.setAutoBetOff(false);

        boolean isHighMultiplier = latestMultiplier >= HUNDRED;
        lastHundredBeforeHolder = lastHundredBefore.get();
        if (isHighMultiplier) {
            lastHundredBefore.set(0);
            start2Fired = false; // RESET FLAG: Allow start2 to run again for the NEXT 100x block
            if (betButtonStatus) {
                wonCount++;
            }
        } else {
            lastHundredBefore.getAndIncrement();
        }

        double score = tt.addTurnAndGetScore(latestMultiplier);

        //if (isHighMultiplier && score <= -1) //allowed -1, -1.2, -2
         //  System.out.println("score " + score);// it won me

       // if (isHighMultiplier && score >= -1) //allows + values 1 , 2 , 3 0
           // System.out.println("score11 " + score); i wou you

        //Start 1 : Most profitable
        if (score <= 0 && isHighMultiplier && !betButtonStatus && !start2Fired && lastHundredBeforeHolder >= 200) {
            triggerBetOn(76);
            start2Fired = true; // LOCK FLAG: Blocks this block from executing again
            startAndWin = 0;
            //System.out.println("............Start1");

        }

        //Start 2 : Most profitable
        if (startAndWin == 0 && isHighMultiplier && !betButtonStatus && !start2Fired) {
            triggerBetOn(76);
            start2Fired = true; // LOCK FLAG: Blocks this block from executing again
            startAndWin = 0;
            System.out.println("............Start2 "+ score );
        }

        //Under testing
        if (isHighMultiplier && !betButtonStatus && !start2Fired && lastHundredBeforeHolder <= 30) {
            triggerBetOn(65);
            start2Fired = true; // LOCK FLAG: Blocks this block from executing again
            System.out.println("............Start3");
            startAndWin = 0;
            state = State.START3;
        }


        betOnCounter.getAndIncrement();

        // Trigger Auto Bet Off conditions
        if (betOnCounter.get() == betOffAfterOccurrence) {
            btn.setAutoBetOff(true);
            betOffAfterOccurrence = Integer.MAX_VALUE;
            startAndWin = wonCount;
            wonCount = 0;
            state = State.ANY;
        }

        if (wonCount == 1 && score <= -4) {
            betOffAfterOccurrence = 120;
        } else if (wonCount == 1) {
            betOffAfterOccurrence = 95;
        }

        if (wonCount == 2 && score <= -3) {
            betOffAfterOccurrence = 140;
        }

        if (wonCount == 1 && betOnCounter.get() <= 10 && score >= 1) {
            betOnCounter.set(betOffAfterOccurrence - 1);
        }

        if (wonCount == 2 && betOnCounter.get() <= 30) {
            betOnCounter.set(betOffAfterOccurrence - 1);
        }

        if (wonCount == 3) {
            betOnCounter.set(betOffAfterOccurrence - 1);
        }

        if (wonCount == 2 && state == State.START3) {
            betOnCounter.set(betOffAfterOccurrence - 1);
        }

        if (state == State.START3) {
            start3test -= 10;
        }

        lastMulti = latestMultiplier;

        if (btn.isAutoBetOn()) {
            betButtonStatus = true;
        }
        if (btn.isAutoBetOff()) {
            betButtonStatus = false;
        }
        return betButtonStatus;
    }

    int start3test = 10000;

    private void triggerBetOn(int duration) {
        btn.setAutoBetOn(true);
        betOnCounter.set(0);
        betOffAfterOccurrence = duration;
    }
}

