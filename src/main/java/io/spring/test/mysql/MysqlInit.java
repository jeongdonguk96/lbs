package io.spring.test.mysql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MysqlInit {

    private final DataSource dataSource;

//    @Bean
    @Transactional
    public void insert() {
        log.info("========== INSERT START ==========");
        long startTime;
        long microStartTime;
        long endTime;
        long timeDiff;
        double transactionTime;
        Random random = new Random();

        String sql = "INSERT INTO dummy" +
                " (id, name_code, address_code, use_yn, create_dt)" +
                " VALUES (?, ?, ?, ?, ?)";

        startTime = System.currentTimeMillis();
        microStartTime = System.currentTimeMillis();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 1; i <= 300000; i++) {
                    preparedStatement.setInt(1, i);
                    preparedStatement.setInt(2, random.nextInt(90000) + 10000);
                    preparedStatement.setInt(3, random.nextInt(90000) + 10000);
                    preparedStatement.setString(4, "Y");
                    preparedStatement.setTimestamp(5, new Timestamp(new Date().getTime()));

                    preparedStatement.addBatch();

                    if (i % 1000 == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                    }

                    if (i % 100000 == 0) {
                        endTime = System.currentTimeMillis();
                        timeDiff = (endTime - microStartTime);
                        transactionTime = timeDiff / 1000.0;
                        log.info("10만 건 당 TRX TIME = {}s", transactionTime);
                        microStartTime = System.currentTimeMillis();
                    }
                }
                preparedStatement.executeBatch();
                preparedStatement.clearBatch();

                endTime = System.currentTimeMillis();
                timeDiff = (endTime - startTime);
                transactionTime = timeDiff / 1000.0;
                log.info("TRX TIME = {}s", transactionTime);

                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        log.info("========== INSERT END ==========");
    }

}
