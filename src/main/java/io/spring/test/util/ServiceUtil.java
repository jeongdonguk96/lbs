package io.spring.test.util;

import io.spring.test.sampleEntity.RedisGeoDummy;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class ServiceUtil {

    static long startTime;
    static long endTime;
    static long timeDiff;
    static double transactionTime;


    // redis에서 조회한 value를 List 객체로 변환한다.
    public static List<RedisGeoDummy> stringToDummyList(List<Object> valueList) {
        startTime = System.currentTimeMillis();
        List<RedisGeoDummy> redisGeoDummyList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        valueList.forEach(value -> {
            try {
                JSONObject json = (JSONObject) parser.parse(String.valueOf(value));
                long longId = (long) json.get("id");
                int id = (int) longId;
                long longNameCode = (long) json.get("nameCode");
                int nameCode = (int) longNameCode;
                double latitude = (double) json.get("latitude");
                double longitude = (double) json.get("longitude");
                RedisGeoDummy dummy = new RedisGeoDummy(id, nameCode, latitude, longitude);
                redisGeoDummyList.add(dummy);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("dataList size = " + redisGeoDummyList.size());
        System.out.println("redisGeoDummyList 1 = " + redisGeoDummyList.get(0));
        System.out.println("stringToDummy() DONE");
        System.out.println("========== JAVA LIST INSTANCE TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println();

        return redisGeoDummyList;
    }

    // redis에서 조회한 value를 Map 객체로 변환한다.
    public static Map<Long, Double[]> stringToDummyMap(List<Object> valueList) {
        startTime = System.currentTimeMillis();
        Map<Long, Double[]> map = new HashMap<>();
        JSONParser parser = new JSONParser();

        valueList.forEach(value -> {
            try {
                JSONObject json = (JSONObject) parser.parse(String.valueOf(value));
                long nameCode = (long) json.get("nameCode");
                double latitude = (double) json.get("latitude");
                double longitude = (double) json.get("longitude");

                map.put(nameCode, new Double[]{latitude, longitude});
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("stringToDummy() DONE");
        System.out.println("========== JAVA MAP INSTANCE TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println();

        return map;
    }

    // 사용자와 기지국 데이터를 매핑한다.
    public static List<String[]> mappingData(List<String[]> readFile, Map<Long, Double[]> coordinatesMap) {
        startTime = System.currentTimeMillis();

        readFile.forEach(file -> {
            String nameCodeToCompare = file[2];
            if (coordinatesMap.containsKey(Long.valueOf(nameCodeToCompare))) {
                Double[] coordinates = coordinatesMap.get(Long.valueOf(nameCodeToCompare));
                file[3] = String.valueOf(coordinates[0]);
                file[4] = String.valueOf(coordinates[1]);
            }
        });

        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;

        System.out.println("file 1 = " + Arrays.toString(readFile.get(0)));
        System.out.println("file 999999 = " + Arrays.toString(readFile.get(999998)));
        System.out.println("file size = " + readFile.size());
        System.out.println("mappingData() DONE");
        System.out.println("========== LAST MAPPING TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println();

        return readFile;
    }

}
