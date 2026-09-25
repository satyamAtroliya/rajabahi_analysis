package com.raja.aviator.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
    private Strategy30x strategy30x = new Strategy30x();
    private Strategy50x strategy50x = new Strategy50x();
    private Strategy60x strategy60x = new Strategy60x();
    private StrategyGapTap strategyGapTap = new StrategyGapTap();
    private Strategy300 strategy300 = new Strategy300();
    private StrategySentiment2 strategySS2 = new StrategySentiment2();

    private boolean betButtonStatus = false;
    private boolean betButtonStatus10 = false;
    private static final double HUNDRED = 100.0;
    private static final double FIFTEEN = 15.0;
    private static double target = 100.0;

    private double balance_profit = 0;
    private int allBet = 0;
    private double betAmount = 10.0;

    // Tracker for how many ticks/games have passed since the last 100x hit
    private int ticksSinceLastHundred = 0;
    double tracker_bal = 0;
    int active_bets_count = 0;
    double invested = 0;
    int STOP = 0; // 1 looking to stop after a win, 2 STOPPED
    boolean skip_flag = false;
    int skip_count = 0;

    int c_won = 0;
    Deque<Double> stack = new ArrayDeque<>();

    double h_ivst=0;
    int h_abc=0;

    public boolean decisionMaker(double latestMultiplier, String balance) {

        balance_profit = Double.parseDouble(System.getProperty(DUMMY_BALANCE, "10"));
        invested = Double.parseDouble(System.getProperty(INVESTED, "0"));

        // Skip section
        if (active_bets_count == 200 || active_bets_count == 400 && !skip_flag) {
            skip_flag = true;
            active_bets_count++;
        }
        if (skip_flag) {
            skip_count++;
            if (skip_count == 150) {
                skip_flag = false;
                skip_count = 0;
            }
            //   return false;
        }

        // Investment control
        double ivst = invested;

        if (active_bets_count/50 == 0) {
           stack.push(ivst);
         //  if(ivst>70000)
            System.out.println("push : " + ivst);
            ivst = 0;
           // active_bets_count=0;
        }
        if(c_won<=1 && !stack.isEmpty())
        { ivst=stack.pop();
         //   if(ivst>70000)
            System.out.println("pop : " +ivst);
        }

        if (ivst <= 100) {
            betAmount = 10;

        } else if (ivst <= 7000000) {
            // Increase by ₹1 for every ₹100 invested beyond ₹600.
            betAmount = 11 + (int) ((ivst - 100) / 100);
        } else {
            // Start at ₹45 to avoid dropping the bet at ₹4000.
            // Each subsequent ₹1 increase requires more investment.
            double extraInvestment = ivst - 7000;
            betAmount = 45 + (int) Math.sqrt(extraInvestment / 100.0);
        }

        STOP = Integer.parseInt(System.getProperty(MANUAL_STOP, "0")) == 1 ? 1 : STOP;
        if (STOP == 2) {
            System.out.println("........STOPPED After a win......");
            return false;
        }

        allBet++;
        // 1. Resolve the PREVIOUS round's bet based on the newly received multiplier
        if (betButtonStatus) {
            if (latestMultiplier >= target) {
                // If won, calculate balance by multiplying betAmount by 99
                double profit = (betAmount * target) - betAmount;
                balance_profit += profit;
                tracker_bal += profit;

                if (active_bets_count < 100)
                    c_won++;
                else c_won = 0;

                active_bets_count = 0;
                if (STOP == 1)
                    STOP = 2;
                invested = 0;
                log.info(allBet + " 💰💰💰 WIN! Multiplier: {}x | Profit: +{} | New Balance: {}", latestMultiplier, profit, balance_profit);
                log.info("");
                // System.out.println(balance_profit);
            } else {
                // If lost, deduct the bet amount
                balance_profit -= betAmount;
                tracker_bal -= betAmount;
                invested += betAmount;
            }
        }

        if (betButtonStatus10) {
            if (latestMultiplier >= FIFTEEN) {
                // If won, calculate balance by multiplying betAmount by 99
                double profit = (betAmount * FIFTEEN) - betAmount;
                balance_profit += profit;
                tracker_bal += profit;
                log.info(allBet + " 💰💰💰 WIN! " + STRATEGYO10 + " Multiplier: {}x | Profit: +{} | New Balance: {}", latestMultiplier, profit, balance_profit);
                log.info("");
                //   System.out.println(balance_profit);
            } else {
                // If lost, deduct the bet amount
                balance_profit -= betAmount;
                tracker_bal -= betAmount;
                invested += betAmount;
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
        boolean isBetting30x = false;
        boolean isBetting50x = false;
        boolean isBetting60x = false;
        boolean isBettingGapTap = false;
        boolean isBetting300 = false;
        boolean isBettingSS2 = false;


        // isBettingO10 = strategyO10.decisionMaker(latestMultiplier); // Lose making
        //isBettingSS70 = strategySS70.decisionMaker(latestMultiplier); // Not that efficient, Bets to profit ratio is low
        //isBetting1p85 = strategy1p85.decisionMaker(latestMultiplier); // Not that efficient, Bets to profit ratio is low
        //isBetting200 = strategy200.decisionMaker(latestMultiplier); // Not that efficient, Bets to profit ratio is low

        isBetting10 = strategy10.decisionMaker(latestMultiplier);
        isBetting100 = strategy100.decisionMaker(latestMultiplier);
        isBetting150 = strategy150.decisionMaker(latestMultiplier);
        isBettingTD = strategyTwoDigit.decisionMaker(latestMultiplier);
        isBetting1p75 = strategy1p75.decisionMaker(latestMultiplier);
        isBetting30x = strategy30x.decisionMaker(latestMultiplier);
        isBetting50x = strategy50x.decisionMaker(latestMultiplier);
        isBetting60x = strategy60x.decisionMaker(latestMultiplier);
        isBettingGapTap = strategyGapTap.decisionMaker(latestMultiplier);
        isBetting300 = strategy300.decisionMaker(latestMultiplier);
        isBettingSS2 = strategySS2.decisionMaker(latestMultiplier);


        // Variables to determine next state
        boolean nextBetStatus = false;
        boolean nextBetStatus10 = false;

        if (isBetting30x || isBetting50x || isBetting60x) {
            nextBetStatus = true;
        }

        if (isBettingO10) target = 15;
        if (isBetting30x) target = 80; //Final
        if (isBetting50x) target = 62;// will keep active
        if (isBetting60x) target = 90;//think about it

        if (isBetting10 || isBetting100 || isBetting150 || isBetting200 || isBettingTD || isBettingSS70 ||
                isBetting1p75 || isBettingGapTap || isBetting1p85 || isBetting300 || isBettingSS2) {
            nextBetStatus = true;
            target = 100;
        }

        if (isBettingO10) {
            nextBetStatus10 = true;
        }

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
        if (isBetting30x) as.add(STRATEGY30x);
        if (isBetting50x) as.add(STRATEGY50x);
        if (isBetting60x) as.add(STRATEGY60x);
        if (isBettingGapTap) as.add(STRATEGYGAPTAP);
        if (isBetting300) as.add(STRATEGY300);
        if (isBettingSS2) as.add(STRATEGY_SS2);


        // 4. Highlight significant state changes (Turning ON or OFF)
        if (!betButtonStatus && nextBetStatus) {
            //  log.info("🟢🟢🟢 BETS TURNED ON! Triggered by: {} | Amount: {} | Ticks since last 100x: {}", activeStrategy, betAmount, ticksSinceLastHundred);
        } else if (betButtonStatus && !nextBetStatus) {
            //log.info("🔴🔴🔴 BETS TURNED OFF! Ticks since last 100x: {}", ticksSinceLastHundred);
        }

        // 5. Standard tick logging for every method call
        String statusString = nextBetStatus || nextBetStatus10 ? "ON" : "OFF";
        if (statusString.equals("ON")) active_bets_count++;

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
        h_ivst=Math.max(ivst,h_ivst);
        h_abc=Math.max(active_bets_count,h_abc);
        log.info(allBet + " 📊 Tick: {} | Strategy: {} | Balance: {} | L 100x ago {} | Bet is {} | Profit: {} | POD: {} | BetAmount: {} | ABC: {} | IBAW: {}",
                tick, as, balance, ticksSinceLastHundred, statusString, balance_profit, tracker_bal, betAmount, h_abc, h_ivst);

        // System property update
        System.setProperty("BET_BTN_STATUS", statusString);
        System.setProperty(DUMMY_BALANCE, String.valueOf(balance_profit));
        System.setProperty(INVESTED, String.valueOf(invested));

        // Save current bet status for the next tick
        // Save current bet status for the next tick
        if (ticksSinceLastHundred < 185 || isBettingGapTap) {
            betButtonStatus = nextBetStatus;
            betButtonStatus10 = nextBetStatus10;
        } else {
            betButtonStatus = false;
            betButtonStatus10 = false;
        }

        System.setProperty("BET_AMOUNT", String.valueOf(betAmount));

        return betButtonStatus;
    }
}