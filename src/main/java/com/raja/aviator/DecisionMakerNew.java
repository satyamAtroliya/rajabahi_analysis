package com.raja.aviator;

import com.city.detective.model.ButtonController;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class DecisionMakerNew {

    boolean betButtonStatus = false;

    int windowStart = Integer.MAX_VALUE; // Start after each 240 occurrence without 100x
    int wonCount = 0;

    //Initial value should be something which can't be reach easily (5000), just to avoid button tuned on wrong
    int betOffAfterOccurrence = Integer.MAX_VALUE;
    //Turn off button after each 58 round
    final double HUNDRED = 100;


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
        if (LastHundredBefore.get() == windowStart) {
            btn.setAutoBetOn(true);
            betOnCounter.set(0);
            betOffAfterOccurrence = 150;
            wonCount=0;
        }

        //start2
        //No need to simplify - (auto bet button on logic)

        if (start2 && !betButtonStatus) {
             btn.setAutoBetOn(true);

            betOnCounter.set(0);
            betOffAfterOccurrence = 50;
            wonCount = 0;
        }


        betOnCounter.getAndIncrement();

        //No need to simplify (auto bet button off logic)
        if (betOnCounter.get() == betOffAfterOccurrence) {
            btn.setAutoBetOff(true);
            betOffAfterOccurrence = Integer.MAX_VALUE;
            wonCount = 0;
            start2=false;
        }

  /*      if (wonCount == 1 && betOnCounter.get()<=30) {
            wonCount=0;
            betOnCounter.set(betOffAfterOccurrence - 2);
        }
*/
        if (wonCount == 2) {
            betOnCounter.set(betOffAfterOccurrence - 2);
            wonCount = 0;
        }


        if (btn.isAutoBetOn()) {
            betButtonStatus = true;
            System.out.println("Auto bet onnnnnnnnn.................");
        }
        if (btn.isAutoBetOff()) {
            betButtonStatus = false;
            System.out.println("Auto bet offfffffffffffffffff...........");
        }
        return betButtonStatus;
    }

    public boolean hundredsTrackerDecider(int tracker100x) {
        if(tracker100x<15)return true;
        return false;
       }

}
