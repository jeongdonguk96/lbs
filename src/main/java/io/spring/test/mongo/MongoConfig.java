package io.spring.test.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Bean
    public MongoDatabase mongoDatabase() {
        // db url을 설정한다.
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

        // MongoClient를 설정한다.
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString) // db url을 설정한다.
                .applyToConnectionPoolSettings(builder -> builder.minSize(10).maxSize(10)) // 커넥션 풀의 크기를 설정한다.
                .build();

        // MongoClient 인스턴스를 생성한다.
        MongoClient mongoClient = MongoClients.create(settings);

        // test라는 이름의 데이터베이스를 가져온다.
        return mongoClient.getDatabase("test");
    }

    @Bean
    public MongoClient mongoClient() {
        // db url을 설정한다.
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

        // MongoClient를 설정한다.
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString) // db url을 설정한다.
                .applyToConnectionPoolSettings(builder -> builder.minSize(10).maxSize(10)) // 커넥션 풀의 크기를 설정한다.
                .build();

        // MongoClient 인스턴스를 생성한다.
        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "test");
    }

}
