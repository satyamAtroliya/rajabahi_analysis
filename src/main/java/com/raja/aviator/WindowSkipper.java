package com.raja.aviator;

import java.util.Random;

public class WindowSkipper {

    private boolean isSkipping = false;
    private int remainingInCurrentWindow = 0;
    private final Random random = new Random();

    /**
     * Determines whether the current method call should be skipped.
     * Synchronized to ensure thread-safety if called concurrently.
     *
     * @return true if the call should be skipped, false otherwise.

     */

    Random randomb = new Random();
    public synchronized boolean shouldSkip() {
        // If the current window has finished, generate a new one
        if (remainingInCurrentWindow <= 0) {
            // Toggle the state
            isSkipping = !isSkipping;

            if (isSkipping) {
                // Generate a skip window between 1 and 150
                remainingInCurrentWindow = random.nextInt(500) + 1;
            } else {
                // Generate an execute window between 1 and 351
                // (This averages ~176, which guarantees the 30% overall skip rate)
                remainingInCurrentWindow = random.nextInt(2500) + 1;
            }
        }

        // Consume one call from the current window
        remainingInCurrentWindow--;

        return isSkipping;
    }

    /**
     * Example method that you are calling 100,000 times.
     */
    public void targetMethod() {
        if (shouldSkip()) {
            return; // Exit early to simulate the skip
        }

        // Actual method logic goes here
    }

    // --- TEST SIMULATION ---
    public static void main(String[] args) {
        WindowSkipper skipper = new WindowSkipper();

        int totalCalls = 100_000;
        int skippedCalls = 0;
        int executedCalls = 0;

        for (int i = 0; i < totalCalls; i++) {
            if (skipper.shouldSkip()) {
                skippedCalls++;
            } else {
                executedCalls++;
            }
        }

        System.out.println("Total Calls: " + totalCalls);
        System.out.println("Skipped: " + skippedCalls + " (" + (skippedCalls * 100.0 / totalCalls) + "%)");
        System.out.println("Executed: " + executedCalls + " (" + (executedCalls * 100.0 / totalCalls) + "%)");
    }
}