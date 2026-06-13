package com.jobscheduler.scheduler_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class SchedulerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchedulerServiceApplication.class, args);
	}
}