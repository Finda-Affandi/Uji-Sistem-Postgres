package com.ujisistempostgres.compareter;

import java.util.List;
import java.util.Objects;

public class CompareList {
    public static boolean compareLists(List<String> list1, List<String> list2) {
        if (list1 == list2) {
            return true;
        }

        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }

        for (int i = 0; i < list1.size(); i++) {
            String element1 = list1.get(i);
            String element2 = list2.get(i);

            if (!Objects.equals(element1, element2)) {
                return false;
            }
        }

        return true;
    }
}
