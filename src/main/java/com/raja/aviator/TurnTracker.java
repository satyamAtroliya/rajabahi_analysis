package com.raja.aviator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TurnTracker {

    private double total100xCount = 0;
    private int totalTurnCount = 0;

    /**
     * Records a new turn number and returns the current luck/frequency score.
     * @param number The number rolled/generated this turn.
     * @return The calculated score (1.0 is average, >1.0 is unlucky/due, <1.0 is lucky).
     */
    public double addTurnAndGetScore(double number) {
        totalTurnCount++;

        if (number >= 100) {
            total100xCount++;
        }

        float expected100x = totalTurnCount/100f;

        double score = total100xCount-expected100x;

        return Math.round(score * 100.0) / 100.0;
    }

}
