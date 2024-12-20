package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAdminServer

public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);

    }
}
