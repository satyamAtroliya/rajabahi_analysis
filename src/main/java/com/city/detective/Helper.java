package com.city.detective;

import com.city.detective.model.DataModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Helper {

    private static final String filePath = "./dataSource/mon_13_Jun_co.txt";

    public static Map<Integer, DataModel> getDataMap() {
        return dataMap;
    }

    static Map<Integer, DataModel> dataMap = new HashMap<>();
    static int totalRecords = 0;

    public static void dataLoader() {

        List<Double> doubleList = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Read the file token by token (space-separated)
            int key = 0;
            while (scanner.hasNext()) {
                String token = scanner.next();

                // Remove the 'x' or 'X' from the end of the number
                String cleanedToken = token.replace("x", "").replace(",", "");

                // Convert to double and add to the dynamic list
                doubleList.add(Double.parseDouble(cleanedToken));
            }
            totalRecords = doubleList.size();
            // Reverse the data to make to same receiving in the game

            //put data in HashMap
            for (double fa : doubleList) {
                dataMap.put(key++, new DataModel(fa,false,false));
            }

            dataMap.forEach((key_p, value) -> {
         //      System.out.println(key_p + " : " + value.getMultiplier());
            });
            System.out.println("==============done=================");
        } catch (FileNotFoundException e) {
            System.err.println("Error: The file could not be found at " + filePath);
        }
    }

    public static double getResultedMultiplier(int latestIndex ) {
        return dataMap.get(latestIndex).getMultiplier();
    }


    public static void showData(int index){
        System.out.println( index+" -> Multiplier : " + getDataMap().get(index).getMultiplier() + " , BetOn : " + getDataMap().get(index).isBetOn() + " , won : " + getDataMap().get(index).isWon());

    }

    public static boolean checkLastDecisionWasSuccess(int latestIndex , double isGreaterThen) {
        return dataMap.get(latestIndex).getMultiplier() >= isGreaterThen;
    }

    static DecisionMakerTest dm = new DecisionMakerTest();


    public static boolean getDecision(int index) {

       return dm.decisionMaker(dataMap.get(index).getMultiplier());

        /*
        if (index <= 10) return false;

        double[] decideOn = getLastTenMultiplier(index);

       // System.out.println(Arrays.toString(decideOn));

        if(decideOn[0] < 2f && decideOn[1] < 2f && decideOn[2] < 2f && decideOn[3] < 2f && decideOn[4] < 2f && decideOn[5] < 2f)
        { return true; }

        return true;

         */
    }

    public static double[] getLastTenMultiplier(int index) {
        // Leaving here actual desire multiplier (which suppose to come in next occurrence)
        index = index - 1;
        int arr_index = 10;
        double[] decisionData = new double[11];
        for (int i = index - 10; i <= index; i++) {
            decisionData[arr_index--] = dataMap.get(i).getMultiplier();
        }
        return decisionData;
    }

    public static boolean hundredsTrackerDecider(int tracker100x) {
        if(tracker100x<25)return true;
        return false;
       }
}
