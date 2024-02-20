package io.spring.test.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import io.spring.test.entity.Dummy;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class MongoInit {

    private final MongoDatabase mongoDatabase;

//    @Bean
    public void insert() {
        // dummy라는 이름의 컬렉션을 가져와 사용한다. 존재하지 않으면 생성한 후 가져온다.
        MongoCollection<Document> collection = mongoDatabase.getCollection("dummy");

        System.out.println("========== INSERT START ==========");
        Dummy dummy = new Dummy();
        Random random = new Random();
        collection.drop();
        mongoDatabase.createCollection("dummy");
        collection = mongoDatabase.getCollection("dummy");


        for (int i = 1; i <= 1000000; i++) {
            dummy.setId(i);
            dummy.setNameCode(random.nextInt(90000) + 10000);
            dummy.setAddressCode(random.nextInt(90000) + 10000);
            dummy.setUseYn("Y");
            dummy.setCreate_dt();

            Document document = new Document();
            document.append("_id", dummy.getId());
            document.append("name_code", dummy.getNameCode());
            document.append("address_code", dummy.getAddressCode());
            document.append("use_yn", dummy.getUseYn());
            document.append("create_dt", dummy.getCreate_dt());

            InsertOneResult insertOneResult = collection.insertOne(document);
            System.out.println("insertOneResult = " + insertOneResult);
        }

        System.out.println("========== INSERT END ==========");
    }

}