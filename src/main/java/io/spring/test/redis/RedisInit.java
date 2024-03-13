package io.spring.test.redis;

import io.spring.test.sampleEntity.RedisGeoDummy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class RedisInit {

    private final RedisRepository redisRepository;

    long startTime;
    long endTime;
    long timeDiff;
    double transactionTime;

//    @Bean
    public void insert() {
        startTime = System.currentTimeMillis();
        System.out.println("========== INSERT START ==========");
        List<RedisGeoDummy> dummyList = new ArrayList<>();
        Random random = new Random();

        double baseLatitude = 37.0;
        double baseLongitude = 126.0;

        for (int i = 1; i <= 1000000; i++) {
            RedisGeoDummy dummy = new RedisGeoDummy();
            dummy.setId(i);
            dummy.setNameCode(i);
            double latitude = baseLatitude + random.nextDouble(9) + 10;
            double longitude = baseLongitude + random.nextDouble(9) + 10;
            dummy.setLatitude(latitude);
            dummy.setLongitude(longitude);
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
