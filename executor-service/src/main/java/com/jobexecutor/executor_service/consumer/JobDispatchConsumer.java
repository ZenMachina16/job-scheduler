package com.jobexecutor.executor_service.consumer;

import com.jobexecutor.executor_service.event.JobDispatchEvent;
import com.jobexecutor.executor_service.runner.HttpJobRunner;
import com.jobexecutor.executor_service.runner.NoopJobRunner;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class JobDispatchConsumer {

    private final HttpJobRunner httpRunner;
    private final NoopJobRunner noopRunner;

    public JobDispatchConsumer(HttpJobRunner httpRunner, NoopJobRunner noopRunner) {
        this.httpRunner = httpRunner;
        this.noopRunner = noopRunner;
    }

    @KafkaListener(
        topics = "job.dispatch",
        groupId = "executor-group"
)
public void consume(
        JobDispatchEvent event
) {

    try {

        String result;

        if ("HTTP_CALL".equals(
                event.jobType()
        )) {

            result =
                    httpRunner.run(
                            event.payload()
                    );

        } else {

            result =
                    noopRunner.run(
                            event.payload()
                    );
        }

        System.out.println(
                "RESULT = "
                        + result
        );

    } catch (Exception e) {

        e.printStackTrace();
    }
}
}