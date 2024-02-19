package io.spring.test.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import io.spring.test.sampleEntity.MongoGeoDummy;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class MongoGeoInit {

    private final MongoDatabase mongoDatabase;

//    @Bean
    public void insert() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");
        collection.drop();

        mongoDatabase.createCollection("geo");
        collection = mongoDatabase.getCollection("geo");

        System.out.println("========== INSERT START ==========");
        MongoGeoDummy dummy = new MongoGeoDummy();

        String[] nameCode = {"숭례문", "원각사지 십층석탑", "진흥왕 순수비", "근정전", "흥인지문", "동관왕묘", "인조 별서 유기비", "마애여래 입상", "이윤탁 한글영비", "살곶이 다리"};
        double[] longitude = {37.559722, 37.571667, 37.312778, 37.344444, 37.571111, 37.572778, 37.608889, 37.626667, 37.645278, 37.557500};
        double[] latitude = {126.975278, 126.988333, 126.585000, 126.583889, 127.009722, 127.018333, 126.916667, 126.937778, 127.076667, 127.034722};

        for (int i = 0; i < 10; i++) {
            dummy.setId(2000001 + i);
            dummy.setNameCode(nameCode[i]);
            dummy.setUseYn("Y");
            dummy.setCreate_dt();

            Document document = new Document();
            document.append("_id", dummy.getId());
            document.append("name_code", dummy.getNameCode());
            document.append("location", new Document("type", "Point").append("coordinates", Arrays.asList(latitude[i], longitude[i])));
            document.append("use_yn", dummy.getUseYn());
            document.append("create_dt", dummy.getCreate_dt());
            InsertOneResult insertOneResult = collection.insertOne(document);
            System.out.println("insertOneResult = " + insertOneResult);
        }

        System.out.println("========== INSERT END ==========");
    }

}