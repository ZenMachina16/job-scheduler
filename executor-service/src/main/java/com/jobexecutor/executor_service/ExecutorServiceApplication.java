package com.jobexecutor.executor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ExecutorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                ExecutorServiceApplication.class,
                args
        );
    }
}