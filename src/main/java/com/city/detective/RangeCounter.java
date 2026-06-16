package com.city.detective;

import com.city.detective.model.DataModel;

import java.util.*;

public class RangeCounter {
    public static void main(String[] args) {

       Helper.dataLoader();

        List<Integer> numbers = new ArrayList<>();
        int counter =0;
        int total =0;

        for (Map.Entry<Integer, DataModel> entry : Helper.getDataMap().entrySet()) {
            //System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            total++;
            counter++;
            if(entry.getValue().getMultiplier()>100){
                numbers.add(counter);
                counter=0;
            }
        }

        // 2. Define your custom ranges here
        List<Range> ranges = Arrays.asList(
                new Range(1, 65),
                new Range(65, 80),
                new Range(80, 120),
                new Range(120, 350),
                new Range(350, 380),
                new Range(450, 600)
        );

        // 3. Initialize the counter map
        Map<Range, Integer> rangeCounts = new LinkedHashMap<>();
        for (Range r : ranges) {
            rangeCounts.put(r, 0);
        }
        int outOfRangeCount = 0;
        int totalNumbers = numbers.size();

        // 4. Count frequencies
        for (double num : numbers) {
            boolean matched = false;
            for (Range r : ranges) {
                if (r.contains(num)) {
                    rangeCounts.put(r, rangeCounts.get(r) + 1);
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                outOfRangeCount++;
            }
        }

        // 5. Print results
        System.out.println ("Total = "+total);
        System.out.printf("%-15s %-10s %-10s%n", "Range", "Count", "Percentage");
        System.out.println("---------------------------------------");

        for (Range r : ranges) {
            int count = rangeCounts.get(r);
            double percentage = (totalNumbers > 0) ? ((double) count / totalNumbers) * 100 : 0;
            System.out.printf("%-15s %-10d %.2f%%%n", r, count, percentage);
        }

        // Print numbers that didn't fit any range
        if (outOfRangeCount > 0) {
            double outPercentage = ((double) outOfRangeCount / totalNumbers) * 100;
            System.out.printf("%-15s %-10d %.2f%%%n", "Out of Range", outOfRangeCount, outPercentage);
        }
    }

    // Custom class to handle dynamic ranges
    static class Range {
        final double min;
        final double max;

        public Range(double min, double max) {
            this.min = min;
            this.max = max;
        }

        // Uses half-open interval [min, max) to avoid double counting
        public boolean contains(double num) {
            return num >= min && num < max;
        }

        @Override
        public String toString() {
            return min + " to " + max;
        }
    }
}
