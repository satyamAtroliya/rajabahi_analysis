package com.raja.aviator;

// Save as BettingStrategy.java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BettingStrategy {

    /**
     * Define your betting rules here.
     *
     * @param history Read-only list of all multiplier numbers processed so far.
     * @return A Config object containing your decision for the NEXT round.
     */

    static DecisionMaker5 dmn= new DecisionMaker5();

    public static BetConfig decideNextBet(List<Double> history) {

        // --- START OF YOUR CUSTOM LOGIC SPACE ---

        boolean shouldBet = false;     // Change to true/false based on your logic
        double betAmount = 10.0;       // Configurable bet amount
        double targetMultiplier = 100; // Configurable target redeem multiplier (e.g., 2.0x, 100.0x)

        if(history.size()>2)
            shouldBet=dmn.decisionMaker(history.get(history.size()-1));

        betAmount = dmn.getBetAmount();

        //targetMultiplier= dmn.getTargetMul();

        // --- END OF YOUR CUSTOM LOGIC SPACE ---
        return new BetConfig(shouldBet, betAmount, targetMultiplier);
    }

    // Helper class to hold your configurable bet settings
    public static class BetConfig {
        public final boolean shouldBet;
        public final double amount;
        public final double targetMultiplier;

        public BetConfig(boolean shouldBet, double amount, double targetMultiplier) {
            this.shouldBet = shouldBet;
            this.amount = amount;
            this.targetMultiplier = targetMultiplier;
        }
    }
}
