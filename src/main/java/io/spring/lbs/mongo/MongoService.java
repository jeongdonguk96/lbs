package io.spring.lbs.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import io.spring.lbs.aop.ServiceTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MongoService {

    private final MongoDatabase mongoDatabase;


    // 폴리곤 좌표를 이용해 폴리곤 내부 데이터를 조회한다.
    @ServiceTrace
    @Transactional(readOnly = true)
    public List<Document> usePolygon(PolygonRequestDto polygonRequestDto) {
        // 사용할 컬렉션의 데이터를 가져온다.
        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");

        // 요청한 구와 좌표를 가져온다.
        String gu = polygonRequestDto.getGu();
        List<List<Double>> polygonCoordinateList = polygonRequestDto.getPolygonCoordinates();

        // 좌표들의 배열을 Position 타입의 List 객체로 받는다.
        // [위도, 경도]의 배열 하나의 Position 타입으로 변환하는 것이다.
        List<Position> positionList = new ArrayList<>();
        for (List<Double> polygonCoordinate : polygonCoordinateList) {
            positionList.add(new Position(polygonCoordinate.get(0), polygonCoordinate.get(1)));
        }

        // 전체 좌표를 하나의 폴리곤으로 만든다.
        Polygon polygon = new Polygon(positionList);

        // 위 위치 좌표로 지리 공간 쿼리로 생성한다.
        Document query = new Document("location", new Document("$geoWithin", new Document("$geometry", polygon)));

        // 쿼리를 실행해 반환한다.
        return collection.find(query).into(new ArrayList<>());
    }


    // 하나의 좌표를 이용해 원 반경 데이터를 조회한다.
    @ServiceTrace
    @Transactional(readOnly = true)
    public List<Document> useCenterSphere(CenterSphereRequestDto centerSphereRequestDto) {
        // 사용할 컬렉션의 데이터를 가져온다.
        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");

        // 요청한 지점과 좌표, 검색할 반경을 가져온다.
        String place = centerSphereRequestDto.getPlace();
        List<Double> coordinates = centerSphereRequestDto.getCoordinates();
        double kmRadius = centerSphereRequestDto.getKmRadius();
        double radiusRadians = kmRadius / 6378.1;

        // 위 데이터로 지리 공간 쿼리로 생성한다.
        Document query = new Document("location", new Document("$geoWithin", new Document("$centerSphere", Arrays.asList(coordinates, radiusRadians))));

        // 쿼리를 실행해 반환한다.
        return collection.find(query).into(new ArrayList<>());
    }

}
