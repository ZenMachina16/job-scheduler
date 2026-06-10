package com.jobscheduler.scheduler_service.event;

public record JobDispatchEvent(
        String jobId,
        String jobType,
        String payload,
        String timestamp
) {}