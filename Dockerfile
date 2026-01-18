# Use a lightweight JDK base image
FROM eclipse-temurin:17-jdk

# Set workdir
WORKDIR /app

# Accept JAR filename as build argument
ARG JAR_FILE
COPY target/${JAR_FILE} app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
