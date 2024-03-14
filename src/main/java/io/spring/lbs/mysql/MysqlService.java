package io.spring.lbs.mysql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MysqlService {

    private final DataSource dataSource;

    @Transactional
    public int insertMmsData(List<Document> documentList) throws SQLException {
        long startTime;
        long endTime;
        long timeDiff;
        double transactionTime;

        int count = 0;
        int totalInsertedCount = 0;

        String sql = "INSERT INTO geospatial" +
                " (id, name_code, type, coordinates, use_yn, create_dt)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        startTime = System.currentTimeMillis();
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

                    if (count % 2000 == 0) {
                        int[] insertedCount = preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                        totalInsertedCount += Arrays.stream(insertedCount).sum();
                        log.info("2천 건 데이터 처리 완료");
                    }
                }

                int[] insertedCount = preparedStatement.executeBatch();
                preparedStatement.clearBatch();
                totalInsertedCount += Arrays.stream(insertedCount).sum();
                endTime = System.currentTimeMillis();

                timeDiff = (endTime - startTime);
                transactionTime = timeDiff / 1000.0;
                log.info("총 {}건 처리 완료, BATCH INSERT TRX 시간 = {}", totalInsertedCount, transactionTime + System.lineSeparator());
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        }

        return totalInsertedCount;
    }

}
