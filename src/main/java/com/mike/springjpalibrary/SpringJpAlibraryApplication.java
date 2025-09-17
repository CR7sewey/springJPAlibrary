package com.mike.springjpalibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringJpAlibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJpAlibraryApplication.class, args);
    }

}
