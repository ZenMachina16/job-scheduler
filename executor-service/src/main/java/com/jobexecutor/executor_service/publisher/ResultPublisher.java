package com.jobexecutor.executor_service.publisher;

import com.jobexecutor.executor_service.event.JobResultEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResultPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ResultPublisher(
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(
            JobResultEvent event
    ) {

        kafkaTemplate.send(
                "job.result",
                event.jobId(),
                event
        );

        System.out.println(
                "RESULT EVENT SENT: "
                + event.jobId()
        );
    }
}