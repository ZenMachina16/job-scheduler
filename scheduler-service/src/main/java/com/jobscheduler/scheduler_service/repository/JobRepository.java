package com.jobscheduler.scheduler_service.repository;

import com.jobscheduler.scheduler_service.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, String> {
}