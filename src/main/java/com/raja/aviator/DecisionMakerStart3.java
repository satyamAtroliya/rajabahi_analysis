package com.raja.aviator;

import com.city.detective.model.ButtonController;
import java.util.concurrent.atomic.AtomicInteger;

public class DecisionMakerStart3 {

    private enum StrategyState {
        WAITING_FOR_START1,
        START1_ACTIVE,
        START2_READY,
        START2_DELAYED,     // Waiting for the 50-occurrence delay before Start 2
        START2_ACTIVE,
        START3_READY,
        START3_DELAYED,     // Waiting for the 50-occurrence delay before Start 3
        START3_ACTIVE
    }

    private static final double HUNDRED = 100;

    private StrategyState currentState = StrategyState.WAITING_FOR_START1;

    private boolean betButtonStatus = false;
    private boolean start2Fired = false;
    private int betOffAfterOccurrence = Integer.MAX_VALUE;

    // Delayed Betting Control Variables
    private int activeBetDurationPending = 0;
    private int occurrencesRemainingToWait = 0;

    private int wonCount = 0;
    private int lastHundredBeforeHolder = 0;

    private final AtomicInteger lastHundredBefore = new AtomicInteger(0);
    private final AtomicInteger betOnCounter = new AtomicInteger(0);

    private final ButtonController btn = new ButtonController(false, false);
    private final TurnTracker tt = new TurnTracker();

    public double getTargetMultiplier() {
        return HUNDRED;
    }

    public boolean decisionMaker(double latestMultiplier) {
        // 1. Reset interface commands for this tick
        btn.setAutoBetOn(false);
        btn.setAutoBetOff(false);

        // 2. Track 100x multiplier cycles
        boolean isHighMultiplier = latestMultiplier >= HUNDRED;
        lastHundredBeforeHolder = lastHundredBefore.get();

        if (isHighMultiplier) {
            lastHundredBefore.set(0);
            start2Fired = false;
            if (betButtonStatus) wonCount++;
        } else {
            lastHundredBefore.getAndIncrement();
        }

        double score = tt.addTurnAndGetScore(latestMultiplier);

        // 3. Handle Active Delay Countdown Engines
        handleDelayedBetCountdown();

        // 4. Evaluate Betting Strategy Triggers
        if (isHighMultiplier && !betButtonStatus && !start2Fired) {

            // Strategy 1: Highest priority, starts immediately (0 delay)
            if (score >= 0 && lastHundredBeforeHolder >= 200) {
                triggerBetOn(76, 1);
                start2Fired = true;
                currentState = StrategyState.START1_ACTIVE;
            }
            // Strategy 2: Runs once if Strategy 1 lost (Starts after 50 occurrences pass)
            else if (currentState == StrategyState.START2_READY && lastHundredBeforeHolder <= 199) {
                triggerBetOn(35, 90);
                start2Fired = true;
                currentState = StrategyState.START2_DELAYED;
            }
            // Strategy 3: Runs once after Strategy 2 completes (Starts after 50 occurrences pass)
            else if (currentState == StrategyState.START3_READY && lastHundredBeforeHolder <= 199) {
                triggerBetOn(35, 35);
                start2Fired = true;
                currentState = StrategyState.START3_DELAYED;
             }
        }

        betOnCounter.getAndIncrement();

        // 5. Handle Active Bet Window Expirations
        if (betOnCounter.get() == betOffAfterOccurrence) {
            btn.setAutoBetOff(true);
            betOffAfterOccurrence = Integer.MAX_VALUE;

            // State resolution router
            if (currentState == StrategyState.START1_ACTIVE) {
                currentState = (wonCount > 0) ? StrategyState.WAITING_FOR_START1 : StrategyState.START2_READY;
            } else if (currentState == StrategyState.START2_ACTIVE) {
                currentState = StrategyState.START3_READY;
            } else if (currentState == StrategyState.START3_ACTIVE) {
                currentState = StrategyState.WAITING_FOR_START1;
            }
            //System.out.println(" Won count at closer =  "+wonCount);
            wonCount = 0;
        }

        // 6. Dynamic Window Management (Applied only when an active bet window runs)
        if (currentState == StrategyState.START1_ACTIVE || currentState == StrategyState.START2_ACTIVE || currentState == StrategyState.START3_ACTIVE) {
            if (wonCount == 1) {
                betOffAfterOccurrence = (score >= 5) ? 130 : 95;
            } else if (wonCount == 2 && score >= 4) {
                betOffAfterOccurrence = 155;
            }

            if ((wonCount == 1 && betOnCounter.get() <= 15) || (wonCount == 2 && betOnCounter.get() <= 40) || (wonCount == 3)) {
                betOnCounter.set(betOffAfterOccurrence - 1);
            }
            if (wonCount == 2 && score <= 1){
                betOnCounter.set(betOffAfterOccurrence - 1);
            }
            if (wonCount == 2 && currentState == StrategyState.START2_ACTIVE){
                betOnCounter.set(betOffAfterOccurrence - 1);
            }
            if (wonCount == 2 && currentState == StrategyState.START3_ACTIVE){
                betOnCounter.set(betOffAfterOccurrence - 1);
            }

        }

        // 7. Synchronize local engine status
        if (btn.isAutoBetOn())  betButtonStatus = true;
        if (btn.isAutoBetOff()) betButtonStatus = false;

        return betButtonStatus;
    }

    /**
     * Updated trigger function to handle both immediate and delayed execution profiles.
     */
    private void triggerBetOn(int duration, int delay) {
        if (delay > 0) {
            this.occurrencesRemainingToWait = delay;
            this.activeBetDurationPending = duration;
        } else {
            btn.setAutoBetOn(true);
            betOnCounter.set(0);
            betOffAfterOccurrence = duration;
        }
    }

    /**
     * Ticks down remaining delay steps and kicks off the active bet session at zero.
     */
    private void handleDelayedBetCountdown() {
        if (occurrencesRemainingToWait <= 0) {
            return;
        }

        occurrencesRemainingToWait--;

        if (occurrencesRemainingToWait == 0) {
            // Delay finished, execute actual hardware triggers now
            btn.setAutoBetOn(true);
            betOnCounter.set(0);
            betOffAfterOccurrence = activeBetDurationPending;

            // Transition state from DELAYED to ACTIVE
            if (currentState == StrategyState.START2_DELAYED) {
                currentState = StrategyState.START2_ACTIVE;
               // System.out.println(">> Delay complete: start22 is now ACTIVE");
            } else if (currentState == StrategyState.START3_DELAYED) {
                currentState = StrategyState.START3_ACTIVE;
               // System.out.println(">> Delay complete: start33 is now ACTIVE");
            }
        }
    }
}
