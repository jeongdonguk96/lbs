package io.spring.test.mysql;

import io.spring.test.entity.Dummy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class MysqlInit {

    private final MysqlRepository mysqlRepository;

//    @Bean
    public void insert() {
        System.out.println("========== INSERT START ==========");
        Random random = new Random();
        List<Dummy> dummies = new ArrayList<>();

        for (int i = 1000000; i <= 1005218; i++) {
            Dummy dummy = new Dummy();
            dummy.setId(i);
            dummy.setNameCode(random.nextInt(90000) + 10000);
            dummy.setAddressCode(random.nextInt(90000) + 10000);
            dummy.setUseYn("Y");
            dummy.setCreate_dt();

            dummies.add(dummy);
        }
        mysqlRepository.saveAll(dummies);

        System.out.println("========== INSERT END ==========");
    }

}
