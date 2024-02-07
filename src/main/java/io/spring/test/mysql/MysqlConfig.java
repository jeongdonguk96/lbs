package io.spring.test.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.yml")
@RequiredArgsConstructor
public class MysqlConfig {

    private final Environment env;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("spring.datasource.hikari.driver-class-name"));
        hikariConfig.setJdbcUrl(env.getProperty("spring.datasource.hikari.jdbc-url"));
        hikariConfig.setUsername(env.getProperty("spring.datasource.hikari.username"));
        hikariConfig.setPassword(env.getProperty("spring.datasource.hikari.password"));
        hikariConfig.setMinimumIdle(Integer.parseInt(env.getProperty("spring.datasource.hikari.minimum-idle")));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource.hikari.maximum-pool-size")));
        return new HikariDataSource(hikariConfig);
    }

}
