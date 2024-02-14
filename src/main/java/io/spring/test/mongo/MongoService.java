package io.spring.test.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoService {

    private final MongoDatabase mongoDatabase;

    long startTime;
    long endTime;
    long timeDiff;
    double transactionTime;

    public void getPolyGeo(PolygonRequestDto polygonRequestDto) {
        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");
        List<List<Double>> polygonCoordinates = polygonRequestDto.getPolygonCoordinates();

        Document query = new Document("location", new Document("$geoWithin", new Document("$geometry",
                new Document("type", "Polygon").append("coordinates", List.of(polygonCoordinates)))));

        System.out.println("query = " + query);

        startTime = System.currentTimeMillis();
        ArrayList<Document> result = collection.find(query).into(new ArrayList<>());
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== MONGO GEO TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("mongo geo result = " + result);
        System.out.println();
    }

}
