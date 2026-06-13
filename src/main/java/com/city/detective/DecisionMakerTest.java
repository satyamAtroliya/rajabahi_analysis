package com.city.detective;

import com.city.detective.model.ButtonController;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class DecisionMakerTest {

    boolean betButtonStatus = false;

    int windowStart = Integer.MAX_VALUE; // Start after each 240 occurrence without 100x
    int wonCount = 0;

    //Initial value should be something which can't be reach easily (5000), just to avoid button tuned on wrong
    int betOffAfterOccurrence = Integer.MAX_VALUE;
    ; //Turn off button after each 58 round
    final double HUNDRED = 100;

    public int getLastHundredBefore() {
        return LastHundredBefore.get();
    }

    AtomicInteger LastHundredBefore = new AtomicInteger(0); //0
    AtomicInteger betOnCounter = new AtomicInteger(0); //0

    ButtonController btn = new ButtonController(false, false);

    boolean start2= false;

    public boolean decisionMaker(double latestMultiplier) {

        if (latestMultiplier >= HUNDRED) {
            start2 = hundredsTrackerDecider(LastHundredBefore.get());
            LastHundredBefore.set(0);
            if (betButtonStatus) {
                wonCount++;
            } else {
                windowStart = 300;
            }
        } else {
            LastHundredBefore.getAndIncrement();
        }

        btn.setAutoBetOn(false);//must
        btn.setAutoBetOff(false);//must

        //Start1
        //No need to simplify - (auto bet button on logic)
     /*   if (LastHundredBefore.get() == windowStart) {
            btn.setAutoBetOn(true);
            betOnCounter.set(0);
            betOffAfterOccurrence = 150;
            wonCount=0;
        }*/

        //start2
        //No need to simplify - (auto bet button on logic)

        if (start2) {
             btn.setAutoBetOn(true);
            betOnCounter.set(0);
            betOffAfterOccurrence = 100;
            wonCount = 0;
        }


        betOnCounter.getAndIncrement();
/*
        //No need to simplify (auto bet button off logic)
        if (betOnCounter.get() == betOffAfterOccurrence) {
            btn.setAutoBetOff(true);
            betOffAfterOccurrence = Integer.MAX_VALUE;
            wonCount = 0;
            start2=false;
        }
*/
  /*      if (wonCount == 1 && betOnCounter.get()<=30) {
            wonCount=0;
            betOnCounter.set(betOffAfterOccurrence - 2);
        }
*/
        if (wonCount == 1) {
            betOnCounter.set(betOffAfterOccurrence - 2);
            wonCount = 0;
        }


        if (btn.isAutoBetOn()) {
            betButtonStatus = true;
        }
        if (btn.isAutoBetOff()) {
            betButtonStatus = false;
        }
        return betButtonStatus;
    }

    LinkedList<Integer> fifoQueue = new LinkedList<>(List.of(100, 100));

    public boolean hundredsTrackerDecider(int tracker100x) {
        if(tracker100x<25)return true;
        return false;
        /*fifoQueue.poll();

        fifoQueue.add(tracker100x);

        int total = 0;
        for (int value : fifoQueue) {
            total += value;
            System.out.println(value);
        }
        float decider = total / 0;

        if (decider <= 133) {
            System.out.println("================decider -===========true "+decider);
            return true;
        }
        System.out.println("================decider -===========false");
        return false;
    */ }

}
