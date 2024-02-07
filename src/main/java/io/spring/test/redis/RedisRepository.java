package io.spring.test.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.test.entity.Dummy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    long startTime;
    long endTime;
    long timeDiff;
    double transactionTime;

    public void insert(Dummy dummy) throws JsonProcessingException {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String jsonString = objectMapper.writeValueAsString(dummy);
        valueOperations.set(String.valueOf(dummy.getId()), jsonString);
    }

    public void find(String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        startTime = System.currentTimeMillis();
        String result = valueOperations.get(id);
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("redis result = " + result);
    }

}
