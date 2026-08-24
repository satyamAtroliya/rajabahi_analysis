package com.raja.aviator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.raja.aviator.Constants.*;

public class DecisionMaker {

    private static final Logger log = LoggerFactory.getLogger(DecisionMaker.class);

    private Strategy200 strategy200 = new Strategy200();
    private StrategyTwoDigit strategyTwoDigit = new StrategyTwoDigit();
    private Strategy100 strategy100 = new Strategy100();
    private Strategy10 strategy10 = new Strategy10();
    private StrategyO10 strategyO10 = new StrategyO10();
    private Strategy150 strategy150 = new Strategy150();
    private StrategySentiment strategySS70 = new StrategySentiment();
    private Strategy1p85 strategy1p85 = new Strategy1p85();
    private Strategy1p75 strategy1p75 = new Strategy1p75();
    private Strategy10x strategy10x = new Strategy10x();
    private Strategy20x strategy20x = new Strategy20x();
    private Strategy30x strategy30x = new Strategy30x();
    private Strategy40x strategy40x = new Strategy40x();
    private Strategy50x strategy50x = new Strategy50x();
    private Strategy60x strategy60x = new Strategy60x();
    private Strategy70x strategy70x = new Strategy70x();
    private Strategy80x strategy80x = new Strategy80x();
    private Strategy90x strategy90x = new Strategy90x();

    private boolean betButtonStatus = false;
    private boolean betButtonStatus10 = false;
    private static final double HUNDRED = 100.0;
    private static final double TEN = 10.0;

    private static double target = 15.0;



    private double balance_profit = 0;
    private int allBet = 0;
    private double betAmount = 10.0;

    // Tracker for how many ticks/games have passed since the last 100x hit
    private int ticksSinceLastHundred = 0;

    int tracker_bal = 10000;

    public boolean decisionMaker(double latestMultiplier, String balance) {

        // this was pattern proven in 10 times to keep graph linier
        if (tracker_bal < 3000) {
            //   betAmount = 20;
        }
        if (tracker_bal > 7500) {
            betAmount = 10;
        }
        if (tracker_bal > 10000) {
            betAmount = 10;
            tracker_bal = 10000;
        }

        allBet++;
        // 1. Resolve the PREVIOUS round's bet based on the newly received multiplier
        if (betButtonStatus) {
            if (latestMultiplier >= target) {
                // If won, calculate balance by multiplying betAmount by 99
                double profit = betAmount * target;
                balance_profit += profit;
                balance_profit -= betAmount;;
                tracker_bal += profit;

                log.info(allBet + " 💰💰💰 WIN! Multiplier: {}x | Profit: +{} | New Balance: {}", latestMultiplier, profit, balance_profit);
                log.info("");
                // System.out.println(balance_profit);
            } else {
                // If lost, deduct the bet amount
                balance_profit -= betAmount;
                tracker_bal -= betAmount;
            }
        }

        if (betButtonStatus10) {
            if (latestMultiplier >= TEN) {
                // If won, calculate balance by multiplying betAmount by 99
                double profit = betAmount * 9;
                balance_profit += profit;
                tracker_bal += profit;

                log.info(allBet + " 💰💰💰 WIN! " + STRATEGYO10 + " Multiplier: {}x | Profit: +{} | New Balance: {}", latestMultiplier, profit, balance_profit);
                log.info("");
                //   System.out.println(balance_profit);
            } else {
                // If lost, deduct the bet amount
                balance_profit -= betAmount;
                tracker_bal -= betAmount;
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
        boolean isBettingSS70 = false;
        boolean isBetting1p75 = false;
        boolean isBetting1p85 = false;
        boolean isBetting10x = false;
        boolean isBetting20x = false;
        boolean isBetting30x = false;
        boolean isBetting40x = false;
        boolean isBetting50x = false;
        boolean isBetting60x = false;
        boolean isBetting70x = false;
        boolean isBetting80x = false;
        boolean isBetting90x = false;


       // isBetting10 = strategy10.decisionMaker(latestMultiplier);
          isBettingO10 = strategyO10.decisionMaker(latestMultiplier);
      //  isBetting100 = strategy100.decisionMaker(latestMultiplier);
      //  isBetting150 = strategy150.decisionMaker(latestMultiplier);
      //  isBetting200 = strategy200.decisionMaker(latestMultiplier);
      //  isBettingTD = strategyTwoDigit.decisionMaker(latestMultiplier);
      //  isBettingSS70 = strategySS70.decisionMaker(latestMultiplier);
      //  isBetting1p75 = strategy1p75.decisionMaker(latestMultiplier);
      //  isBetting1p85 = strategy1p85.decisionMaker(latestMultiplier);
//          isBetting10x = strategy10x.decisionMaker(latestMultiplier);
 //         isBetting20x = strategy20x.decisionMaker(latestMultiplier);
 //         isBetting30x = strategy30x.decisionMaker(latestMultiplier);
  //        isBetting40x = strategy40x.decisionMaker(latestMultiplier);
     //     isBetting50x = strategy50x.decisionMaker(latestMultiplier);
 //         isBetting60x = strategy60x.decisionMaker(latestMultiplier);
//          isBetting70x = strategy70x.decisionMaker(latestMultiplier);
//          isBetting80x = strategy80x.decisionMaker(latestMultiplier);// W62B7=(T83)->P17790
//          isBetting90x = strategy90x.decisionMaker(latestMultiplier);


        // Variables to determine next state
        boolean nextBetStatus = false;
        boolean nextBetStatus10 = false;

        if (isBetting10 || isBetting100 || isBetting150 || isBetting200 || isBettingTD || isBettingSS70 || isBetting1p75 || isBetting1p85) {
            nextBetStatus = true;
        }

        if(isBetting10x || isBetting20x || isBetting30x || isBetting40x || isBetting50x || isBetting60x || isBetting70x || isBetting80x || isBetting90x)
            nextBetStatus = true;


        List<String> as = new ArrayList<>();
        if (isBetting10) as.add(STRATEGY_10);
        if (isBetting100) as.add(STRATEGY_100);
        if (isBetting150) as.add(STRATEGY_150);
        if (isBetting200) as.add(STRATEGY_200A);
        if (isBettingTD) as.add(STRATEGY_TD_A1);
        if (isBettingO10) as.add(STRATEGYO10);
        if (isBettingSS70) as.add(STRATEGY_SS_70);
        if (isBetting1p75) as.add(STRATEGY1P75);
        if (isBetting1p85) as.add(STRATEGY1P85);

        if(isBetting10x) target=15;
        if(isBetting20x) target=23;
        if(isBetting30x) target=33;
        if(isBetting40x) target=43;
        if(isBetting50x) target=63;// will keep active
        if(isBetting60x) target=63;//think about it
        if(isBetting70x) target=73;
        if(isBetting80x) target=83;
        if(isBetting90x) target=93;

        // 4. Highlight significant state changes (Turning ON or OFF)
        if (!betButtonStatus && nextBetStatus) {
            //  log.info("🟢🟢🟢 BETS TURNED ON! Triggered by: {} | Amount: {} | Ticks since last 100x: {}", activeStrategy, betAmount, ticksSinceLastHundred);
        } else if (betButtonStatus && !nextBetStatus) {
            //log.info("🔴🔴🔴 BETS TURNED OFF! Ticks since last 100x: {}", ticksSinceLastHundred);
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

        log.info(allBet + " 📊 Tick:  {}  | Strategy:  {}  |  Balance:  {}  | Last 100x ago  {}  | Bet is  {}  | Profit:  {}",
                tick, as, balance, ticksSinceLastHundred, statusString, balance_profit);

        // System property update
        System.setProperty("BET_BTN_STATUS", statusString);

        // Save current bet status for the next tick
        betButtonStatus = nextBetStatus;
        betButtonStatus10 = nextBetStatus10;
        System.setProperty("BET_AMOUNT", String.valueOf(betAmount));

        return betButtonStatus;
    }
}