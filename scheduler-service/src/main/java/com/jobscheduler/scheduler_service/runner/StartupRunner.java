package com.jobscheduler.scheduler_service.runner;

import com.jobscheduler.scheduler_service.event.JobDispatchEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final KafkaTemplate<String, JobDispatchEvent> kafkaTemplate;

    public StartupRunner(
            KafkaTemplate<String, JobDispatchEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run(String... args) {

        System.out.println(
            "KafkaTemplate Bean Created Successfully"
        );
    }
}