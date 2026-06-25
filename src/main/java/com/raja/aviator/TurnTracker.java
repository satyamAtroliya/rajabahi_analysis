package com.raja.aviator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TurnTracker {
    private final int windowSize = 900;
    private final int expectedCount = 9; // 1 per 100 turns
    private final Queue<Boolean> turnHistory = new LinkedList<>();
    private int current100xCount = 0;
    private int total100xCount = 0;

    private int totalTurnCount = 0;

    /**
     * Records a new turn number and returns the current luck/frequency score.
     * @param number The number rolled/generated this turn.
     * @return The calculated score (1.0 is average, >1.0 is unlucky/due, <1.0 is lucky).
     */
    public int addTurnAndGetScore(double number) {
        totalTurnCount++;
        boolean is100x = (number >= 100);
        total100xCount++;
        // Add new turn
        turnHistory.add(is100x);
        if (is100x) {
            current100xCount++;
            addInput(total100xCount);
            total100xCount=0;
        }

        // Remove oldest turn if window exceeds 600
        if (turnHistory.size() > windowSize) {
            totalTurnCount--;
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
        return (totalTurnCount /100) - current100xCount;
    }

    // Getter to see how many 100x hits are in the current window
    public int getCurrent100xCount() {
        return current100xCount;
    }

    private final ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
    private static final int MAX_SIZE = 8;

    public synchronized void addInput(int input) {
        queue.add(input);
        // Automatically evict the oldest item if limit is exceeded
        while (queue.size() > MAX_SIZE) {
            queue.poll();
        }
    }

    public int getWindowPlus(){
        int totalSum = queue.stream()
                .mapToInt(x -> x) // Lambda expression
                .sum();
        int window = ((totalSum/100)-(MAX_SIZE+2))*35;
        System.out.println(totalSum+"  "+totalSum/100+"  "+queue.size()+"   "+window);
        return Math.max(window,0);
    }
}
