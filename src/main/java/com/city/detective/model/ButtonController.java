package com.city.detective.model;


public class ButtonController {

    private boolean autoBetOn;
    private boolean autoBetOff;

    public ButtonController(boolean autoBetOn, boolean autoBetOff) {
        this.autoBetOn = autoBetOn;
        this.autoBetOff = autoBetOff;
    }

    public boolean isAutoBetOn() {
        return autoBetOn;
    }

    public void setAutoBetOn(boolean autoBetOn) {
        this.autoBetOn = autoBetOn;
    }

    public boolean isAutoBetOff() {
        return autoBetOff;
    }

    public void setAutoBetOff(boolean autoBetOff) {
        this.autoBetOff = autoBetOff;
    }
}
