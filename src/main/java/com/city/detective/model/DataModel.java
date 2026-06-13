package com.city.detective.model;

public class DataModel {

    double multiplier;
    boolean betOn;
    boolean won;

    public DataModel(double multiplier, boolean betOn, boolean won) {
        this.multiplier = multiplier;
        this.betOn = betOn;
        this.won = won;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public boolean isBetOn() {
        return betOn;
    }

    public void setBetOn(boolean betOn) {
        this.betOn = betOn;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }
}
