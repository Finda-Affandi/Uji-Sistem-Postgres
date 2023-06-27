package com.ujisistempostgres.compareter;

import java.util.List;
import java.util.Objects;

public class CompareList {
    public static boolean compareLists(List<String> list1, List<String> list2) {
        if (list1 == list2) {
            return true; // Lists refer to the same object
        }

        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false; // Lists have different sizes or at least one is null
        }

        for (int i = 0; i < list1.size(); i++) {
            String element1 = list1.get(i);
            String element2 = list2.get(i);

            if (!Objects.equals(element1, element2)) {
                return false; // Different elements at index i
            }
        }

        return true; // Lists have the same elements in the same order
    }
}
