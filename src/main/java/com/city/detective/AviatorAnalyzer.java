package com.city.detective;

import com.city.detective.model.DataModel;

import java.util.*;

public class AviatorAnalyzer {

    private static final double TARGET = 100.0;

    public static void main(String[] args) {
        List<Double> multipliers = new ArrayList<>();

        Helper.dataLoader();
        for (Map.Entry<Integer, DataModel> entry : Helper.getDataMap().entrySet()) {
            multipliers.add(entry.getValue().getMultiplier());

        }


        List<Integer> gaps = new ArrayList<>();

        int last100xIndex = -1;

        for (int i = 0; i < multipliers.size(); i++) {

            if (multipliers.get(i) >= TARGET) {

                if (last100xIndex != -1) {
                    gaps.add(i - last100xIndex);
                }

                last100xIndex = i;
            }
        }

        System.out.println("============== SUMMARY ==============");
        System.out.println("Total Rounds       : " + multipliers.size());
        System.out.println("100x Hits          : " + (gaps.size() + 1));

        if (gaps.isEmpty()) {
            System.out.println("No sufficient 100x data.");
            return;
        }

        printGapStatistics(gaps);
        printDistribution(gaps);
        printWindowProbabilities(gaps);
        printBettingAnalysis(gaps, 10);
    }

    private static void printGapStatistics(List<Integer> gaps) {

        IntSummaryStatistics stats =
                gaps.stream().mapToInt(Integer::intValue).summaryStatistics();

        List<Integer> sorted = new ArrayList<>(gaps);
        Collections.sort(sorted);

        double median;

        int n = sorted.size();

        if (n % 2 == 0) {
            median = (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
        } else {
            median = sorted.get(n / 2);
        }

        System.out.println("\n========== GAP STATS ==========");
        System.out.printf("Average Gap : %.2f%n", stats.getAverage());
        System.out.println("Median Gap  : " + median);
        System.out.println("Min Gap     : " + stats.getMin());
        System.out.println("Max Gap     : " + stats.getMax());
    }

    private static void printDistribution(List<Integer> gaps) {

        int[] ranges = {50, 100, 200, 300, 500};

        Map<String, Integer> counts = new LinkedHashMap<>();

        for (int gap : gaps) {

            boolean assigned = false;
            int previous = 1;

            for (int range : ranges) {

                if (gap <= range) {

                    String key = previous + "-" + range;
                    counts.merge(key, 1, Integer::sum);

                    assigned = true;
                    break;
                }

                previous = range + 1;
            }

            if (!assigned) {
                counts.merge(">500", 1, Integer::sum);
            }
        }

        System.out.println("\n========== DISTRIBUTION ==========");

        int total = gaps.size();

        counts.forEach((range, count) -> {
            double pct = count * 100.0 / total;

            System.out.printf(
                    "%-10s  %5d  %7.2f%%%n",
                    range,
                    count,
                    pct
            );
        });
    }

    private static void printWindowProbabilities(List<Integer> gaps) {

        int[] windows = {10, 25, 50, 100, 150, 200};

        System.out.println("\n====== PROBABILITY OF 100x ======");

        for (int window : windows) {

            long count =
                    gaps.stream()
                            .filter(g -> g <= window)
                            .count();

            double probability =
                    count * 100.0 / gaps.size();

            System.out.printf(
                    "Within %-3d rounds : %.2f%%%n",
                    window,
                    probability
            );
        }
    }

    private static void printBettingAnalysis(
            List<Integer> gaps,
            double betPerRound) {

        int[] windows = {25, 50, 100, 150, 200};

        System.out.println("\n====== BETTING ANALYSIS ======");

        for (int window : windows) {

            long wins =
                    gaps.stream()
                            .filter(g -> g <= window)
                            .count();

            double successRate =
                    wins * 1.0 / gaps.size();

            double cost = window * betPerRound;

            double profitIfWin =
                    100 * betPerRound - cost;

            double expectedValue =
                    successRate * profitIfWin +
                            (1 - successRate) * (-cost);

            System.out.printf(
                    "Window=%3d  Success=%6.2f%%  EV=%8.2f%n",
                    window,
                    successRate * 100,
                    expectedValue
            );
        }
    }
}