package com.jobexecutor.executor_service.consumer;

import com.jobexecutor.executor_service.event.JobDispatchEvent;
import com.jobexecutor.executor_service.event.JobResultEvent;
import com.jobexecutor.executor_service.publisher.ResultPublisher;
import com.jobexecutor.executor_service.runner.HttpJobRunner;
import com.jobexecutor.executor_service.runner.NoopJobRunner;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class JobDispatchConsumer {

    private final HttpJobRunner httpRunner;
    private final NoopJobRunner noopRunner;
    private final ResultPublisher resultPublisher;

    public JobDispatchConsumer(
            HttpJobRunner httpRunner,
            NoopJobRunner noopRunner,
            ResultPublisher resultPublisher
    ) {
        this.httpRunner = httpRunner;
        this.noopRunner = noopRunner;
        this.resultPublisher = resultPublisher;
    }

    @KafkaListener(
            topics = "job.dispatch",
            groupId = "executor-group"
    )
    public void consume(JobDispatchEvent event) {

        long start = System.currentTimeMillis();

        try {

            String result;

            if ("HTTP_CALL".equals(event.jobType())) {

                result = httpRunner.run(
                        event.payload()
                );

            } else {

                result = noopRunner.run(
                        event.payload()
                );
            }

            System.out.println(
                    "RESULT = " + result
            );

            resultPublisher.publish(
                    new JobResultEvent(
                            event.jobId(),
                            "SUCCESS",
                            result,
                            System.currentTimeMillis() - start
                    )
            );

        } catch (Exception e) {

            resultPublisher.publish(
                    new JobResultEvent(
                            event.jobId(),
                            "FAILED",
                            e.getMessage(),
                            System.currentTimeMillis() - start
                    )
            );

            e.printStackTrace();
        }
    }
}