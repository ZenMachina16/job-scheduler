package com.jobexecutor.executor_service.consumer;

import com.jobexecutor.executor_service.event.JobDispatchEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class JobDispatchConsumer {

    @KafkaListener(
            topics = "job.dispatch",
            groupId = "executor-group"
    )
    public void consume(
            JobDispatchEvent event
    ) {

        System.out.println(
                "Received Job: "
                        + event.jobId()
        );
    }
}