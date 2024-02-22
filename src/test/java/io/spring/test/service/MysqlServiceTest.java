package io.spring.test.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.spring.test.mysql.GeospatialRepository;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class MysqlServiceTest {

    @Autowired private DataSource dataSource;
    @Autowired private MongoDatabase mongoDatabase;
    @Autowired private GeospatialRepository geospatialRepository;

    @BeforeEach
    void deleteAll() {
        geospatialRepository.deleteAll();
    }

    @Test
    @DisplayName("mysql 데이터 생성 테스트")
    void insertMmsDataTest() {
        // given
        int count = 0;
        int totalInsertedCount = 0;

        String sql = "INSERT INTO geospatial" +
                " (id, name_code, type, coordinates, use_yn, create_dt)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        List<Double> coordinates = Arrays.asList(126.972658, 37.576025);
        double radiusRadians = 1 / 6378.1;

        MongoCollection<Document> collection = mongoDatabase.getCollection("geo");
        Document query = new Document("location", new Document("$geoWithin", new Document("$centerSphere", Arrays.asList(coordinates, radiusRadians))));
        List<Document> documentList = collection.find(query).into(new ArrayList<>());

        // when
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (Document document : documentList) {
                    preparedStatement.setInt(1, document.getInteger("_id"));
                    preparedStatement.setInt(2, document.getInteger("name_code"));
                    Document locationDocument = (Document) document.get("location");
                    preparedStatement.setString(3, locationDocument.getString("type"));
                    preparedStatement.setString(4, locationDocument.get("coordinates", List.class).toString());
                    preparedStatement.setString(5, document.getString("use_yn"));
                    Object createDateObject = document.get("create_dt");
                    java.util.Date createDate = (java.util.Date) createDateObject;
                    preparedStatement.setDate(6, new java.sql.Date(createDate.getTime()));

                    preparedStatement.addBatch();
                    count++;

                    if (count % 100000 == 0) {
                        int[] insertedCount = preparedStatement.executeBatch();
                        connection.commit();
                        totalInsertedCount += Arrays.stream(insertedCount).sum();
                        System.out.println("10만건 데이터 처리 완료");
                    }
                }

                int[] insertedCount = preparedStatement.executeBatch();
                connection.commit();
                totalInsertedCount += Arrays.stream(insertedCount).sum();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // then
        assertThat(totalInsertedCount).isEqualTo(34481);

    }

}