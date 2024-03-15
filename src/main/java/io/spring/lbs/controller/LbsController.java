package io.spring.lbs.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.opencsv.exceptions.CsvValidationException;
import io.spring.lbs.aop.ControllerTrace;
import io.spring.lbs.mongo.CenterSphereRequestDto;
import io.spring.lbs.mongo.MongoService;
import io.spring.lbs.mongo.PolygonRequestDto;
import io.spring.lbs.mysql.DummyRepository;
import io.spring.lbs.mysql.MysqlService;
import io.spring.lbs.redis.RedisRepository;
import io.spring.lbs.util.CsvUtil;
import io.spring.lbs.util.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LbsController {

    private final DummyRepository mysqlRepository;
    private final RedisRepository redisRepository;
    private final MongoDatabase mongoDatabase;
    private final MysqlService mysqlService;
    private final MongoService mongoService;


    @GetMapping("/mysql")
    @ControllerTrace
    public void userMysql(@Param("id") int id) {
        mysqlRepository.findById(id);
    }


    @GetMapping("/redis")
    @ControllerTrace
    public void useRedis(@Param("id") int id) {
        redisRepository.find(String.valueOf(id));
    }


    @GetMapping("/redis/all")
    @ControllerTrace
    public void useRedis() {
        redisRepository.findAll();
    }


    @GetMapping("/csv/write")
    @ControllerTrace
    public void writeCsv() throws IOException {
        CsvUtil.writeCsv();
    }


    @GetMapping("/csv/read")
    @ControllerTrace
    public void readCsv() throws CsvValidationException, IOException {
        String filePath = "C:\\Users\\nb18-03hp\\IdeaProjects\\test\\user_data.csv";
        File file = new File(filePath);
        List<String[]> readFile = CsvUtil.readCsv(file);
    }


    @GetMapping("/csv/mapping")
    @ControllerTrace
    public void mapCsvData() throws CsvValidationException, IOException {
        String filePath = "C:\\Users\\nb18-03hp\\IdeaProjects\\test\\user_data.csv";
        File file = new File(filePath);
        List<String[]> readFile = CsvUtil.readCsv(file);

        List<String> keyList = redisRepository.findAllRedisKey();
        List<Object> valueList = redisRepository.findAllRedisValue(keyList);
        Map<Long, Double[]> coordinatesMap = ServiceUtil.stringToDummyMap(valueList);
        readFile = ServiceUtil.mappingData(readFile, coordinatesMap);
    }


    @GetMapping("/mongo")
    @ControllerTrace
    public void useMongo(@Param("id") int id) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("dummy");
        Document result = collection.find(Filters.eq("_id", id)).first();
    }


    @PostMapping("/mongo/polygon")
    @ControllerTrace
    public int useMongoPolygon(@RequestBody PolygonRequestDto polygonRequestDto) throws SQLException {
        List<Document> documentList = mongoService.usePolygon(polygonRequestDto);
//        return mysqlService.insertMmsData(documentList);
        return 1;
    }


    @PostMapping("/mongo/centerSphere")
    @ControllerTrace
    public int useMongoCenterSphere(@RequestBody CenterSphereRequestDto centerSphereRequestDto) throws SQLException {
        List<Document> documentList = mongoService.useCenterSphere(centerSphereRequestDto);
//        return mysqlService.insertMmsData(documentList);
        return 1;
    }

}
