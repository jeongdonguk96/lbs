package io.spring.test.util;

import java.util.*;

public class ServiceUtil {

    static long startTime;
    static long endTime;
    static long timeDiff;
    static double transactionTime;


    public static Set<String[]> listToSet(List<String> keyList, List<Object> valueList) {
        Set<String[]> btsSet = new HashSet<>();

        for (int i = 0; i < keyList.size(); i++) {
            String[] pair = new String[2];
            pair[0] = keyList.get(0);
            pair[1] = (String) valueList.get(0);
            btsSet.add(pair);
        }

        return btsSet;
    }

    public static List<String[]> processUserData(List<String[]> readFile, Set<Object> btsData) {
        Map<String, String> btsMap = new HashMap<>();


        return null;
    }
}
