# Java Spring Boot DevOps Application

<p align="center">
  <img src="ci-cd-iac-project-overview.png" alt="CI/CD and Infrastructure Architecture" width="1200">
</p>

<p align="center">

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Maven](https://img.shields.io/badge/Maven-Build-red)
![Docker](https://img.shields.io/badge/Docker-Container-blue)
![Kubernetes](https://img.shields.io/badge/Kubernetes-Deployment-326CE5)
![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD-D24939)
![Prometheus](https://img.shields.io/badge/Prometheus-Monitoring-E6522C)
![Grafana](https://img.shields.io/badge/Grafana-Dashboards-F46800)

</p>

# Project Overview

This repository contains a complete **Java Spring Boot Employee Management application** designed as a practical **DevOps / DevSecOps / Kubernetes / Observability lab**.

The application is progressively integrated with modern DevOps tools and practices:

```text
Java Spring Boot
       │
       ├── Maven
       ├── JUnit
       ├── JaCoCo
       │
       ├── Docker
       │
       ├── Kubernetes
       │
       ├── Jenkins CI/CD
       │
       ├── DevSecOps
       │
       └── Observability
             ├── Spring Boot Actuator
             ├── Micrometer
             ├── Prometheus
             └── Grafana
````

The same application can be used to demonstrate:

* Application development
* Maven build automation
* Automated testing
* Code coverage
* Docker containerization
* Kubernetes deployment
* Jenkins CI/CD
* DevSecOps pipelines
* Prometheus monitoring
* Grafana dashboards
* MicroK8s deployment
* Amazon EKS deployment
* GitHub Actions self-hosted runners

---

# Application Architecture

The application is a simple Employee Management application.

```text
                    Spring Boot Application
                            │
                ┌───────────┴───────────┐
                │                       │
             Web UI                 REST API
                │                       │
          Thymeleaf +             /api/employees
            Bootstrap                   │
                │                       │
                └───────────┬───────────┘
                            │
                     EmployeeController
                            │
                     EmployeeService
                            │
                     In-Memory Map
```

Current application functionality:

* View employees
* Add employee
* Delete employee
* REST API
* HTML UI using Thymeleaf
* Bootstrap frontend
* Unit testing
* Integration testing
* REST CRUD operations

# REST API

The application exposes a RESTful Employee API:

```text
/api/employees
```

Supported operations:

```text
GET     /api/employees
GET     /api/employees/{id}
POST    /api/employees
PUT     /api/employees/{id}
DELETE  /api/employees/{id}
```

The API uses JSON request and response bodies.

Example Employee:

```json
{
  "id": 1,
  "name": "Alice",
  "role": "DevOps Engineer"
}
```

# Technology Stack

| Category                | Technology                                           |
| ----------------------- | ---------------------------------------------------- |
| Language                | Java 21                                              |
| Framework               | Spring Boot                                          |
| Build                   | Maven                                                |
| Frontend                | Thymeleaf + Bootstrap                                |
| Testing                 | JUnit 5                                              |
| Code Coverage           | JaCoCo                                               |
| Container               | Docker                                               |
| Container Orchestration | Kubernetes                                           |
| Local Kubernetes        | MicroK8s                                             |
| Cloud Kubernetes        | Amazon EKS                                           |
| CI/CD                   | Jenkins                                              |
| DevSecOps               | SonarQube/SonarCloud, Trivy and other pipeline tools |
| Monitoring              | Spring Boot Actuator                                 |
| Metrics                 | Micrometer                                           |
| Metrics Storage         | Prometheus                                           |
| Visualization           | Grafana                                              |
| Automation              | GitHub Actions                                       |

# Repository Structure

```text
java-springboot-app/
│
├── Dockerfile
│
├── pom.xml
│
├── README.md
├── monitoring.md
│
├── ci-cd-iac-project-overview.png
│
├── deployment.yaml
│
├── Jenkinsfile-app-deploy-eks
├── Jenkinsfile-app-deploy-microk8s
├── Jenkinsfile-devsecops
│
├── monitoring/
│   ├── docker-compose.yaml
│   │
│   └── prometheus/
│       └── prometheus.yml
│
└── src/
    │
    ├── main/
    │   │
    │   ├── java/
    │   │   └── com/example/demo/
    │   │       │
    │   │       ├── DemoApplication.java
    │   │       │
    │   │       ├── controller/
    │   │       │   └── EmployeeController.java
    │   │       │
    │   │       ├── model/
    │   │       │   └── Employee.java
    │   │       │
    │   │       └── service/
    │   │           └── EmployeeService.java
    │   │
    │   └── resources/
    │       │
    │       ├── application.properties
    │       │
    │       └── templates/
    │           ├── index.html
    │           └── form.html
    │
    └── test/
        └── java/
            └── com/example/demo/
                ├── UnitTest.java
                └── IntegrationTest.java
```

> `target/` is generated by Maven and should not be committed to Git.

# 1. Prerequisites

Install:

* Java 21
* Maven
* Git
* Docker
* Docker Compose
* kubectl
* Kubernetes cluster if deploying to Kubernetes

Install Java:
```bash
sudo apt update
sudo apt install -y openjdk-21-jdk
```

Verify Java:

```bash
java -version
```

Expected:

```text
openjdk version "21..."
```

Verify Maven:

```bash
mvn -version
```

# 2. Build the Application

Clone the repository:

```bash
git clone https://github.com/rootpromptnext/java-springboot-app.git
cd java-springboot-app
```

## Manual Compile, Package and Test Workflow

Before running Docker, Kubernetes, Jenkins or any CI/CD pipeline, you can manually verify the application locally.

### Step 1: Compile the Source Code

```bash
mvn compile
```

This verifies that the Java source code can be compiled successfully.

### Step 2: Run Tests

```bash
mvn test
```

This executes the configured Maven test lifecycle.

### Step 3: Package the Application

```bash
mvn package
```

The generated JAR will be available under:

```text
target/java-springboot-app-1.0.0.jar
```

### Step 4: Clean Build

For a clean build:

```bash
mvn clean package
```

This removes the previous `target/` directory, compiles the source code, runs tests and creates the Spring Boot JAR.

### Step 5: Complete Verification

```bash
mvn clean verify
```

This runs the complete Maven verification lifecycle, including the configured JaCoCo reporting.

### Step 6: Run the Generated JAR

```bash
java -jar target/java-springboot-app-1.0.0.jar
```

Application:

```text
http://localhost:8080
```

### Recommended Manual Workflow

```text
Source Code
    │
    ▼
mvn compile
    │
    ▼
mvn test
    │
    ▼
mvn package
    │
    ▼
mvn clean verify
    │
    ▼
Spring Boot JAR
    │
    ▼
java -jar
```

Build:

```bash
mvn clean package
```

Run tests:

```bash
mvn test
```

Run complete verification:

```bash
mvn clean verify
```

# 3. Testing

The project contains:

### Unit Test

```text
src/test/java/com/example/demo/UnitTest.java
```

Run:

```bash
mvn test
```

### Integration Test

```text
src/test/java/com/example/demo/IntegrationTest.java
```

Run:

```bash
mvn verify
```

> The project currently contains `IntegrationTest.java`. The Maven Failsafe configuration is configured for `*IT.java` naming, so the integration test should be verified against the actual Maven test lifecycle if Failsafe-specific execution is required.

# 4. JaCoCo Code Coverage

JaCoCo is integrated into Maven.

Run:

```bash
mvn clean verify
```

Coverage report:

```text
target/site/jacoco/index.html
```

Open it in a browser.

# 5. Run Spring Boot Application

Run using Maven:

```bash
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
```

```bash
java -jar target/java-springboot-app-1.0.0.jar
```

Application:

```text
http://localhost:8080
```

# 6. Application Endpoints

## Employee UI

```text
GET /
```

## Add Employee Form

```text
GET /form
```

## REST API

Base path:

```text
/api/employees
```

### Get All Employees

```text
GET /api/employees
```

### Get Employee by ID

```text
GET /api/employees/{id}
```

### Create Employee

```text
POST /api/employees
```

### Update Employee

```text
PUT /api/employees/{id}
```

### Delete Employee

```text
DELETE /api/employees/{id}
```

---

# 7. REST API CRUD Testing

Make sure the application is running:

```bash
mvn spring-boot:run
```

Or:

```bash
java -jar target/java-springboot-app-1.0.0.jar
```

## 7.1 Create Employee

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Alice","role":"DevOps Engineer"}'
```

Expected response:

```json
{
  "id": 1,
  "name": "Alice",
  "role": "DevOps Engineer"
}
```

Create another employee:

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"id":2,"name":"Bob","role":"Cloud Engineer"}'
```

## 7.2 Get All Employees

```bash
curl http://localhost:8080/api/employees
```

Example response:

```json
[
  {
    "id": 1,
    "name": "Alice",
    "role": "DevOps Engineer"
  },
  {
    "id": 2,
    "name": "Bob",
    "role": "Cloud Engineer"
  }
]
```

## 7.3 Get Employee by ID

```bash
curl http://localhost:8080/api/employees/1
```

Expected:

```json
{
  "id": 1,
  "name": "Alice",
  "role": "DevOps Engineer"
}
```

## 7.4 Update Employee

```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Updated","role":"Senior DevOps Engineer"}'
```

Expected:

```json
{
  "id": 1,
  "name": "Alice Updated",
  "role": "Senior DevOps Engineer"
}
```

The ID remains `1` because the REST endpoint identifies the employee using:

```text
PUT /api/employees/1
```

## 7.5 Delete Employee

```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

Expected HTTP status:

```text
204 No Content
```

## 7.6 Verify Deletion

```bash
curl http://localhost:8080/api/employees/1
```

Expected HTTP status:

```text
404 Not Found
```

## 7.7 Verify All Employees

```bash
curl http://localhost:8080/api/employees
```

---

# 8.1 Docker

The project contains a multi-stage Dockerfile.

```text
Dockerfile
```

The Dockerfile uses:

```text
Maven + JDK
      │
      │ Build
      ▼
Spring Boot JAR
      │
      ▼
Java Runtime Image
```

Build:

```bash
docker build -t java-springboot-app .
```

Run:

```bash
docker run -d \
  --name java-springboot-app \
  -p 8080:8080 \
  java-springboot-app
```

Check:

```bash
docker ps
```

Application:

```text
http://localhost:8080
```

REST API:

```bash
curl http://localhost:8080/api/employees
```

Stop:

```bash
docker stop java-springboot-app
```

Remove:

```bash
docker rm java-springboot-app
```

Yes — **we should explicitly document the Docker Compose deployment of all 3 containers**. Your existing README mentions it, but it doesn't clearly explain that the Compose stack starts:

```text
Spring Boot Application :8080
        │
        ▼
   Prometheus :9090
        │
        ▼
    Grafana :3000
```

And your actual `monitoring/docker-compose.yaml` contains all three services.

# 8.2 Docker Compose - Spring Boot + Prometheus + Grafana

The repository includes a Docker Compose configuration that runs the complete application monitoring stack.

The stack contains three containers:

```text
                    Docker Compose
                         │
          ┌──────────────┼──────────────┐
          │              │              │
          ▼              ▼              ▼
   Spring Boot       Prometheus      Grafana
      :8080             :9090          :3000
          │               ▲
          │               │
          └───────────────┘
           /actuator/prometheus
````

Compose configuration:

```text
monitoring/docker-compose.yaml
```

Prometheus configuration:

```text
monitoring/prometheus/prometheus.yml
```

## Start the Complete Stack

From the project root:

```bash
docker compose -f monitoring/docker-compose.yaml up -d --build
```

This builds and starts:

```text
springboot-app
prometheus
grafana
```

## Check Running Containers

```bash
docker ps
```

You should see the three containers running.

## Check Spring Boot Application

Open:

```text
http://localhost:8080
```

Test the REST API:

```bash
curl http://localhost:8080/api/employees
```

Test Actuator:

```bash
curl http://localhost:8080/actuator/health
```

Test Prometheus metrics:

```bash
curl http://localhost:8080/actuator/prometheus
```

## Check Prometheus

Open:

```text
http://localhost:9090
```

Check Prometheus targets:

```bash
curl http://localhost:9090/api/v1/targets
```

Or:

```bash
curl -s http://localhost:9090/api/v1/targets | python3 -m json.tool
```

The Spring Boot target should be:

```text
UP
```

Prometheus scrapes the Spring Boot application using:

```text
http://springboot-app:8080/actuator/prometheus
```

## Check Grafana

Open:

```text
http://localhost:3000
```

Grafana uses Prometheus as its datasource.

Inside the Grafana container, configure the Prometheus datasource as:

```text
http://prometheus:9090
```

Example PromQL queries:

```promql
jvm_memory_used_bytes
```

```promql
jvm_threads_live_threads
```

```promql
process_cpu_usage
```

```promql
http_server_requests_seconds_count
```

## Check Container Logs

Spring Boot:

```bash
docker logs springboot-app
```

Prometheus:

```bash
docker logs prometheus
```

Grafana:

```bash
docker logs grafana
```

Follow Spring Boot logs:

```bash
docker logs -f springboot-app
```

## Stop the Stack

```bash
docker compose -f monitoring/docker-compose.yaml down
```

## Stop and Remove Volumes

```bash
docker compose -f monitoring/docker-compose.yaml down -v
```

## Docker Compose Verification Flow

```text
docker compose up
        │
        ├── Spring Boot
        │      │
        │      └── :8080
        │
        ├── Prometheus
        │      │
        │      └── :9090
        │
        └── Grafana
               │
               └── :3000

Spring Boot
     │
     │ /actuator/prometheus
     ▼
Prometheus
     │
     │ PromQL
     ▼
Grafana
```

# 9. Kubernetes Deployment

Kubernetes manifest:

```text
deployment.yaml
```

Apply:

```bash
kubectl apply -f deployment.yaml
```

Check:

```bash
kubectl get pods
```

```bash
kubectl get svc
```

```bash
kubectl get deployment
```

Check everything:

```bash
kubectl get all
```

# 10. MicroK8s Deployment

The application can be deployed to MicroK8s.

Verify:

```bash
microk8s status
```

Check nodes:

```bash
kubectl get nodes
```

Deploy:

```bash
kubectl apply -f deployment.yaml
```

Verify:

```bash
kubectl get pods
```

```bash
kubectl get svc
```

# 11. Amazon EKS Deployment

The repository also contains a Jenkins pipeline for EKS:

```text
Jenkinsfile-app-deploy-eks
```

The pipeline can be used to automate:

```text
Git
 │
 ▼
Jenkins
 │
 ├── Build
 ├── Test
 ├── Docker Build
 ├── Docker Push
 └── Deploy
       │
       ▼
      EKS
```

# 12. Jenkins CI/CD

Jenkins pipelines included in this repository:

```text
Jenkinsfile-app-deploy-eks
Jenkinsfile-app-deploy-microk8s
Jenkinsfile-devsecops
```

### EKS Deployment

```text
Jenkinsfile-app-deploy-eks
```

### MicroK8s Deployment

```text
Jenkinsfile-app-deploy-microk8s
```

### DevSecOps Pipeline

```text
Jenkinsfile-devsecops
```

Typical pipeline:

```text
Developer
    │
    ▼
 Git Repository
    │
    ▼
  Jenkins
    │
    ├── Checkout
    │
    ├── Maven Build
    │
    ├── Unit Tests
    │
    ├── Integration Tests
    │
    ├── JaCoCo
    │
    ├── SAST
    │
    ├── Dependency/SCA Scan
    │
    ├── Container Build
    │
    ├── Container Security Scan
    │
    ├── Push Image
    │
    └── Kubernetes Deployment
```

# 13. DevSecOps

The repository contains:

```text
Jenkinsfile-devsecops
```

The pipeline can be extended with:

* SAST
* SCA
* Secret scanning
* Container scanning
* Quality gates
* Security gates
* Artifact management

Typical tools:

```text
Source Code
     │
     ▼
   SAST
     │
     ▼
    SCA
     │
     ▼
Secret Scan
     │
     ▼
Docker Build
     │
     ▼
Container Scan
     │
     ▼
Registry
     │
     ▼
Kubernetes
```

# 14. Prometheus Monitoring

Spring Boot monitoring is implemented using:

```text
Spring Boot Actuator
        │
        ▼
     Micrometer
        │
        ▼
 Prometheus Registry
        │
        ▼
/actuator/prometheus
```

The application exposes:

```text
/actuator/health
/actuator/prometheus
```

Test health:

```bash
curl http://localhost:8080/actuator/health
```

Expected:

```json
{"status":"UP"}
```

Test Prometheus metrics:

```bash
curl http://localhost:8080/actuator/prometheus
```

Check JVM memory:

```bash
curl http://localhost:8080/actuator/prometheus | grep jvm_memory_used_bytes
```

Example metrics include:

```text
jvm_memory_used_bytes
jvm_memory_max_bytes
jvm_threads_live_threads
jvm_gc_pause_seconds
process_cpu_usage
process_uptime_seconds
http_server_requests_seconds
disk_free_bytes
disk_total_bytes
```

Detailed monitoring instructions:

# 15. Prometheus + Grafana

```text
                Docker Compose
                     │
        ┌────────────┼────────────┐
        │            │            │
        ▼            ▼            ▼
 Spring Boot    Prometheus      Grafana
    :8080          :9090          :3000
        │            ▲            │
        │            │            │
        └────────────┘            │
          /actuator/prometheus    │
                                  │
                             PromQL Queries
```

Monitoring files:

```text
monitoring/
├── docker-compose.yaml
└── prometheus/
    └── prometheus.yml
```

Start the complete monitoring stack:

```bash
docker compose -f monitoring/docker-compose.yaml up -d --build
```

Check containers:

```bash
docker ps
```

Expected:

```text
springboot-app
prometheus
grafana
```

# 16. Test Prometheus

Prometheus:

```text
http://localhost:9090
```

Check targets:

```bash
curl http://localhost:9090/api/v1/targets
```

Or:

```bash
curl -s http://localhost:9090/api/v1/targets | python3 -m json.tool
```

The Spring Boot target should show:

```text
health: up
```

Query JVM memory:

```bash
curl -s \
'http://localhost:9090/api/v1/query?query=jvm_memory_used_bytes' \
| python3 -m json.tool
```

# 17. Grafana

Grafana:

```text
http://localhost:3000
```

Add Prometheus as a datasource.

Inside Grafana use:

```text
http://prometheus:9090
```

Example PromQL queries:

```promql
jvm_memory_used_bytes
```

```promql
jvm_threads_live_threads
```

```promql
process_cpu_usage
```

```promql
http_server_requests_seconds_count
```

# 18. Observability Roadmap

The monitoring implementation starts with traditional Spring Boot + Micrometer monitoring.

Current:

```text
Spring Boot
     │
     ▼
Actuator
     │
     ▼
Micrometer
     │
     ▼
Prometheus
     │
     ▼
Grafana
```

The next observability stage can introduce OpenTelemetry:

```text
Spring Boot
     │
     ▼
OpenTelemetry
     │
     ├── Metrics
     ├── Traces
     └── Logs
          │
          ▼
   Observability Backend
```

This repository can therefore be used to demonstrate:

* Prometheus without OpenTelemetry
* Prometheus with OpenTelemetry
* Metrics
* Traces
* Logs
* Grafana dashboards
* Distributed observability

# 19. GitHub Actions Self-Hosted Runner

The repository can also be deployed using a GitHub Actions self-hosted runner.

Runner architecture:

```text
GitHub
   │
   ▼
GitHub Actions
   │
   ▼
Self-Hosted Runner
   │
   ├── Docker
   ├── kubectl
   └── MicroK8s
          │
          ▼
     Kubernetes
```

The runner should run as a non-root user.

Example:

```bash
sudo useradd -m -s /bin/bash github
```

Add Docker access:

```bash
sudo usermod -aG docker github
```

For MicroK8s:

```bash
sudo usermod -aG microk8s github
```

# Quick Verification

After cloning the repository:

```bash
cd java-springboot-app
```

Compile:

```bash
mvn compile
```

Run tests:

```bash
mvn test
```

Package:

```bash
mvn package
```

Complete verification:

```bash
mvn clean verify
```

Run:

```bash
mvn spring-boot:run
```

Test Actuator:

```bash
curl http://localhost:8080/actuator/health
```

Test REST API:

```bash
curl http://localhost:8080/api/employees
```

Create employee:

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Alice","role":"DevOps Engineer"}'
```

Get employee:

```bash
curl http://localhost:8080/api/employees/1
```

Update employee:

```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Updated","role":"Senior DevOps Engineer"}'
```

Delete employee:

```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

Verify deletion:

```bash
curl http://localhost:8080/api/employees/1
```

Build Docker:

```bash
docker build -t java-springboot-app .
```

Start monitoring:

```bash
docker compose -f monitoring/docker-compose.yaml up -d --build
```

Check containers:

```bash
docker ps
```

Check Prometheus:

```text
http://localhost:9090
```

Check Grafana:

```text
http://localhost:3000
```

# Stop Monitoring Stack

```bash
docker compose -f monitoring/docker-compose.yaml down
```

Remove volumes if required:

```bash
docker compose -f monitoring/docker-compose.yaml down -v
```

# Learning Path

This repository is designed to demonstrate the following progression:

```text
01. Java / Spring Boot
          │
          ▼
02. Maven
          │
          ▼
03. JUnit + Integration Testing
          │
          ▼
04. JaCoCo
          │
          ▼
05. REST API
          │
          ▼
06. Docker
          │
          ▼
07. Kubernetes
          │
          ▼
08. Jenkins CI/CD
          │
          ▼
09. DevSecOps
          │
          ▼
10. Prometheus
          │
          ▼
11. Grafana
          │
          ▼
```

# Project Goals

The goal of this repository is not only to demonstrate a Spring Boot application, but to show how the **same application moves through a complete DevOps lifecycle**:

```text
Code
 │
 ▼
Build
 │
 ▼
Test
 │
 ▼
Quality
 │
 ▼
Security
 │
 ▼
Container
 │
 ▼
Registry
 │
 ▼
Kubernetes
 │
 ▼
CI/CD
 │
 ▼
Monitoring
```

# 👨‍💻 Author

**Prayag Sangode**
Senior Technical Architect

DevOps | AWS | Kubernetes | CI/CD | DevSecOps | Observability | AIOps

# ⭐ Repository

If this project is useful for your DevOps learning journey, consider starring the repository.

```text
https://github.com/rootpromptnext/java-springboot-app
```
