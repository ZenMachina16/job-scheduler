package com.jobexecutor.executor_service.event;

public record JobDispatchEvent(
        String jobId,
        String jobType,
        String payload,
        String timestamp
) {}