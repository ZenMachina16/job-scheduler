package com.jobscheduler.scheduler_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTestRunner implements CommandLineRunner {

    private final StringRedisTemplate redis;

    public RedisTestRunner(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public void run(String... args) {

        redis.opsForValue().set("test", "hello");

        System.out.println(
                "Redis value = "
                + redis.opsForValue().get("test")
        );
    }
}