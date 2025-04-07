# Stage 1: Build the application
FROM openjdk:23-jdk-slim AS builder

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and build scripts
COPY gradlew settings.gradle.kts build.gradle.kts ./

# Copy Gradle wrapper files
COPY gradle gradle/

# Make gradlew executable
RUN chmod +x ./gradlew

# Cache Gradle dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY . .

#Reapply executable permission to gradlew after copying source code
RUN chmod +x ./gradlew

# Build the application
RUN ./gradlew build --no-daemon

# Stage 2: Run the application
FROM openjdk:23-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]