package io.spring.test.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoDatabase mongoDatabase() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

        // MongoClient를 설정한다.
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder.minSize(10).maxSize(10)) // 커넥션 풀의 크기를 설정한다.
                .build();

        // MongoClient 인스턴스를 생성한다.
        MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient.getDatabase("test");
    }
}
