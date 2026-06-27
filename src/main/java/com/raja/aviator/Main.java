package com.raja.aviator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Save as Main.java
public class Main {
    public static void main(String[] args) {
        // Initialize the calculator with a starting balance of Rs. 5000


        /* Path to your test file containing the source numbers
        String path = "./dataSource/";
        List<String> names = getFileNames(path);
        for(String name : names){
            String testFilePath = "./dataSource/"+name;
            AviatorCalculator calculator = new AviatorCalculator(10000);
            calculator.processSimulation(testFilePath);
        }
*/
       // Individual Testing
        AviatorCalculator calculator = new AviatorCalculator(10000);
       calculator.processSimulation("./dataSource/all_new.txt");


    }


    public static List<String> getFileNames(String folderPath) {
        List<String> fileNames = new ArrayList<>();
        File folder = new File(folderPath);

        // Verify the folder exists and is a valid directory
        if (folder.exists() && folder.isDirectory()) {
            File[] listOfFiles = folder.listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    // Only add names if it's a file, skipping subdirectories
                    if (file.isFile()) {
                        fileNames.add(file.getName());
                    }
                }
            }
        }
        return fileNames;
    }

}
