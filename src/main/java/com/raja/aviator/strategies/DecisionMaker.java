package com.raja.aviator.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

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
    private int habc = 10;

    // Tracker for how many ticks/games have passed since the last 100x hit
    private int ticksSinceLastHundred = 0;
    double tracker_bal = 0;
    double tracker_bal_l = 0;
    int active_bets_count = 0;
    double invested = 0;
    double invested_real = 0;
    double hinvested = 0;
    int STOP = 0; // 1 looking to stop after a win, 2 STOPPED

    int win_in_150 = 0;
    int yet_pending = 0;
    double pAndL = 0;

    public boolean decisionMaker(double latestMultiplier, String balance) {

        balance_profit = Double.parseDouble(System.getProperty(DUMMY_BALANCE, "10"));
        invested = Double.parseDouble(System.getProperty(INVESTED, "0"));
        yet_pending = Integer.parseInt(System.getProperty(YET_PENDING, "0"));

        double ivst = invested;
        tracker_bal_l=Math.min(tracker_bal,tracker_bal_l);

        betAmount = 11 + (int) ((ivst - 100) / 100);
        int bd = (int) (betAmount / 4);

        STOP = Integer.parseInt(System.getProperty(MANUAL_STOP, "0")) == 1 ? 1 : STOP;
        if (STOP == 2) {
            System.out.println("........STOPPED After a win......");
            return false;
        }

        allBet++;
        // 1. Resolve the PREVIOUS round's bet based on the newly received multiplier
        if (invested > 7000) {
            if (betButtonStatus) {
                betAmount=bd;
                calculate(latestMultiplier, 10, true);
                calculate(latestMultiplier, 20, true);
               // calculate(latestMultiplier, 30, false);
               // calculate(latestMultiplier, 40, false);
               // calculate(latestMultiplier, 70, false);
                //calculate(latestMultiplier, 80, false);
                calculate(latestMultiplier, 90, false);
            }

            if (betButtonStatus) {
                target = 100;
                betAmount = bd;
                if (latestMultiplier >= target) {
                    // If won, calculate balance by multiplying betAmount by 99
                    habc = Math.max(habc, active_bets_count);
                    hinvested = Math.max(hinvested, invested);
                    double profit = (betAmount * target) - betAmount;
                    balance_profit += profit;
                    tracker_bal += profit;
                    pAndL = (profit - invested);
                    if (active_bets_count < 150)
                        win_in_150++;
                    else win_in_150 = 0;
                    if (active_bets_count < 200 && active_bets_count > 110)
                        yet_pending++;

                    if (STOP == 1)
                        STOP = 2;
                    //if(invested>20000)
                    log.error(allBet + " 💰💰💰 WIN! Multiplier: {}x | Profit: +{} | New Balance: {} | {} | {}", latestMultiplier, profit, balance_profit, hinvested, tracker_bal);
                    log.info("");
                    // System.out.println(balance_profit);
                    active_bets_count = 0;
                    invested = 1000;
                    tracker_bal = 0;
                } else {
                    // If lost, deduct the bet amount
                    balance_profit -= betAmount;
                    tracker_bal -= betAmount;
                    invested += betAmount;
                }
            }
        } else {

            if (betButtonStatus) {
                target = 100;
                if (latestMultiplier >= target) {
                    // If won, calculate balance by multiplying betAmount by 99
                    habc = Math.max(habc, active_bets_count);
                    hinvested = Math.max(hinvested, invested);
                    double profit = (betAmount * target) - betAmount;
                    balance_profit += profit;
                    tracker_bal += profit;
                    pAndL = (profit - invested);
                    if (active_bets_count < 150)
                        win_in_150++;
                    else win_in_150 = 0;
                    if (active_bets_count < 200 && active_bets_count > 110)
                        yet_pending++;

                    if (STOP == 1)
                        STOP = 2;
                    //if(invested>20000)
                    log.error(allBet + " 💰💰💰 WIN! Multiplier: {}x | Profit: +{} | New Balance: {} | {} | {}", latestMultiplier, profit, balance_profit, hinvested, tracker_bal_l);
                    log.info("");
                    // System.out.println(balance_profit);
                    active_bets_count = 0;
                    invested = pop();
                    tracker_bal = 0;
                } else {
                    // If lost, deduct the bet amount
                    balance_profit -= betAmount;
                    tracker_bal -= betAmount;
                    invested += betAmount;
                }
            }

        }
        if (betButtonStatus10) {
            if (latestMultiplier >= FIFTEEN) {
                // If won, calculate balance by multiplying betAmount by 99
                double profit = (betAmount * FIFTEEN) - betAmount;
                balance_profit += profit;
                tracker_bal += profit;
                log.info(allBet + " 💰💰💰 WIN! " + STRATEGYO10 + " Multiplier: {}x | Profit: +{} | New Balance: {} | {}", latestMultiplier, profit, balance_profit, tracker_bal);
                log.info("");
                //   System.out.println(balance_profit);
                tracker_bal = 0;
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


        isBetting1p75 = strategy1p75.decisionMaker(latestMultiplier);
        isBettingGapTap = strategyGapTap.decisionMaker(latestMultiplier);
        // isBetting10 = strategy10.decisionMaker(latestMultiplier);
        isBetting150 = strategy150.decisionMaker(latestMultiplier);
        isBetting30x = strategy30x.decisionMaker(latestMultiplier);
        isBetting50x = strategy50x.decisionMaker(latestMultiplier);
        isBetting60x = strategy60x.decisionMaker(latestMultiplier);
// - If bellow is commented max abc is 480

        isBettingTD = strategyTwoDigit.decisionMaker(latestMultiplier);
        isBetting100 = strategy100.decisionMaker(latestMultiplier);
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

        log.info(allBet + " 📊 Tick: {} | Strategy: {} | Balance: {} | L 100x ago {} | Bet is {} | Profit: {} | POD: {} | BetAmount: {} | ABC: {} | IBAW: {}",
                tick, as, balance, ticksSinceLastHundred, statusString, balance_profit, tracker_bal, betAmount, active_bets_count, ivst);

        // System property update
        System.setProperty("BET_BTN_STATUS", statusString);
        System.setProperty(DUMMY_BALANCE, String.valueOf(balance_profit));
        System.setProperty(INVESTED, String.valueOf(invested));
        System.setProperty(YET_PENDING, String.valueOf(yet_pending));

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


    private final Queue<Integer> queue = new PriorityQueue<>();

    private int nextThreshold = 20000;
    private int pushedd = 0;
    private int maxpushedd = 0;

    public int process(int number) {

        if (number < nextThreshold) {
            return -1;
        }

        // Store one part in the queue
        queue.offer(3000);
        queue.offer(3000);
        queue.offer(3000);
        queue.offer(3000);
        queue.offer(3000);
        queue.offer(3000);
        queue.offer(2000);
        //  System.out.println(" PUSHED : " + part);
        pushedd = pushedd + 20000;
        System.out.println(" PUSHED : Total : " + pushedd);
        // Return the other part if no smaller value exists;
        // otherwise return the lowest queued value.
        int result = pop();
        System.out.println(" POP : remaing : " + pushedd);
        maxpushedd = Math.max(pushedd, maxpushedd);
        System.out.println(" MAX PUSHED PUSHED : Total : " + maxpushedd);

        return result;
    }

    public int pop() {
        Integer value = queue.poll();
        if (value == null)
            nextThreshold = 20000;
        else {
            pushedd = pushedd - 3000;
            System.out.println("POPPED : remaining " + pushedd);
        }
        return value != null ? value : -1;
    }

    public void calculate(double latestMultiplier, double increasingTarget ,boolean addCutProfit ){
        if (latestMultiplier >= increasingTarget) {
            // If won, calculate balance by multiplying betAmount by 99
            habc = Math.max(habc, active_bets_count);
            hinvested = Math.max(hinvested, invested);
            double profit = (betAmount * increasingTarget) - betAmount;
            balance_profit += profit;
            tracker_bal += profit;

            log.error(allBet + " 💰💰💰 WIN! Multiplier: {}x | Profit: +{} | New Balance: {} | {} | {} ", latestMultiplier, profit, balance_profit, hinvested, tracker_bal_l);
            log.info("");
            // System.out.println(balance_profit);
            active_bets_count = 0;
            if(addCutProfit)
            {invested -= profit;}
        } else {
            // If lost, deduct the bet amount
            balance_profit -= betAmount;
            tracker_bal -= betAmount;
            invested += betAmount;
        }
    }

}