package io.spring.test.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.test.entity.Dummy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Dummy> redisTemplate2;
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
    public void insertMany(List<Dummy> dummyList) {
        redisTemplate2.opsForList().rightPushAll("dummyList", dummyList);
    }

    // redis에서 데이터를 키로 조회한다.
    public void find(String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        startTime = System.currentTimeMillis();
        String result = valueOperations.get(id);
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("id = " + id + ", ========== REDIS TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("redis result = " + result);
        System.out.println();
    }

}
