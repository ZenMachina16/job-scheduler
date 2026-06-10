package com.jobscheduler.scheduler_service.jobs;

import com.jobscheduler.scheduler_service.entity.Job;
import com.jobscheduler.scheduler_service.repository.JobRepository;
import com.jobscheduler.scheduler_service.service.JobDispatchProducer;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleJob implements org.quartz.Job {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobDispatchProducer producer;

    @Override
    public void execute(JobExecutionContext context) {

        String jobId =
            context.getMergedJobDataMap()
                   .getString("jobId");

        Job job =
            jobRepository.findById(jobId)
                         .orElseThrow();

        producer.dispatch(job);
    }
}