package com.raja.aviator;

public class TurnTracker {

    static int ivst = 0;
    static int betAmount = 10;
//abc max till today 480.

    public static void main(String[] args) {
        long ivst = 0;
        long tivst = 0;
        int magicNumber = 35;
        final long maxInvestment = 20000;

        System.out.println("   Magic Number === " + magicNumber);
        int betAmount = 10;
        for (int i = 1; i <= 700; i++) {

            betAmount = 16 + (int) ((ivst - 100) / 100);
            if(ivst>5000) {ivst=2500; }

            tivst=tivst+betAmount;
            ivst += betAmount;

            // 100x payout includes the current bet.
            long profitLossIfWin = betAmount * 100L - ivst;

            System.out.println(
                    i
                            + " betAmount: " + betAmount
                            + ", IVST: " + ivst
                            + ", P/L if win: " + profitLossIfWin
                            + ", total invested: " + tivst
            );
        }
    }

    private double total100xCount = 0;
    private int totalTurnCount = 0;

    /**
     * Records a new turn number and returns the current luck/frequency score.
     *
     * @param number The number rolled/generated this turn.
     * @return The calculated score (1.0 is average, >1.0 is unlucky/due, <1.0 is lucky).
     */
    public double addTurnAndGetScore(double number) {
        totalTurnCount++;

        if (number >= 100) {
            total100xCount++;
        }

        float expected100x = totalTurnCount / 100f;

        double score = total100xCount - expected100x;

        return Math.round(score * 100.0) / 100.0;
    }

}
