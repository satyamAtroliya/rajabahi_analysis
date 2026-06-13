package com.city.detective.dataOrganizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LineReverser {
    public static void main(String[] args) {
        // Define paths for the input file and the target output file
        String inputFilePath = "dataSource/mon_1_Jun.txt";
        String outputFilePath = "mon_1_Jun_co.txt";

        // Try-with-resources handles closing files automatically
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;

            // Read the file line by line until reaching the end (null)
            while ((line = reader.readLine()) != null) {

                String arr[] = line.split("  ");
                StringBuilder reversedLine = new StringBuilder();

                for(int i= arr.length-1; i>=0;i--){
                    reversedLine.append(arr[i]+"  ");
                }

                // Write the reversed line to the new file
                writer.write(reversedLine.toString());

                // Add a new line separator to maintain line-by-line formatting
                writer.newLine();
            }

            System.out.println("Success! Each line has been reversed and saved to: " + outputFilePath);

        } catch (IOException e) {
            System.err.println("An error occurred during file processing: " + e.getMessage());
        }
    }
}
