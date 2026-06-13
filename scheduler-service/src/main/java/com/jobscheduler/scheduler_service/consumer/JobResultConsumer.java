package com.jobscheduler.scheduler_service.consumer;

import com.jobscheduler.scheduler_service.entity.Job;
import com.jobscheduler.scheduler_service.event.JobResultEvent;
import com.jobscheduler.scheduler_service.repository.JobRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JobResultConsumer {

    private final JobRepository jobRepository;

    public JobResultConsumer(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @KafkaListener(
            topics = "job.result",
            groupId = "scheduler-group"
    )
    public void consume(
            JobResultEvent event
    ) {

        Job job =
            jobRepository
                .findById(event.jobId())
                .orElseThrow();

        job.setLastStatus(
                event.status()
        );

        job.setLastOutput(
                event.output()
        );

        job.setLastDurationMs(
                event.durationMs()
        );

        job.setLastRunAt(
                LocalDateTime.now()
        );

        jobRepository.save(job);
    }
}