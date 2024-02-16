package io.spring.test.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
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
        String gu = polygonRequestDto.getGu();
        List<List<Double>> polygonCoordinateList = polygonRequestDto.getPolygonCoordinates();

        List<Position> positionList = new ArrayList<>();
        for (List<Double> polygonCoordinate : polygonCoordinateList) {
            positionList.add(new Position(polygonCoordinate.get(0), polygonCoordinate.get(1)));
        }

        Polygon polygon = new Polygon(positionList);
        Document query = new Document("location", new Document("$geoWithin", new Document("$geometry", polygon)));
        System.out.println("요청 구 = " + gu + ", query = " + query);

        int pageSize = 1000000;
        int pageNum = 0;
        long totalDocuments = 0;

        startTime = System.currentTimeMillis();
        ArrayList<Document> result = collection.find(query).into(new ArrayList<>());
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;
        System.out.println("========== MONGO GEO TRX TIME = { " + transactionTime + "}s ==========");
        System.out.println("mongo geo result size = " + result.size());
        System.out.println();
    }

}
