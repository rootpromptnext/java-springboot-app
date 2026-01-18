# =========================
# Build stage
# =========================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first (dependency caching)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/java-springboot-app-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
