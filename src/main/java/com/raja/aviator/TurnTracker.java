package com.raja.aviator;

public class TurnTracker {

    static int ivst =0;
    static int betAmount =10;

    public static void main(String arg[]){
        for(int i = 0; i<=500; i++) {


            if (ivst > 600) {
                // Calculates how many steps of 100 have passed beyond 1000
                int extraSteps = (int) ((ivst - 600) / 100);
                betAmount = 11 + extraSteps;
            } else {
                betAmount = 10;
            }
            if (ivst > 4000) {
                // Calculates how many steps of 100 have passed beyond 1000
                int extraSteps = (int) ((ivst - 4000) / 100);
                betAmount = 11 + extraSteps;
            }
            System.out.println(i+ "  betAmount : "+betAmount+" , IVST : "+ivst);
            ivst+=betAmount;
        }
    }

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
