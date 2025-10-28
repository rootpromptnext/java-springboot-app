# Java Spring Boot CRUD Application

This repository contains a **complete Spring Boot CRUD web application** with:
- REST API for CRUD operations
- Bootstrap-based HTML UI
- Unit and Integration Testing (JUnit + Spring Boot Test)
- Code Coverage via **JaCoCo**
- Maven build support (`mvn clean install`)
- Ready-to-run **executable JAR**

---

## Features

| Feature | Description |
|----------|--------------|
| Framework | Spring Boot 3 (Java 17) |
| Build Tool | Maven |
| Testing | JUnit 5 (Unit + Integration) |
| Code Coverage | JaCoCo integrated |
| Frontend | HTML5 + Bootstrap templates |
| Run Mode | Executable JAR (`java -jar target/java-springboot-app-1.0.0.jar`) |

---

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

---

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

---

## API Endpoints

| Method | Endpoint              | Description              |
| ------ | --------------------- | ------------------------ |
| GET    | `/api/employees`      | Get all employees        |
| GET    | `/api/employees/{id}` | Get employee by ID       |
| POST   | `/api/employees`      | Create new employee      |
| PUT    | `/api/employees/{id}` | Update existing employee |
| DELETE | `/api/employees/{id}` | Delete employee          |

---

## Test CRUD via `curl`

```bash
# Create employee
curl -X POST -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Alice","department":"HR"}' \
  http://localhost:8080/api/employees

# List all
curl http://localhost:8080/api/employees

# Get by ID
curl http://localhost:8080/api/employees/1

# Update employee
curl -X PUT -H "Content-Type: application/json" \
  -d '{"name":"Alice Smith","department":"Finance"}' \
  http://localhost:8080/api/employees/1

# Delete employee
curl -X DELETE http://localhost:8080/api/employees/1
```

---

## Testing Summary

| Type              | Framework                | Command                         |
| ----------------- | ------------------------ | ------------------------------- |
| Unit Tests        | JUnit + Spring Boot Test | `mvn test`                      |
| Integration Tests | JUnit + MockMvc          | `mvn verify`                    |
| Coverage          | JaCoCo                   | `target/site/jacoco/index.html` |

