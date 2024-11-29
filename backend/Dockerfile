# Use a lightweight JDK 23 image
FROM openjdk:23-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the build output (JAR file) from Gradle
COPY build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the backend port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
