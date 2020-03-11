package com.departments_db_rest_api_angular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = "com.departments_db_rest_api_angular")
public class SpringRestApiRunner {

    public static void main(String... args){
        SpringApplication.run(SpringRestApiRunner.class);
    }
}
