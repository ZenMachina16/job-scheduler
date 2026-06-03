# Job Scheduler Project Documentation

## Building in-progress

## Overview

This document summarizes the implementation progress of the Job Scheduler project up to the completion of Quartz persistence using PostgreSQL.

The objective of this phase was to build a scheduler service capable of:

* Exposing REST APIs for job management
* Persisting job metadata in PostgreSQL
* Scheduling jobs using Quartz
* Persisting Quartz triggers across application restarts

---

# Technology Stack

| Component              | Technology       |
| ---------------------- | ---------------- |
| Language               | Java 21          |
| Framework              | Spring Boot      |
| Build Tool             | Maven            |
| Database               | PostgreSQL 16    |
| ORM                    | Hibernate (JPA)  |
| Scheduler              | Quartz Scheduler |
| Cache (Future Use)     | Redis            |
| Messaging (Future Use) | Kafka            |

---

# Project Structure

```text
scheduler-service
│
├── controller/
├── service/
├── repository/
├── entity/
├── jobs/
├── config/
│
└── SchedulerServiceApplication.java
```

---

# Architecture Overview

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
Hibernate / JPA
  │
  ▼
PostgreSQL

          +

Quartz Scheduler
  │
  ▼
Scheduled Job Execution
```

---

# Layer Responsibilities

## Entity Layer

Defines the application's domain model and maps Java objects to database tables.

Example:

```java
@Entity
@Table(name = "jobs")
public class Job
```

Responsibilities:

* Represents job metadata
* Maps fields to database columns
* Managed by Hibernate

Database table:

```sql
jobs
```

---

## Repository Layer

Provides database access operations.

Example:

```java
public interface JobRepository
        extends JpaRepository<Job, String>
```

Responsibilities:

* Save entities
* Retrieve entities
* Delete entities
* Query entities

Spring Data JPA automatically generates implementations.

---

## Service Layer

Contains business logic.

Example:

```java
public Job createJob(Job job)
```

Responsibilities:

* Coordinate database operations
* Coordinate Quartz scheduling
* Implement business rules

Current workflow:

```text
Save Job
    ↓
Register Quartz Trigger
    ↓
Return Response
```

---

## Controller Layer

Exposes REST endpoints.

Example:

```java
@PostMapping
```

Responsibilities:

* Handle HTTP requests
* Validate request payloads
* Return HTTP responses

Endpoints implemented:

```text
POST   /api/jobs
GET    /api/jobs
```

---

# PostgreSQL Integration

## Docker Setup

PostgreSQL was deployed using Docker.

Container:

```text
postgres-job
```

Database:

```text
jobscheduler
```

Connection:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/jobscheduler
    username: admin
    password: secret
```

---

# Hibernate Integration

Hibernate acts as the JPA implementation.

Workflow:

```text
Repository
    ↓
Spring Data JPA
    ↓
Hibernate
    ↓
PostgreSQL
```

Responsibilities:

* Generate SQL statements
* Map entities to tables
* Manage persistence lifecycle

Example generated SQL:

```sql
INSERT INTO jobs (...)
```

---

# Job Entity Persistence

A Job entity was implemented with fields such as:

```java
id
name
cronExpression
jobType
payload
createdAt
```

Example persisted record:

```sql
SELECT * FROM jobs;
```

Result:

```text
Test Job
Quartz Test
```

---

# Quartz Integration

## Purpose

Quartz provides enterprise-grade scheduling capabilities.

Responsibilities:

* Schedule jobs
* Execute jobs
* Manage cron triggers
* Persist scheduling metadata

---

# Quartz Concepts

## Scheduler

Core engine responsible for managing execution.

```java
Scheduler scheduler;
```

---

## Job

Represents executable code.

Example:

```java
public class SimpleJob implements Job
```

Executed by Quartz whenever a trigger fires.

---

## JobDetail

Represents what should be executed.

Example:

```java
JobBuilder.newJob(SimpleJob.class)
```

---

## Trigger

Represents when execution should occur.

Example:

```java
CronScheduleBuilder.cronSchedule(
    "0/10 * * * * ?"
)
```

---

# Initial Quartz Implementation

A Quartz service was implemented:

```java
QuartzJobService
```

Responsibilities:

* Convert Job entities into Quartz jobs
* Create JobDetail objects
* Create Trigger objects
* Register schedules with Quartz

Workflow:

```text
POST Job
    ↓
Persist Job
    ↓
Create JobDetail
    ↓
Create Trigger
    ↓
Register with Scheduler
```

---

# Simple Quartz Job

Implementation:

```java
public class SimpleJob implements Job
```

Current behavior:

```java
System.out.println(
    "Executing Quartz Job: " + jobId
);
```

Purpose:

* Verify Quartz execution
* Validate cron scheduling
* Validate trigger persistence

---

# Quartz Persistence

## Initial State

Quartz defaulted to:

```text
RAMJobStore
```

Characteristics:

* Schedules stored in memory
* Lost on application restart

Startup log:

```text
Using job-store 'org.quartz.simpl.RAMJobStore'
```

---

## PostgreSQL Quartz Schema

Quartz schema was installed into PostgreSQL.

Created tables:

```text
qrtz_job_details
qrtz_triggers
qrtz_cron_triggers
qrtz_fired_triggers
qrtz_scheduler_state
qrtz_locks
...
```

Verification:

```sql
\dt
```

Displayed all Quartz tables successfully.

---

## JDBC JobStore Configuration

Quartz was configured to use PostgreSQL.

Configuration:

```yaml
spring:
  quartz:
    job-store-type: jdbc
```

Result:

```text
Quartz metadata persisted in PostgreSQL
```

---

# Persistence Verification

A test job was created:

```json
{
  "name": "Quartz Test",
  "cronExpression": "0/10 * * * * ?"
}
```

Execution observed:

```text
Executing Quartz Job:
d6df177d...
```

every 10 seconds.

---

# Trigger Persistence Verification

Quartz tables contained data.

Verification:

```sql
SELECT COUNT(*) FROM qrtz_job_details;
```

Result:

```text
1
```

Verification:

```sql
SELECT COUNT(*) FROM qrtz_triggers;
```

Result:

```text
1
```

---

# Restart Recovery Verification

Application was stopped and restarted.

No new API request was made.

Observed:

```text
Executing Quartz Job:
d6df177d...
```

continued automatically after startup.

Conclusion:

```text
Quartz successfully recovered scheduling metadata
from PostgreSQL.
```

---

# Current System State

Implemented:

* Spring Boot application
* PostgreSQL integration
* Hibernate/JPA persistence
* Job CRUD foundation
* Quartz scheduler
* Quartz PostgreSQL persistence
* Cron-based execution
* Scheduler recovery after restart

Not yet implemented:

* Kafka dispatching
* Redis distributed locking
* Multi-node Quartz clustering
* gRPC communication
* Executor service
* Job status tracking
* Pause/Resume functionality

---

# Next Phase

The next phase will replace the current demonstration job:

```java
System.out.println(...)
```

with actual job execution logic.

Target workflow:

```text
Quartz Trigger
      ↓
Load Job Metadata
      ↓
Dispatch Work
      ↓
Kafka
      ↓
Executor Service
      ↓
Execute Task
```

This will transition the project from a scheduler prototype to a distributed job execution platform.
