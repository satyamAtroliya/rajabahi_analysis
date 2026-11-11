package com.city.detective.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ButtonCtr {
    private String scriptId;
    private int betAmount;
    private int target;
    private boolean autoBetOn;
    private boolean autoBetOff;
}
