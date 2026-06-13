package com.city.detective.dataOrganizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CleanTextNumbers {
    public static void main(String[] args) {
        // Define file paths
        String inputFilePath = "dataSource/all_new.txt";
        String outputFilePath = "outputforExcel.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;

            // Process the text file line by line
            while ((line = reader.readLine()) != null) {
                // Split the line by any whitespace (spaces, tabs)
                String[] tokens = line.trim().split("\\s+");

                for (String token : tokens) {
                    if (!token.isEmpty()) {
                        // Remove the trailing 'x' or 'X' if it exists
                        String cleanedNumber = token.replaceAll("[xX]$", "");
if(Double.parseDouble(cleanedNumber)>=200)
    cleanedNumber="200";
                        // Write the cleaned number and move to a new line
                        writer.write(cleanedNumber);
                        writer.newLine();
                    }
                }
            }

            System.out.println("Output file generated successfully!");

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }
}
