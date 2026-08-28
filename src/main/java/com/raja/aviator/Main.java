package com.raja.aviator;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Save as Main.java
public class Main {


    public static void main(String[] args) {
        // Initialize the calculator with a starting balance of Rs. 5000
        String FILE_SOURCE = "./dataSource/";

        List<String> names = getFileNames(FILE_SOURCE);
        for(String name : names){
            String testFilePath = FILE_SOURCE+name;
          //  AviatorCalculator calculator = new AviatorCalculator(10000);
           //calculator.processSimulation(testFilePath);
        }

        // Individual Testing
        AviatorCalculator calculator = new AviatorCalculator(10000);
         //calculator.processSimulation(FILE_SOURCE+"mon_19_jun_CO.txt");
        //calculator.processSimulation("C:/WorkSpace/multipliers/daily/daily_239.txt");
       //  calculator.processSimulation("C:/WorkSpace/multipliers/yearly/year_2026.txt");
         //calculator.processSimulation("C:/WorkSpace/multipliers/monthly/monthly_6.txt");
       //  calculator.processSimulation("C:/WorkSpace/multipliers/weekly/weekly_35.txt");

        LocalDateTime from = LocalDateTime.of(2026, 8, 16, 0, 0, 0);
        LocalDateTime to = LocalDateTime.of(2026, 8, 16, 23, 59, 59);
        calculator.processSimulationDataFromDB(from, to);

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