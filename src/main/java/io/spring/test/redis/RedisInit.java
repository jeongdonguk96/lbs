package io.spring.test.redis;

import io.spring.test.entity.Dummy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class RedisInit {

    private final RedisRepository redisRepository;

    long startTime;
    long endTime;
    long timeDiff;
    double transactionTime;

    @Bean
    public void insert() {
        startTime = System.currentTimeMillis();
        List<Dummy> dummyList = new ArrayList<>();
        Dummy dummy = new Dummy();
        System.out.println("========== INSERT START ==========");
        Random random = new Random();

        for (int i = 1; i <= 1000000; i++) {
            dummy.setId(i);
            dummy.setNameCode(random.nextInt(90000) + 10000);
            dummy.setAddressCode(random.nextInt(90000) + 10000);
            dummy.setUseYn("Y");
            dummy.setCreate_dt();
            dummyList.add(dummy);
        }

        redisRepository.insertMany(dummyList);

        endTime = System.currentTimeMillis();
        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== REDIS TRX TIME = { " + transactionTime + "}s ==========");

        System.out.println("========== INSERT END ==========");
    }

}
