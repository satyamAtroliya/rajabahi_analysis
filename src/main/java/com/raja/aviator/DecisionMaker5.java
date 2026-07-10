package com.raja.aviator;

import java.util.ArrayList;
import java.util.List;

public class DecisionMaker5 {

    public enum State {
        DM3_ACTIVE,
        DM4_ACTIVE,
        WAITING
    }

    private State state = State.WAITING;

    DecisionMakerStart3 dm3 = new DecisionMakerStart3();
    DecisionMakerStart4 dm4 = new DecisionMakerStart4();

    boolean btn3 = false;
    boolean btn4 = false;

    double betAmount =10;

    public double getamt(){
        return betAmount;
    }

    public boolean decisionMaker(double lm){

        btn3 = dm3.decisionMaker(lm);
        btn4 = dm4.decisionMaker(lm);

        if(btn3 && btn4)
        {
            betAmount = 20;
            return true;
        }
        else if(btn3||btn4)
        { return true; }

        if(!btn3 && !btn4)
        {betAmount=10;
            return false;
        }

        return false;
    }
}