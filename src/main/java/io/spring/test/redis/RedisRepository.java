package io.spring.test.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.test.entity.Dummy;
import io.spring.test.sampleEntity.RedisGeoDummy;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<byte[], byte[]> redisTemplate2;
    private final RedisTemplate<String, Dummy> redisTemplate3;
    private final RedisTemplate<String, Object> redisTemplate4;
    private final ObjectMapper objectMapper;

    long startTime;
    long endTime;
    long timeDiff;
    double transactionTime;

    // redis에 데이터를 저장한다.
    public void insert(Dummy dummy) throws JsonProcessingException {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String jsonString = objectMapper.writeValueAsString(dummy);
        valueOperations.set(String.valueOf(dummy.getId()), jsonString);
    }

    // redis에 데이터를 벌크로 저장한다.
    public void insertMany(List<RedisGeoDummy> dummyList) {
        RedisSerializer keySerializer = redisTemplate4.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplate4.getValueSerializer();

        redisTemplate4.executePipelined((RedisCallback<?>) connection -> {
            for (RedisGeoDummy dummy : dummyList) {
                String key = String.valueOf(dummy.getId());
                String value = null;
                try {
                    value = objectMapper.writeValueAsString(dummy);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                connection.set(keySerializer.serialize(key), valueSerializer.serialize(value));
            }

            return null;
        });
    }

    // redis에서 데이터를 키로 조회한다.
    public void find(String id) {
        ValueOperations<String, Object> valueOperations = redisTemplate4.opsForValue();
        startTime = System.currentTimeMillis();
        String result = (String) valueOperations.get(id);
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("id = " + id + ", ========== REDIS TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("redis result = " + result);
        System.out.println();
    }

    // redis에서 전체 데이터를 조회한다.
    public List<Object> findAll() {
        ValueOperations<String, Object> valueOperations = redisTemplate4.opsForValue();

        startTime = System.currentTimeMillis();
        Set<String> keys = redisTemplate4.keys("*");
        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== REDIS TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("REDIS SELECT SIZE = " + Objects.requireNonNull(keys).size());
        System.out.println("data = " + valueOperations.get("1"));
        System.out.println();

        startTime = System.currentTimeMillis();
        List<String> keyList = new ArrayList<>(keys);
        List<Object> valueList = Objects.requireNonNull(redisTemplate4.opsForValue().multiGet(keyList)).stream()
                .filter(Objects::nonNull)
                .toList();
        System.out.println("key 1 = " + keyList.get(0) + ", value = " + valueList.get(0));
        System.out.println("key 999999 = " + keyList.get(999998) + ", value = " + valueList.get(999998));
        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== MAPPING TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("dataList size = " + valueList.size());
        System.out.println();

        return valueList;
    }

    // redis에서 전체 데이터의 키를 조회한다.
    public List<String> findAllRedisKey() {
        startTime = System.currentTimeMillis();
        Set<String> keys = redisTemplate4.keys("*");
        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== REDIS TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("REDIS SELECT SIZE = " + Objects.requireNonNull(keys).size());
        System.out.println();

        return new ArrayList<>(Objects.requireNonNull(keys));
    }

    // redis에서 키로 전체 데이터의 밸류를 조회한다.
    public List<Object> findAllRedisValue(List<String> keyList) {
        startTime = System.currentTimeMillis();
        List<Object> valueList = Objects.requireNonNull(redisTemplate4.opsForValue().multiGet(keyList)).stream()
                .filter(Objects::nonNull)
                .toList();
        System.out.println("key 1 = " + keyList.get(0) + ", value = " + valueList.get(0));
        System.out.println("key 2 = " + keyList.get(999998) + ", value = " + valueList.get(999998));
        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== MAPPING TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("dataList size = " + valueList.size());
        System.out.println();

        return valueList;
    }

    // redis에서 조회한 value를 객체로 변환한다.
    public List<RedisGeoDummy> stringToDummy(List<Object> valueList) throws ParseException {
        startTime = System.currentTimeMillis();
        List<RedisGeoDummy> redisGeoDummyList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        for (Object value : valueList) {
            JSONObject json = (JSONObject) parser.parse(String.valueOf(value));
            long longId = (long) json.get("id");
            int id = (int) longId;
            long longNameCode = (long) json.get("nameCode");
            int nameCode = (int) longNameCode;
            double latitude = (double) json.get("latitude");
            double longitude = (double) json.get("longitude");
            RedisGeoDummy dummy = new RedisGeoDummy(id, nameCode, latitude, longitude);
            redisGeoDummyList.add(dummy);
        }

        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== JAVA INSTANCE TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("dataList size = " + redisGeoDummyList.size());
        System.out.println("redisGeoDummyList 1 = " + redisGeoDummyList.get(0));
        System.out.println();

        return redisGeoDummyList;
    }


}
