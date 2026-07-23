package com.raja.aviator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecisionMaker {

    private static final Logger log = LoggerFactory.getLogger(DecisionMaker.class);

    private Stretegy200 stretegy200 = new Stretegy200();
    private StretegyTwoDigit stretegyTwoDigit = new StretegyTwoDigit();
    private Stretegy100 stretegy100 = new Stretegy100();
    private Stretegy10 stretegy10 = new Stretegy10();

    private boolean betButtonStatus = false;
    private static final double HUNDRED = 100.0;

    private double balance_profit = 0;
    private double betAmount = 10.0;

    // Tracker for how many ticks/games have passed since the last 100x hit
    private int ticksSinceLastHundred = 0;
    double heigest = 0.0;

    public boolean decisionMaker(double latestMultiplier, String balance) {
        heigest = Math.max(heigest,balance_profit);
        // 1. Resolve the PREVIOUS round's bet based on the newly received multiplier
        if (betButtonStatus) {
            if (latestMultiplier >= HUNDRED) {
                // If won, calculate balance by multiplying betAmount by 99
                double profit = betAmount * 99;
                balance_profit += profit;
            //    log.info("");
                log.info("Lose from Last: {}", heigest-balance_profit);
                log.info("💰💰💰 WIN! Multiplier: {}x | Profit: +{} | New Balance: {}", latestMultiplier, profit, balance_profit);
              //  log.info("");
            } else {
                // If lost, deduct the bet amount
                balance_profit -= betAmount;
            }
        }

        // 2. Update the tracker for the last 100x multiplier
        if (latestMultiplier >= HUNDRED) {
            ticksSinceLastHundred = 0;
        } else {
            ticksSinceLastHundred++;
        }

        // 3. Consult strategies for the NEXT round
        boolean isDm3Betting = stretegy200.decisionMaker(latestMultiplier); // 22k profit in 8.5k bets
        boolean isDm4Betting = stretegyTwoDigit.decisionMaker(latestMultiplier);// More profitable 30k in 3k bets
        boolean isDm5Betting = stretegy100.decisionMaker(latestMultiplier);// More profitable 30k in 3k bets
        boolean isDm6Betting = stretegy10.decisionMaker(latestMultiplier);// More profitable 30k in 3k bets

        // Variables to determine next state
        boolean nextBetStatus = false;
        String activeStrategy = "None";

        // Logic to determine bet status, amount, and which strategy triggered it
        if (isDm3Betting && isDm4Betting) {
            betAmount = 10;
            nextBetStatus = true;
            activeStrategy = "BOTH Strategy200 & "+System.getProperty("STRATEGY");
        } else if (isDm3Betting || isDm5Betting || isDm6Betting || isDm4Betting) {
            betAmount = 10;
            nextBetStatus = true;
            activeStrategy = "Strategy200";
        } else if (isDm4Betting) {
            betAmount = 10;
            nextBetStatus = true;
            activeStrategy = System.getProperty("STRATEGY");
        } else {
            betAmount = 10;
            nextBetStatus = false;
        }

        // 4. Highlight significant state changes (Turning ON or OFF)
        if (!betButtonStatus && nextBetStatus) {
         // log.info("");
        //    log.info("🟢🟢🟢 BETS TURNED ON! Triggered by: {} | Amount: {} | Ticks since last 100x: {}", activeStrategy, betAmount, ticksSinceLastHundred);
           // log.info("");
        } else if (betButtonStatus && !nextBetStatus) {
            //log.info("");
          //  log.info("🔴🔴🔴 BETS TURNED OFF! Ticks since last 100x: {}", ticksSinceLastHundred);
            //log.info("");
        }

        // 5. Standard tick logging for every method call
        String statusString = nextBetStatus ? "ON" : "OFF";
        String tick= String.valueOf(latestMultiplier);
        switch (tick.length()) {
            case 3:
                tick = tick + "x  ";
                break;
            case 4:
                tick = tick + "x ";
                break;
            case 5:
                tick = tick + "x";
                break;
            default:
                tick = tick + "x   ";
                break;
        }


    //    log.info("📊 Tick:  {}  | Strategy:  {}  |  Balance:  {}  | Last 100x ago  {}  | Bet is  {}  | Profit:  {}",
      //          tick, activeStrategy, balance ,ticksSinceLastHundred, statusString, balance_profit);

        // System property update
        System.setProperty("BET_BTN_STATUS", statusString);

        // Save current bet status for the next tick
        betButtonStatus = nextBetStatus;

        System.setProperty("BET_AMOUNT",String.valueOf(betAmount));
        return betButtonStatus;
    }
}