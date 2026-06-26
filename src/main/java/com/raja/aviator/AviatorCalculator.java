package com.raja.aviator;

// Save as AviatorCalculator.java
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class AviatorCalculator {

    private double balance;
    AtomicInteger totalBet = new AtomicInteger(0);
    AtomicInteger totalWonBet = new AtomicInteger(0);
    private final List<Double> history = new ArrayList<>();

    public AviatorCalculator(double initialBalance) {
        this.balance = initialBalance;
    }

    public void processSimulation(String filePath) {
        System.out.println("=== Starting Aviator Simulation ===");
        System.out.println("Initial Balance: Rs. " + balance);
        System.out.println("Reading source file: " + filePath + "\n-----------------------------------");

        try (Scanner scanner = new Scanner(new File(filePath))) {
            int round = 1;
            AtomicInteger LastHundredBefore = new AtomicInteger(0);

                while (scanner.hasNext()) {
                    String token = scanner.next();

                    // Remove the 'x' or 'X' from the end of the number
                    String cleanedToken = token.replace("x", "").replace(",", "");
                    double actualMultiplier;
                    try {
                        actualMultiplier = Double.parseDouble(cleanedToken);
                    } catch (NumberFormatException e) {
                        System.out.println("[WARN] Skipping invalid number: " + cleanedToken);
                        continue;
                    }
                    LastHundredBefore.getAndIncrement();
                    if(actualMultiplier>=100)
                        LastHundredBefore.set(0);
                //System.out.println(String.format("[Round %d] Actual Crash Point: %.2fx", round, actualMultiplier));
                    synchronized (this) {
                        // Fetch your custom configuration from the strategy class
                        BettingStrategy.BetConfig config = BettingStrategy.decideNextBet(history);

                        if (config.shouldBet) {
                            totalBet.getAndIncrement();
                            if (balance < config.amount) {
                                // System.out.println(String.format("  [SKIPPED] Insufficient balance! Required: Rs. %.2f, Available: Rs. %.2f", config.amount, balance));
                            } else {
                                //   System.out.println(String.format("  [BET PLACED] Amount: Rs. %.2f | Target Redeem: %.2fx", config.amount, config.targetMultiplier));

                                // Deduct bet amount upfront
                                balance -= config.amount;

                                // Aviator logic: If actual crash point is >= your target, you successfully cashed out
                                if (actualMultiplier >= config.targetMultiplier) {
                                    double winnings = config.amount * config.targetMultiplier;
                                    balance += winnings;
                                   // System.out.println(String.format("  %d 🎉 WON! Multiplier %.2fx. Won: Rs. %.2f | last100xBefore : %d | New Balance: Rs. %.2f", round, actualMultiplier, winnings, LastHundredBefore.get(), balance));
                                    totalWonBet.getAndIncrement();
                                } else {
                                   // System.out.println(String.format("  %d ❌ LOST! Crashed at %.2fx before reaching %.2fx | last100xBefore : %d | New Balance: Rs. %.2f", round, actualMultiplier, config.targetMultiplier, LastHundredBefore.get(), balance));
                                }
                            }
                        } else {
                            //System.out.println("  [NO BET] Strategy decided to skip this round.");
                           // System.out.println(String.format("  %d 🌎 Actual Crash Point: %.2fx                      | last100xBefore : %d |", round, actualMultiplier, LastHundredBefore.get()));
                        }

                        // Add the current number to history so your strategy can analyze it for the next round
                        history.add(actualMultiplier);
                        round++;
                    }
                //System.out.println("-----------------------------------");
            }

        } catch (IOException e) {
            System.err.println("Error reading the test file: " + e.getMessage());
        }

        System.out.println("Final Win Balance: Rs. " + (balance - 10000)+" | TotalBet : "+ totalBet+" | total Won Bet : "+totalWonBet);
        float successRate = (float) (totalWonBet.get() * 100) /totalBet.get();

        System.out.println(  "Success rate : " +successRate+" , Expected Losses before a win  "+ 1/(successRate/100));
        System.out.println("=== Simulation Ended ===");
    }
}
