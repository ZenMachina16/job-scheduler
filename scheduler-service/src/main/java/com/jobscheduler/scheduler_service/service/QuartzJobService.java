package com.jobscheduler.scheduler_service.service;

import com.jobscheduler.scheduler_service.entity.Job;
import com.jobscheduler.scheduler_service.jobs.SimpleJob;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Service
public class QuartzJobService {

    private final Scheduler scheduler;

    public QuartzJobService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleJob(Job job)
            throws SchedulerException {

        JobDetail detail =
                JobBuilder.newJob(SimpleJob.class)
                        .withIdentity(job.getId())
                        .usingJobData("jobId", job.getId())
                        .build();

        Trigger trigger =
                TriggerBuilder.newTrigger()
                        .withIdentity(
                                job.getId() + "-trigger"
                        )
                        .withSchedule(
                                CronScheduleBuilder
                                        .cronSchedule(
                                                job.getCronExpression()
                                        )
                        )
                        .build();
        System.out.println(
                "Scheduling Job: " + job.getId()
        );  

        scheduler.scheduleJob(
                detail,
                trigger
        );
    }
}