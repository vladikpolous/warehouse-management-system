# Stage 1: Build the application
FROM openjdk:23-jdk-slim AS builder

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and build scripts
COPY gradlew settings.gradle build.gradle ./

# Copy Gradle wrapper files
COPY gradle gradle/

# Cache Gradle dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY . .

# Build the application (generate the JAR file)
RUN ./gradlew build --no-daemon

# Stage 2: Run the application
FROM openjdk:23-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
