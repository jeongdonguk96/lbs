package io.spring.test.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.spring.test.sampleEntity.MongoGeoDummy2;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class MongoGeoInit2 {

    private final MongoDatabase mongoDatabase;

//    @Bean
    public void insert() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");
//        collection.createIndex(new Document("location", "2dsphere"));
//        collection.createIndex(Indexes.geo2dsphere("location"));

        // 최소/최대 위도/경도를 설정한다.
        double minLatitude = 37.434068;
        double maxLatitude = 37.684881;
        double minLongitude = 126.791833;
        double maxLongitude = 127.160525;

        System.out.println("========== INSERT START ==========");
        MongoGeoDummy2 dummy = new MongoGeoDummy2();
        Random random = new Random();

        for (int i = 1; i <= 10000000; i++) {
            dummy.setId(i);
            dummy.setNameCode(i);
            dummy.setUseYn("Y");
            dummy.setCreate_dt();

            // 최소 경도와 최대 경도 사이에서 랜덤한 경도값을 추출한다.
            // 최소 위도와 최대 위도 사이에서 랜덤한 위도값을 추출한다.
            double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
            double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();

            // 소수점 6자리까지만 추출해 사용한다.
            latitude = Double.parseDouble(String.format("%.6f", latitude));
            longitude = Double.parseDouble(String.format("%.6f", longitude));

            Document document = new Document();
            document.append("_id", dummy.getId());
            document.append("name_code", dummy.getNameCode());
            document.append("location", new Document("type", "Point").append("coordinates", Arrays.asList(longitude, latitude)));
            document.append("use_yn", dummy.getUseYn());
            document.append("create_dt", dummy.getCreate_dt());

            collection.insertOne(document);
        }

        System.out.println("========== INSERT END ==========");
    }

}