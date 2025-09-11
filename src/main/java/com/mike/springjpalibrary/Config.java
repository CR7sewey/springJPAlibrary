package com.mike.springjpalibrary;

import com.mike.springjpalibrary.service.AuthorService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class Config implements CommandLineRunner {

    @Value("${spring.datasource.url}") // injeta valor na variavel
    private String url;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() { // generic datasource
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    @Primary
    public DataSource hikariDataSource() { // allows pool of connections
        HikariConfig dataSource = new HikariConfig();
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setMaximumPoolSize(10); // size of pool of connections, maximum
        dataSource.setMinimumIdle(1); // initial size of the pool
        dataSource.setPoolName("library-pool");
        dataSource.setMaxLifetime(600000); // 600 mil ms
        dataSource.setConnectionTimeout(100000); // time limit to establish a connection
        dataSource.setConnectionTestQuery("SELECT 1"); // test to check if it is ok


        return new HikariDataSource(dataSource);

    }



    @Override
    public void run(String... args) throws Exception {


    }


}
