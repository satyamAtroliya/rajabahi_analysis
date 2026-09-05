package com.raja.aviator.strategies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.raja.aviator.Constants.STRATEGY_200A;
import static com.raja.aviator.Constants.STRATEGY_200B;

public class Strategy200 implements Strategy {
    private static final Logger log = LoggerFactory.getLogger(Strategy200.class);

    private static final double HUNDRED = 100.0;

    private int betOffAfterOccurrence = Integer.MAX_VALUE;
    private boolean betButtonStatus = false;

    private int lastHundredBefore = 0;
    private int betOnCounter = 0;
    private int wonCount = 0;

    public boolean decisionMaker(double latestMultiplier) {
        boolean isHighMultiplier = latestMultiplier >= HUNDRED;
        int previousLastHundred = lastHundredBefore;

        // 1. Process Multiplier and Track Hits
        if (isHighMultiplier) {
            lastHundredBefore = 0;
            if (betButtonStatus) {
                wonCount++;
            }
        } else {
            lastHundredBefore++;
        }

        // 1. Start Betting Logic
        if (isHighMultiplier && !betButtonStatus && previousLastHundred >= 200 && previousLastHundred <= 402) {
            triggerBetOn(76);
            System.setProperty(STRATEGY_200A, STRATEGY_200A);
            log.warn(" ------------ 200 <= (LH) '{}' <= 402 ----- ", previousLastHundred);
            log.warn("------- STRATEGY_200A ------- BETS will be ON for next { 75 } Round");

        }
        //2. 84 lost before 1 win
        if (isHighMultiplier && !betButtonStatus && previousLastHundred >= 60 && previousLastHundred <= 85) {
            triggerBetOn(35);
            System.setProperty(STRATEGY_200B, STRATEGY_200B);
            log.warn(" ------------ 60 <= (LH) '{}' <= 85 ----- ", previousLastHundred);
            log.warn("------- STRATEGY_200B ------- BETS will ON for next { 35 } Round");
        }


        // 3. Process Active Betting Logic & Auto-Stop
        betOnCounter++;

        if (betOnCounter == betOffAfterOccurrence) {
            betButtonStatus = false;
            betOffAfterOccurrence = Integer.MAX_VALUE;
            wonCount = 0;

            if (Objects.equals(System.getProperty(STRATEGY_200A), STRATEGY_200A)) {
                System.setProperty(STRATEGY_200A, "");
                log.warn(" ------------ STRATEGY_200A ( 1 to 75 ) ------ TURNED OFF");
            }

            if (Objects.equals(System.getProperty(STRATEGY_200B), STRATEGY_200B)) {
                System.setProperty(STRATEGY_200B, "");
                log.warn(" ------------ STRATEGY_200B ( 1 to 35 ) ------ TURNED OFF");
            }

        }

        // 4. Adjust Future Durations and Early Stop Triggers based on Wins
        if (wonCount == 1) {
            betOffAfterOccurrence = 85;
            if (betOnCounter <= 20) betOnCounter = betOffAfterOccurrence - 1;
        } else if (wonCount == 2) {
            betOffAfterOccurrence = 135;
            if (betOnCounter <= 40) betOnCounter = betOffAfterOccurrence - 1;
        } else if (wonCount >= 3) {
            betOnCounter = betOffAfterOccurrence - 1;
        }

        return betButtonStatus;
    }

    private void triggerBetOn(int duration) {
        betButtonStatus = true;
        betOnCounter = 0;
        betOffAfterOccurrence = duration;
    }
}