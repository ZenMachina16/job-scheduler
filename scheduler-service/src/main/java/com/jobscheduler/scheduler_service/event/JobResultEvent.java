package com.jobscheduler.scheduler_service.event;

public record JobResultEvent(
        String jobId,
        String status,
        String output,
        long durationMs
) {}