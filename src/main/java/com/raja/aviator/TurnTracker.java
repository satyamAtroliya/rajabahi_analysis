package com.raja.aviator;

public class TurnTracker {

    static int ivst =0;
    static int betAmount =10;

    public static void main(String arg[]){
        for(int i = 0; i<=600; i++) {
            if (ivst <= 100) {
                betAmount = 10;

            } else if (ivst <= 7000) {
                // Increase by ₹1 for every ₹100 invested beyond ₹600.
                betAmount = 11 + (int) ((ivst - 100) / 100);
            } else {
                // Start at ₹45 to avoid dropping the bet at ₹4000.
                // Each subsequent ₹1 increase requires more investment.
                double extraInvestment = ivst - 7000;
                betAmount = 45 + (int) Math.sqrt(extraInvestment / 100.0);
            }
            System.out.println(i+ "  betAmount : "+betAmount+" , IVST : "+ivst +" P/L  : "+((betAmount*100)-ivst) );
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
