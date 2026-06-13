package com.city.detective;

import com.city.detective.model.ButtonController;

import java.util.concurrent.atomic.AtomicInteger;


public class DecisionMaker {

    boolean betButtonStatus = false;

    int windowStart = Integer.MAX_VALUE; // Start after each 240 occurrence without 100x

    //Initial value should be something which can't be reach easily (5000), just to avoid button tuned on wrong
    int betOffAfterOccurrence = Integer.MAX_VALUE; //Turn off button after each 58 round
    final double HUNDRED = 100; //

    AtomicInteger LastHundredBefore = new AtomicInteger(0); //0
    AtomicInteger betOnCounter = new AtomicInteger(0); //0

    int wonCount = 0;

    ButtonController btn = new ButtonController(false, false);

    public boolean decisionMaker(double latestMultiplier) {

        if (latestMultiplier >= HUNDRED) {
            LastHundredBefore.set(0);
            if (betButtonStatus) {
                wonCount++;
            } else {
                windowStart = 5;
            }
        } else {
            LastHundredBefore.getAndIncrement();
        }

        btn.setAutoBetOn(false);//must
        btn.setAutoBetOff(false);//must

        //No need to simplify - (auto bet button on logic)
        if (LastHundredBefore.get() == windowStart) {
            btn.setAutoBetOn(true);
            betOnCounter.set(0);
            betOffAfterOccurrence = 40;
        }

        betOnCounter.getAndIncrement();

        //No need to simplify (auto bet button off logic)
        if (betOnCounter.get() == betOffAfterOccurrence) {
            btn.setAutoBetOff(true);
            betOffAfterOccurrence = Integer.MAX_VALUE;
            wonCount = 0;
        }

        if (wonCount == 1) {
            betOnCounter.set(betOffAfterOccurrence - 2);
        }

        if (btn.isAutoBetOn()) {
            betButtonStatus = true;
        }
        if (btn.isAutoBetOff()) {
            betButtonStatus = false;
        }
        return betButtonStatus;
    }

}
