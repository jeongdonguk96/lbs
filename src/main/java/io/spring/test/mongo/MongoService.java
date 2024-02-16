package io.spring.test.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoService {

    private final MongoDatabase mongoDatabase;

    // 폴리곤 좌표를 이용해 폴리곤 내부 데이터를 조회한다.
    public void usePolygon(PolygonRequestDto polygonRequestDto) {
        long startTime;
        long endTime;
        long timeDiff;
        double transactionTime;

        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");
        String gu = polygonRequestDto.getGu();
        List<List<Double>> polygonCoordinateList = polygonRequestDto.getPolygonCoordinates();

        List<Position> positionList = new ArrayList<>();
        for (List<Double> polygonCoordinate : polygonCoordinateList) {
            positionList.add(new Position(polygonCoordinate.get(0), polygonCoordinate.get(1)));
        }

        Polygon polygon = new Polygon(positionList);
        Document query = new Document("location", new Document("$geoWithin", new Document("$geometry", polygon)));
        System.out.println("요청 구 = " + gu);

        List<Document> result;
        int pageSize = 1000000;
        int pageNum = 0;
        long totalDocuments = 0;

        startTime = System.currentTimeMillis();
        result = collection.find(query).into(new ArrayList<>());
        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;

//        while (true) {
//            startTime = System.currentTimeMillis();
////            result = collection.find(query)
////                    .skip(pageNum * pageSize)
////                    .limit(pageSize)
////                    .into(new ArrayList<>());
//
//            totalDocuments += result.size();
//
//            if (result.size() < pageSize) {
//                break;
//            }
//            endTime = System.currentTimeMillis();
//
//            timeDiff = (endTime - startTime);
//            transactionTime = timeDiff / 1000.0;
//
//            pageNum++;
//            System.out.println(pageNum + "차 데이터 조회, 총 조회수 = " + totalDocuments + ", TRX 시간 = " + transactionTime + "s");
//
//            if (totalDocuments >= 3000000) {
//                break;
//            }
//
//        }

        System.out.println("총 조회수 = " + result.size() + ", TRX 시간 = " + transactionTime + "s");
        System.out.println();
    }


    // 하나의 좌표를 이용해 원 반경 데이터를 조회한다.
    public void useCenterSphere(CenterSphereRequestDto centerSphereRequestDto) {
        long startTime;
        long endTime;
        long timeDiff;
        double transactionTime;

        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");
        String place = centerSphereRequestDto.getPlace();
        List<Double> coordinates = centerSphereRequestDto.getCoordinates();
        Double kmRadius = centerSphereRequestDto.getKmRadius();

        double longitude = coordinates.get(0);
        double latitude = coordinates.get(1);

        List<Double> centerPoint = Arrays.asList(longitude, latitude);
        double radiusRadians = kmRadius / 6378.1;

        Document query = new Document("location", new Document("$geoWithin", new Document("$centerSphere", Arrays.asList(centerPoint, radiusRadians))));
        System.out.println("요청 위치 = " + place + ", 반경 " + kmRadius + "km");

        startTime = System.currentTimeMillis();
        List<Document> result = collection.find(query).into(new ArrayList<>());

        endTime = System.currentTimeMillis();

        timeDiff = (endTime - startTime);
        transactionTime = timeDiff / 1000.0;

        System.out.println("총 조회수 = " + result.size() + ", TRX 시간 = " + transactionTime + "s");
        System.out.println();
    }

}
