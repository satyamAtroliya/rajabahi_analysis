package com.raja.aviator;
import java.util.LinkedList;
import java.util.Queue;

public class TurnTracker {
    private final int windowSize = 700;
    private final double expectedCount = 7.0; // 1 per 100 turns
    private final Queue<Boolean> turnHistory = new LinkedList<>();
    private int current100xCount = 0;

    /**
     * Records a new turn number and returns the current luck/frequency score.
     * @param number The number rolled/generated this turn.
     * @return The calculated score (1.0 is average, >1.0 is unlucky/due, <1.0 is lucky).
     */
    public double addTurnAndGetScore(double number) {
        boolean is100x = (number >= 100);

        // Add new turn
        turnHistory.add(is100x);
        if (is100x) {
            current100xCount++;
        }

        // Remove oldest turn if window exceeds 600
        if (turnHistory.size() > windowSize) {
            boolean removedTurn = turnHistory.poll();
            if (removedTurn) {
                current100xCount--;
            }
        }

        // Prevent division by zero if no 100x numbers have appeared yet
        if (current100xCount == 0) {
            return expectedCount;
        }

        // Score = Expected (6) / Actual
        return expectedCount / current100xCount;
    }

    // Getter to see how many 100x hits are in the current window
    public int getCurrent100xCount() {
        return current100xCount;
    }
}
