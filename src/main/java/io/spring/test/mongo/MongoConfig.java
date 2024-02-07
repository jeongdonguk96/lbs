package io.spring.test.mongo;

import com.mongodb.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoDatabase mongoDatabase() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        return mongoClient.getDatabase("test");
    }
}
