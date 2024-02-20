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
                    java.sql.Date sqlTimestamp = (java.sql.Date) createDate;
                    preparedStatement.setDate(6, sqlTimestamp);

                    preparedStatement.addBatch();
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
