package com.city.detective;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Data_filterTest {

    public static void main(String arg[]){
        List<String> result = new ArrayList<>();

        List<String> latest = List.of("B1", "A3", "A3" , "A2");

        result.addAll(latest);

        Set<String> seen = new HashSet<>(latest);


    }
}
