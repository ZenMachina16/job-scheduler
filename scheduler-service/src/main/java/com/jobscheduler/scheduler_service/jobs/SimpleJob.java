package com.jobscheduler.scheduler_service.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class SimpleJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {

        String jobId =
            context.getMergedJobDataMap()
                   .getString("jobId");

        System.out.println(
            "Executing Quartz Job: " + jobId
        );
    }
}