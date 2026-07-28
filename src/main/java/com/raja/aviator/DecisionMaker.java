package com.raja.aviator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.raja.aviator.Constants.*;

public class DecisionMaker {

    private static final Logger log = LoggerFactory.getLogger(DecisionMaker.class);

    private Strategy200 strategy200 = new Strategy200();
    private StrategyTwoDigit strategyTwoDigit = new StrategyTwoDigit();
    private Strategy100 strategy100 = new Strategy100();
    private Strategy10 strategy10 = new Strategy10();
    private StrategyO10 strategyO10 = new StrategyO10();
    private Strategy150 strategy150 = new Strategy150();

    private boolean betButtonStatus = false;
    private boolean betButtonStatus10 = false;
    private static final double HUNDRED = 100.0;
    private static final double TEN = 10.0;

    private double balance_profit = 0;
    private int allBet = 0;
    private double betAmount = 10.0;
    private double betAmountFor10 = 100.0;

    // Tracker for how many ticks/games have passed since the last 100x hit
    private int ticksSinceLastHundred = 0;

    public boolean decisionMaker(double latestMultiplier, String balance) {
        allBet++;
        // 1. Resolve the PREVIOUS round's bet based on the newly received multiplier
        if (betButtonStatus) {
            if (latestMultiplier >= HUNDRED) {
                // If won, calculate balance by multiplying betAmount by 99
                double profit = betAmount * 99;
                balance_profit += profit;

                log.info(allBet + " 💰💰💰 WIN! Multiplier: {}x | Profit: +{} | New Balance: {}", latestMultiplier, profit, balance_profit);
                log.info("");
            } else {
                // If lost, deduct the bet amount
                balance_profit -= betAmount;
            }
        }

        if (betButtonStatus10) {
            if (latestMultiplier >= TEN) {
                // If won, calculate balance by multiplying betAmount by 99
                double profit = betAmountFor10 * 9;
                balance_profit += profit;

                log.info(allBet + " 💰💰💰 WIN! "+STRATEGYO10+" Multiplier: {}x | Profit: +{} | New Balance: {}", latestMultiplier, profit, balance_profit);
                log.info("");
            } else {
                // If lost, deduct the bet amount
                balance_profit -= betAmountFor10;
            }
        }

        // 2. Update the tracker for the last 100x multiplier
        if (latestMultiplier >= HUNDRED) {
            ticksSinceLastHundred = 0;
        } else {
            ticksSinceLastHundred++;
        }

        // 3. Consult strategies for the NEXT round

        boolean isBetting10 = false;
        boolean isBettingO10 = false;
        boolean isBetting100 = false;
        boolean isBetting150 = false;
        boolean isBetting200 = false;
        boolean isBettingTD = false;


        isBetting10 = strategy10.decisionMaker(latestMultiplier);
       // isBettingO10 = strategyO10.decisionMaker(latestMultiplier);
        isBetting100 = strategy100.decisionMaker(latestMultiplier);
        isBetting150 = strategy150.decisionMaker(latestMultiplier);
        isBetting200 = strategy200.decisionMaker(latestMultiplier);
      isBettingTD = strategyTwoDigit.decisionMaker(latestMultiplier);


        // Variables to determine next state
        boolean nextBetStatus = false;
        boolean nextBetStatus10 = false;
        String activeStrategy = "";

        if (isBetting10 || isBetting100 || isBetting150 || isBetting200 || isBettingTD) {
            betAmount = 10;
            nextBetStatus = true;
        } else {
            activeStrategy = "None";
        }

        if (!isBetting10 && !isBetting100 && !isBetting150 && !isBetting200 && !isBettingTD && !isBettingO10) {
            activeStrategy = "None";
        }

        /*
        activeStrategy = String.format("%s %s %s %s %s %s %s %s",
                System.getProperty(STRATEGY_10, ""),
                System.getProperty(STRATEGY_100, ""),
                System.getProperty(STRATEGY_150, ""),
                System.getProperty(STRATEGY_200A, ""),
                System.getProperty(STRATEGY_200B, ""),
                System.getProperty(STRATEGY_TD_A1, ""),
                System.getProperty(STRATEGY_TD_B1, ""),
                System.getProperty(STRATEGYO10, "")
        );*/

        if (isBetting10) {
            activeStrategy = System.getProperty(STRATEGY_10, "");
        }
        if (isBetting100) {
            activeStrategy = activeStrategy + " " + System.getProperty(STRATEGY_100, "");
        }
        if (isBetting150) {
            activeStrategy = activeStrategy + " " + System.getProperty(STRATEGY_150, "");
        }
        if (isBetting200) {
            activeStrategy = activeStrategy + " " + System.getProperty(STRATEGY_200A, "") + " " + System.getProperty(STRATEGY_200B, "");
        }
        if (isBettingTD) {
            activeStrategy = activeStrategy + " " + System.getProperty(STRATEGY_TD_A1, "") + " " + System.getProperty(STRATEGY_TD_B1, "");
        }
        if (isBettingO10) {
            activeStrategy = activeStrategy + " " + System.getProperty(STRATEGYO10, "");
            nextBetStatus10=true;
        }

        // 4. Highlight significant state changes (Turning ON or OFF)
        if (!betButtonStatus && nextBetStatus) {
            log.info("🟢🟢🟢 BETS TURNED ON! Triggered by: {} | Amount: {} | Ticks since last 100x: {}", activeStrategy, betAmount, ticksSinceLastHundred);
        } else if (betButtonStatus && !nextBetStatus) {
            log.info("🔴🔴🔴 BETS TURNED OFF! Ticks since last 100x: {}", ticksSinceLastHundred);
        }

        // 5. Standard tick logging for every method call
        String statusString = nextBetStatus ? "ON" : "OFF";
        String tick = String.valueOf(latestMultiplier);
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

     //   log.info(allBet + " 📊 Tick:  {}  | Strategy:  {}  |  Balance:  {}  | Last 100x ago  {}  | Bet is  {}  | Profit:  {}",
       //         tick, activeStrategy, balance, ticksSinceLastHundred, statusString, balance_profit);

        // System property update
        System.setProperty("BET_BTN_STATUS", statusString);

        // Save current bet status for the next tick
        betButtonStatus = nextBetStatus;
        betButtonStatus10 = nextBetStatus10;
        System.setProperty("BET_AMOUNT", String.valueOf(betAmount));

        return betButtonStatus;
    }
}