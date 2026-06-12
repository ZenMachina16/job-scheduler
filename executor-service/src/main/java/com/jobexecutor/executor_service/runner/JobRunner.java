package com.jobexecutor.executor_service.runner;

public interface JobRunner {

    String run(String payload)
            throws Exception;
}