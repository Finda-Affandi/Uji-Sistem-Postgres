package com.ujisistempostgres.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class listToLowercase {
    public List<String> listLowercase(List<String> listUppercase) {
        List<String> lower = new ArrayList<>();

        for (String str : listUppercase){
            lower.add(str.toLowerCase());
        }

        Collections.sort(lower);

        return lower;
    }
}
