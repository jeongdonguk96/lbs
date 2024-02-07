package io.spring.test.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.spring.test.entity.Dummy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class RedisInit {

    private final RedisRepository redisRepository;

//    @Bean
    public void insert() throws JsonProcessingException {
        System.out.println("========== INSERT START ==========");
        Dummy dummy = new Dummy();
        Random random = new Random();

        for (int i = 1; i <= 1000000; i++) {
            dummy.setId(i);
            dummy.setNameCode(random.nextInt(90000) + 10000);
            dummy.setAddressCode(random.nextInt(90000) + 10000);
            dummy.setUseYn("Y");
            dummy.setCreate_dt();
            redisRepository.insert(dummy);
        }

        System.out.println("========== INSERT END ==========");
    }

}
