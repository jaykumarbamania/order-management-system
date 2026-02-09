# Order Management System

A **production-grade & backend-focused Order Management System** built using **Spring Boot, PostgreSQL, Docker, and AWS**.  
This project is designed to demonstrate **real-world backend engineering**, **event-driven workflows**, and **production-style cloud deployment**.

---

## What this project does

- Manages the **complete lifecycle of an order**
- Uses **domain events** to drive order state transitions
- Ensures **reliable event publishing** using the **Outbox pattern**
- Handles **retries and idempotency** for fault tolerance
- Runs **locally using Docker** and **on AWS using EC2, ECR, and RDS**

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA / Hibernate**
- **PostgreSQL**
- **Docker**
- **AWS**
    - EC2 (compute)
    - ECR (container registry)
    - RDS (managed PostgreSQL)
    - IAM (access management)

---

## Core Backend Concepts Implemented

- **Event-driven architecture** for order processing
- **Outbox pattern** to avoid dual-write problems
- **Transactional event listeners** with isolated transactions
- **Idempotent processing** to safely handle duplicate events
- **Failure-aware design** with retry-friendly workflows

---

## Order Lifecycle (High Level)

1. Order is **created**
2. Inventory reservation is attempted
3. Payment is processed
4. Order transitions to **CONFIRMED** or **CANCELLED**
5. Notification event is generated for downstream processing

---

## Local Setup (Docker)

### Start PostgreSQL

```bash
docker network create oms-network

docker run -d \
  --name oms-postgres \
  --network oms-network \
  -e POSTGRES_DB=oms \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15
```

### Run Order Service
```bash 
docker run -d \
--name oms-order-service \
--network oms-network \
-p 8080:8080 \
-e DB_URL=jdbc:postgresql://oms-postgres:5432/oms \
-e DB_USERNAME=postgres \
-e DB_PASSWORD=postgres \
oms/order-service:latest
```


---

## Docker Image

- Uses a **multi-stage Docker build**
- Keeps the **runtime image small and production-ready**
- The **same Docker image** runs both **locally** and **on AWS**

---

## AWS Deployment Overview

- **Docker images** stored in **Amazon ECR**
- **Application** deployed on **EC2**
- **Database** hosted on **Amazon RDS (PostgreSQL)**
- **Access** controlled using **IAM**

---

## Current Status

- **Order Service** implemented
- **Event-driven order flow** working
- **Deployed on AWS** (EC2 + RDS)
- **Notification module** in progress

---

## Planned Enhancements

- Complete **Notification processing**
- Migrate deployment to **ECS Fargate**
- Replace access keys with **IAM Roles**
- Introduce **asynchronous messaging** (SQS / Kafka)
- Add **monitoring and centralized logging**

---

## Author

**Jaykumar Bamania**
