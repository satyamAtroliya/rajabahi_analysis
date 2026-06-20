package com.city.detective.dataOrganizer;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataOrganizer {

    public static void main(String[] args) {
        // Change these to your actual folder and file paths
        String inputFolderPath = "inputfiles";
        String masterOutputPath = "mon_20_Jun_co.txt";

        try {
            mergeAllFiles(inputFolderPath, masterOutputPath);
            System.out.println("All files merged successfully into: " + masterOutputPath);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void mergeAllFiles(String folderPath, String outputPathStr) throws IOException {
        Path inputDir = Paths.get(folderPath);
        Path outputPath = Paths.get(outputPathStr);

        // Create output directories if they do not exist
        if (outputPath.getParent() != null) {
            Files.createDirectories(outputPath.getParent());
        }

        // Open the master output file for writing
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath);
             Stream<Path> paths = Files.walk(inputDir)) {

            // Find and process all text files
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    // Avoid reading the output file if it sits in the same folder
                    .filter(path -> !path.toAbsolutePath().equals(outputPath.toAbsolutePath()))
                    .forEach(file -> {
                        System.out.println("Merging: " + file.getFileName());

                        // Read each file line by line and stream it directly to the master writer
                        try (BufferedReader reader = Files.newBufferedReader(file)) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                writer.write(line);
                                writer.newLine();
                            }
                        } catch (IOException e) {
                            System.err.println("Could not read file " + file.getFileName() + ": " + e.getMessage());
                        }
                    });
        }
    }
}
