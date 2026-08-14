# Project Overview

<p align="center">
  <img src="ci-cd-iac-project-overview.png" alt="CI/CD and Terraform Infrastructure Architecture" width="1200">
</p>

# Java Spring Boot Application

This repository contains a **complete a Spring Boot web application** with:
- REST API for CRUD operations
- Bootstrap-based HTML UI
- Unit and Integration Testing (JUnit + Spring Boot Test)
- Code Coverage via **JaCoCo**
- Maven build support (`mvn clean install`)

  


## Features

| Feature | Description |
|----------|--------------|
| Framework | Spring Boot 3 (Java 17) |
| Build Tool | Maven |
| Testing | JUnit 5 (Unit + Integration) |
| Code Coverage | JaCoCo integrated |
| Frontend | HTML5 + Bootstrap templates |
| Run Mode | Executable JAR (`java -jar target/java-springboot-app-1.0.0.jar`) |


## Project Structure

```

java-springboot-app/
├── pom.xml
├── src
│   ├── main
│   │   ├── java/com/example/demo
│   │   │   ├── DemoApplication.java
│   │   │   ├── controller/EmployeeController.java
│   │   │   ├── model/Employee.java
│   │   │   └── service/EmployeeService.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── index.html
│   │           └── form.html
│   └── test/java/com/example/demo
│       ├── UnitTest.java
│       └── IntegrationTest.java
└── target/

````

## Getting Started

### Clone the repository
```bash
git clone https://github.com/rootpromptnext/java-springboot-app.git
cd java-springboot-app
````

### Build and run tests

```bash
mvn clean test
```

This runs **both unit and integration tests** and generates a **JaCoCo coverage report** under:

```
target/site/jacoco/index.html
```

### Package the app

```bash
mvn clean package
```

This generates an **executable JAR** at:

```
target/java-springboot-app-1.0.0.jar
```

### Run the application

```bash
java -jar target/java-springboot-app-1.0.0.jar
```

App runs on:
[http://localhost:8080](http://localhost:8080)

## Testing Summary

| Type              | Framework                | Command                         |
| ----------------- | ------------------------ | ------------------------------- |
| Unit Tests        | JUnit + Spring Boot Test | `mvn test`                      |
| Integration Tests | JUnit + MockMvc          | `mvn verify`                    |
| Coverage          | JaCoCo                   | `target/site/jacoco/index.html` |


# Java Spring Boot Employee App (Dockerized)

This is a simple Spring Boot application containerized using Docker.
The Docker image builds the application JAR inside Docker and runs it
using a lightweight Java runtime image.

## Install docker
```bash
echo "Installing Docker..."
sudo apt-get update -y
sudo apt-get install -y ca-certificates curl gnupg lsb-release

sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update -y
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

echo "Adding Jenkins user to docker group..."
sudo usermod -aG docker jenkins
sudo usermod -aG docker ubuntu
sudo usermod -aG docker $USER
```
## Build Docker Image

Run this command from the project root:

```bash
docker build -t java-springboot-app .
````

This will:

* Use Maven inside Docker to build the Spring Boot JAR
* Create a runnable Docker image


## Run the Application

```bash
docker run -d \
  --name java-springboot-app \
  -p 8080:8080 \
  java-springboot-app
```

## Access the Application

Open in browser:

```
http://localhost:8080
```

or (from another machine):

```
http://<VM-IP>:8080
```

## Dockerfile Explanation

* **Build stage**

  * Uses Maven + JDK to compile the Spring Boot application
  * Ensures consistent and reproducible builds

* **Runtime stage**

  * Uses a lightweight JRE image
  * Copies only the final JAR
  * Reduces image size and attack surface

This approach follows Docker and DevOps best practices using
multi-stage builds.


##  Stop & Remove Container

```bash
docker stop java-springboot-app
docker rm java-springboot-app
```

# Deploy Java Spring Boot App on Kubernetes

The steps work with **MicroK8s, Minikube, Vanilla Kubernetes, or Amazon EKS**.

## Docker Image Details

* **Image:** `rootpromptnext/java-springboot-app:v1`
* **Application:** Java Spring Boot
* **Port (assumed):** `8080`

> If your Spring Boot app uses a different port, update the Kubernetes manifests accordingly.


## Prerequisites

Make sure you have:

* Docker (for local testing)
* One of the following Kubernetes clusters:

  * MicroK8s
  * Minikube
  * Vanilla Kubernetes (kubeadm)
  * Amazon EKS
* `kubectl` installed and configured

## Microk8s installation

```bash
#!/bin/bash
sudo apt-get update -y
sudo apt-get upgrade -y

sudo apt-get install -y snapd

sudo snap install microk8s --classic --channel=1.28/stable

sudo usermod -aG microk8s $USER
sudo chown -f -R $USER ~/.kube

newgrp microk8s

microk8s status --wait-ready

microk8s enable dns dashboard storage ingress

sudo snap alias microk8s.kubectl kubectl

microk8s kubectl get nodes

echo "MicroK8s installation completed successfully!"
```

## Kubectl installation
```bash
curl -O https://s3.us-west-2.amazonaws.com/amazon-eks/1.30.0/2024-05-12/bin/linux/amd64/kubectl
chmod +x kubectl
sudo mv kubectl /usr/local/bin/
which kubectl
kubectl version --client || true
```
Verify cluster access:

```bash
kubectl get nodes
```

Apply:

```bash
kubectl apply -f deployment.yaml
```

## Access the Application
```
http://<VM-IP>:<port> or fqdn
```

## Verify Deployment

```bash
kubectl get all
```
# Add github actions self hosted runner

## Create Runner User

GitHub runners **must not run as root**.

```bash
sudo useradd -m -s /bin/bash github
sudo passwd github
````

Add required groups:

```bash
sudo usermod -aG docker github
sudo usermod -aG microk8s github
```

---

##  Prepare Runner Directory

```bash
sudo mkdir -p /opt/actions-runner
sudo chown -R github:github /opt/actions-runner
```

Switch to runner user:

```bash
sudo su - github
cd /opt/actions-runner
```

---

## Download GitHub Actions Runner

```bash
curl -o actions-runner-linux-x64-2.331.0.tar.gz -L \
https://github.com/actions/runner/releases/download/v2.331.0/actions-runner-linux-x64-2.331.0.tar.gz
```

Verify checksum:

```bash
echo "5fcc01bd546ba5c3f1291c2803658ebd3cedb3836489eda3be357d41bfcf28a7  actions-runner-linux-x64-2.331.0.tar.gz" \
| shasum -a 256 -c
```

Extract:

```bash
tar xzf actions-runner-linux-x64-2.331.0.tar.gz
```

---

## Configure Runner (One-Time)

Get token from:

```
GitHub Repo → Settings → Actions → Runners → New self-hosted runner
```

Configure runner:

```bash
./config.sh \
  --url https://github.com/rootpromptnext/java-springboot-app \
  --token <RUNNER_TOKEN>
```

> Tokens are **one-time use** and expire quickly.

---

## Test Runner (Manual)

```bash
./run.sh
```

Once confirmed working, **stop it** and proceed to systemd setup.

---

## systemd Service (Auto-Start)

Exit github user and switch back to root:

```bash
exit
```

Create service file:

```bash
sudo nano /etc/systemd/system/github-actions-runner.service
```

Paste:

```ini
[Unit]
Description=GitHub Actions Runner
After=network.target docker.service
Wants=docker.service

[Service]
Type=simple
User=github
WorkingDirectory=/opt/actions-runner
ExecStart=/opt/actions-runner/run.sh
Restart=always
RestartSec=10
KillMode=process
Environment="HOME=/home/github"

[Install]
WantedBy=multi-user.target
```

Enable & start:

```bash
sudo systemctl daemon-reexec
sudo systemctl daemon-reload
sudo systemctl enable github-actions-runner
sudo systemctl start github-actions-runner
```

Check status:

```bash
sudo systemctl status github-actions-runner
```

---

## Configure kubectl for MicroK8s

Login as github user:

```bash
sudo su - github
```

Create kubeconfig:

```bash
microk8s config > ~/.kube/config
```

Verify access:

```bash
kubectl get nodes
```

Confirm groups:

```bash
id
```

Expected output example:

```text
uid=1001(github) gid=1002(github)
groups=github,docker,microk8s
```

---

## Use Runner in GitHub Actions

In your workflow file:

```yaml
runs-on: self-hosted
```

Example:

```yaml
jobs:
  deploy:
    runs-on: self-hosted
    steps:
      - uses: actions/checkout@v4
      - run: kubectl get nodes
```

