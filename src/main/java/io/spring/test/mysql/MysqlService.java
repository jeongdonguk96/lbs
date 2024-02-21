package io.spring.test.mysql;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MysqlService {

    private final DataSource dataSource;

    @Transactional
    public void insertMmsData(List<Document> documentList) throws SQLException {
        long startTime;
        long endTime;
        long timeDiff;
        double transactionTime;
        int count = 0;

        String sql = "INSERT INTO geospatial" +
                " (id, name_code, type, coordinates, use_yn, create_dt)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        String sql2 = "DELETE FROM geospatial";

        startTime = System.currentTimeMillis();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement deletePreparedStatement = connection.prepareStatement(sql2);
            deletePreparedStatement.execute();
            connection.commit();

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
                        preparedStatement.executeBatch();
                        connection.commit();
                        System.out.println("10만건 데이터 처리 완료");
                    }
                }

                preparedStatement.executeBatch();
                connection.commit();
                endTime = System.currentTimeMillis();

                timeDiff = (endTime - startTime);
                transactionTime = timeDiff / 1000.0;
                System.out.println("배치 INSERT TRX 시간 = " + transactionTime + "s");
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        }
    }

}
