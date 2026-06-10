package com.jobscheduler.scheduler_service.service;

import com.jobscheduler.scheduler_service.entity.Job;
import com.jobscheduler.scheduler_service.event.JobDispatchEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JobDispatchProducer {

    private final KafkaTemplate<String, JobDispatchEvent> kafkaTemplate;

    public JobDispatchProducer(
            KafkaTemplate<String, JobDispatchEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void dispatch(Job job) {

        JobDispatchEvent event =
                new JobDispatchEvent(
                        job.getId(),
                        job.getJobType(),
                        job.getPayload(),
                        Instant.now().toString()
                );

        kafkaTemplate.send(
                "job.dispatch",
                job.getId(),
                event
        );

        System.out.println(
                "Kafka Event Sent for Job: "
                        + job.getId()
        );
    }
}