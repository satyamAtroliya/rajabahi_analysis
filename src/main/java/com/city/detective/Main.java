package com.city.detective;

import com.city.detective.model.DataModel;

import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static int accountBalance = 1000;

    public static void main(String[] args) {

        Helper.dataLoader();
        //Statistics.statistics();

        for (int index = 0; index < Helper.totalRecords; index++) {

            //Helper.showData(index);
            boolean decision = Helper.getDecision(index);

            if (decision) {
                Helper.getDataMap().get(index).setBetOn(true);
            }

            if (decision && Helper.checkLastDecisionWasSuccess(index, 100)) {
                Helper.getDataMap().get(index).setWon(true);
            }

        }
        int totalBet = 0;
        int wonBet = 0;
        int loseBet = 0;

        int hundredsGap = 0;

        for (Map.Entry<Integer, DataModel> entry : Helper.getDataMap().entrySet()) {

            if(entry.getValue().isBetOn() && entry.getValue().isWon()){
                accountBalance = accountBalance + 990;
                totalBet++;
                wonBet++;
            }

            if(entry.getValue().isBetOn() && ! entry.getValue().isWon()){
                accountBalance = accountBalance - 10;
                totalBet++;
                loseBet++;
            }

             hundredsGap++;
            if(entry.getValue().getMultiplier()>100) {
                 System.out.println("======================================================hundread Gap = "+ hundredsGap);
                hundredsGap = 0;
           //   System.out.println(" TotalBet = " + totalBet + " , WonBet = " + wonBet + ", LoseBet = " + loseBet + " , Balance =" + accountBalance);

            }
            System.out.println(entry.getKey() + " -> Multiplier : " + entry.getValue().getMultiplier() + " , BetOn : " + entry.getValue().isBetOn() + " , won : " + entry.getValue().isWon()+" --- FromLast100x : "+hundredsGap+" "+" TotalBet = " + totalBet + " , WonBet = " + wonBet + ", LoseBet = " + loseBet + " , Balance =" + accountBalance);


            }
    }


}

