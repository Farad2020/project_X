package com.example.projectX.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresDataSource {

    private HikariDataSource hikariDataSource;

    @Bean
    @ConfigurationProperties("app.datasource")
    public HikariDataSource hikariDataSource() {
        hikariDataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
        return hikariDataSource;
    }

    public HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }
}
