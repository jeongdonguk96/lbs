package io.spring.test.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.opencsv.exceptions.CsvValidationException;
import io.spring.test.entity.Dummy;
import io.spring.test.mongo.CenterSphereRequestDto;
import io.spring.test.mongo.MongoService;
import io.spring.test.mongo.PolygonRequestDto;
import io.spring.test.mysql.DummyRepository;
import io.spring.test.mysql.MysqlService;
import io.spring.test.redis.RedisRepository;
import io.spring.test.sampleEntity.RedisGeoDummy;
import io.spring.test.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.json.simple.parser.ParseException;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final DummyRepository mysqlRepository;
    private final RedisRepository redisRepository;
    private final MongoDatabase mongoDatabase;
    private final MysqlService mysqlService;
    private final MongoService mongoService;

    long startTime;
    long endTime;
    long timeDiff;
    double transactionTime;

    @GetMapping("/mysql")
    public void userMysql(@Param("id") int id) {
        startTime = System.currentTimeMillis();
        Optional<Dummy> result = mysqlRepository.findById(id);
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("id = " + id + ", ========== MYSQL TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("mysql result = " + result);
        System.out.println();
    }

    @GetMapping("/redis")
    public void useRedis(@Param("id") int id) {
        redisRepository.find(String.valueOf(id));
    }

    @GetMapping("/redis/all")
    public void useRedis() {
        redisRepository.findAll();
    }

    @GetMapping("/csv/write")
    public void writeCsv() throws IOException {
        CsvUtil.writeCsv();
    }

    @GetMapping("/csv/read")
    public void readCsv() throws CsvValidationException, IOException {
        String filePath = "C:\\Users\\nb18-03hp\\IdeaProjects\\test\\user_data.csv";
        File file = new File(filePath);
        List<String[]> readFile = CsvUtil.readCsv(file);
    }

    @GetMapping("/csv/mapping")
    public void mapCsvData() throws CsvValidationException, IOException, ParseException {
        String filePath = "C:\\Users\\nb18-03hp\\IdeaProjects\\test\\user_data.csv";
        File file = new File(filePath);
        List<String[]> readFile = CsvUtil.readCsv(file);

        List<String> keyList = redisRepository.findAllRedisKey();
        List<Object> valueList = redisRepository.findAllRedisValue(keyList);
        List<RedisGeoDummy> objectList = redisRepository.stringToDummy(valueList);


//        ServiceUtil.processUserData()

    }

    @GetMapping("/mongo")
    public void useMongo(@Param("id") int id) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("dummy");
        startTime = System.currentTimeMillis();
        Document result = collection.find(Filters.eq("_id", id)).first();
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("id = " + id + ", ========== MONGO TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("mongo result = " + result);
        System.out.println();
    }

    @PostMapping("/mongo/polygon")
    public int useMongoPolygon(@RequestBody PolygonRequestDto polygonRequestDto) throws SQLException {
        List<Document> documentList = mongoService.usePolygon(polygonRequestDto);
        return mysqlService.insertMmsData(documentList);
    }

    @PostMapping("/mongo/centerSphere")
    public int useMongoCenterSphere(@RequestBody CenterSphereRequestDto centerSphereRequestDto) throws SQLException {
        List<Document> documentList = mongoService.useCenterSphere(centerSphereRequestDto);
        return mysqlService.insertMmsData(documentList);
    }

}
