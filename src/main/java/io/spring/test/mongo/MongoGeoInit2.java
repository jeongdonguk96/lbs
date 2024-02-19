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

            double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
            double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();

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