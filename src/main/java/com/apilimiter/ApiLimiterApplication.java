package com.apilimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApiLimiterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiLimiterApplication.class, args);
    }
}