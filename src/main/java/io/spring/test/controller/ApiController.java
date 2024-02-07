package io.spring.test.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.spring.test.entity.Dummy;
import io.spring.test.mysql.MysqlRepository;
import io.spring.test.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final MysqlRepository mysqlRepository;
    private final RedisRepository redisRepository;
    private final MongoDatabase mongoDatabase;

    long startTime;
    long endTime;
    long timeDiff;
    double transactionTime;

    @GetMapping("/mysql")
    public void userMysql(@Param("id") int id) {
        System.out.println("id = " + id);
        startTime = System.currentTimeMillis();
        Optional<Dummy> result = mysqlRepository.findById(id);
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("mysql result = " + result);
    }

    @GetMapping("/redis")
    public void useRedis(@Param("id") int id) {
        System.out.println("id = " + id);
        redisRepository.find(String.valueOf(id));
    }

    @GetMapping("/mongo")
    public void useMongo(@Param("id") int id) {
        System.out.println("id = " + id);
        MongoCollection<Document> collection = mongoDatabase.getCollection("dummy");
        startTime = System.currentTimeMillis();
        Document result = collection.find(Filters.eq("id", id)).first();
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("mongo result = " + result);
    }

}
