package com.jobexecutor.executor_service.event;

public record JobResultEvent(
        String jobId,
        String status,
        String output,
        long durationMs
) {}