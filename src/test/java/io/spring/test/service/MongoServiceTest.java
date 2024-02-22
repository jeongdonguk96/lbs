package io.spring.test.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import io.spring.test.mongo.PolygonRequestDto;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Transactional
@SpringBootTest
class MongoServiceTest {

    @Autowired private MongoDatabase mongoDatabase;

    @Test
    @DisplayName("몽고DB 폴리곤 조회 테스트")
    void usePolygonTest() {
        // given
        List<List<Double>> coordinates = new ArrayList<>();
        coordinates.add(Arrays.asList(126.969665, 37.554877));
        coordinates.add(Arrays.asList(126.976429, 37.553087));
        coordinates.add(Arrays.asList(126.976546, 37.555221));
        coordinates.add(Arrays.asList(126.978747, 37.553207));
        coordinates.add(Arrays.asList(126.985415, 37.553687));
        coordinates.add(Arrays.asList(126.995229, 37.547229));
        coordinates.add(Arrays.asList(126.998325, 37.549695));
        coordinates.add(Arrays.asList(127.004368, 37.550159));
        coordinates.add(Arrays.asList(127.006133, 37.548202));
        coordinates.add(Arrays.asList(127.004997, 37.546170));
        coordinates.add(Arrays.asList(127.007278, 37.543859));
        coordinates.add(Arrays.asList(127.008985, 37.544133));
        coordinates.add(Arrays.asList(127.009591, 37.539639));
        coordinates.add(Arrays.asList(127.017262, 37.537681));
        coordinates.add(Arrays.asList(127.017193, 37.533789));
        coordinates.add(Arrays.asList(127.006140, 37.522902));
        coordinates.add(Arrays.asList(126.990702, 37.513092));
        coordinates.add(Arrays.asList(126.985537, 37.506547));
        coordinates.add(Arrays.asList(126.975249, 37.507021));
        coordinates.add(Arrays.asList(126.949885, 37.517518));
        coordinates.add(Arrays.asList(126.949879, 37.527029));
        coordinates.add(Arrays.asList(126.944819, 37.534520));
        coordinates.add(Arrays.asList(126.953357, 37.537500));
        coordinates.add(Arrays.asList(126.958174, 37.542313));
        coordinates.add(Arrays.asList(126.957905, 37.545463));
        coordinates.add(Arrays.asList(126.963720, 37.548792));
        coordinates.add(Arrays.asList(126.962338, 37.551555));
        coordinates.add(Arrays.asList(126.965714, 37.554151));
        coordinates.add(Arrays.asList(126.969185, 37.555662));
        coordinates.add(Arrays.asList(126.969665, 37.554877));

        PolygonRequestDto polygonRequestDto = new PolygonRequestDto();
        polygonRequestDto.setGu("용산구");
        polygonRequestDto.setPolygonCoordinates(coordinates);

        List<Position> positionList = new ArrayList<>();
        for (List<Double> polygonCoordinate : coordinates) {
            positionList.add(new Position(polygonCoordinate.get(0), polygonCoordinate.get(1)));
        }

        // when
        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");
        Polygon polygon = new Polygon(positionList);
        Document query = new Document("location", new Document("$geoWithin", new Document("$geometry", polygon)));
        List<Document> documentList = collection.find(query).into(new ArrayList<>());

        // then
        assertThat(documentList.size()).isEqualTo(241203);
    }
    
    @Test
    @DisplayName("몽고DB 원 반경 조회 테스트")
    void useCenterSphereTest() {
        // given
        List<Double> coordinates = Arrays.asList(126.972658, 37.576025);
        double radiusRadians = 5 / 6378.1;

        // when
        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");
        Document query = new Document("location", new Document("$geoWithin", new Document("$centerSphere", Arrays.asList(coordinates, radiusRadians))));
        List<Document> documentList = collection.find(query).into(new ArrayList<>());

        // then
        assertThat(documentList.size()).isEqualTo(865843);
    }

}