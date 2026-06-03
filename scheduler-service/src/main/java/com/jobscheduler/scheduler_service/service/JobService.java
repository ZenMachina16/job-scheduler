package com.jobscheduler.scheduler_service.service;

import com.jobscheduler.scheduler_service.entity.Job;
import com.jobscheduler.scheduler_service.repository.JobRepository;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final QuartzJobService quartzJobService;

    public JobService(JobRepository jobRepository, QuartzJobService quartzJobService) {
        this.jobRepository = jobRepository;
        this.quartzJobService = quartzJobService;
    }

    public Job createJob(Job job)
        throws SchedulerException {

    Job savedJob =
            jobRepository.save(job);

    quartzJobService.scheduleJob(savedJob);

    return savedJob;
}

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}