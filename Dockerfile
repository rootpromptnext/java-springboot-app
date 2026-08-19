# =========================
# Build stage
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN mvn -B clean package -DskipTests

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy generated JAR
COPY --from=build /app/target/java-springboot-app-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
