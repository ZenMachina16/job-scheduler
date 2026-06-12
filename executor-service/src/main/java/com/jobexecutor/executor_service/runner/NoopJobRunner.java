package com.jobexecutor.executor_service.runner;

import org.springframework.stereotype.Component;

@Component
public class NoopJobRunner
        implements JobRunner {

    @Override
    public String run(String payload) {

        System.out.println(
                "NOOP EXECUTED"
        );

        return "SUCCESS";
    }
}