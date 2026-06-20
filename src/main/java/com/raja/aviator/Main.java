package com.raja.aviator;

// Save as Main.java
public class Main {
    public static void main(String[] args) {
        // Initialize the calculator with a starting balance of Rs. 5000
        AviatorCalculator calculator = new AviatorCalculator(10000);

        // Path to your test file containing the source numbers
        String testFilePath = "./dataSource/mon_3_Jun_co.txt";

        calculator.processSimulation(testFilePath);
    }
}
