package io.spring.test.mysql;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class MysqlInit {

    private final DataSource dataSource;

//    @Bean
    public void insert() {
        System.out.println("========== INSERT START ==========");
        Random random = new Random();

        String sql = "INSERT INTO dummy" +
                " (id, name_code, address_code, use_yn, create_dt)" +
                " VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 1; i <= 1000000; i++) {
                    preparedStatement.setInt(1, i);
                    preparedStatement.setInt(2, random.nextInt(90000) + 10000);
                    preparedStatement.setInt(3, random.nextInt(90000) + 10000);
                    preparedStatement.setString(4, "Y");
                    preparedStatement.setTimestamp(5, new Timestamp(new Date().getTime()));

                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("========== INSERT END ==========");
    }

}
