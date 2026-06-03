package com.jobscheduler.scheduler_service.controller;

import com.jobscheduler.scheduler_service.entity.Job;
import com.jobscheduler.scheduler_service.service.JobService;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public Job create(@RequestBody Job job) throws SchedulerException {
        return jobService.createJob(job);
    }

    @GetMapping
    public List<Job> getAll() {
        return jobService.getAllJobs();
    }
}