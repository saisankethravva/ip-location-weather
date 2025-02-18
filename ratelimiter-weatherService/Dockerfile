# Base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy build artifact into container
COPY build/libs/weather-service-0.0.1-SNAPSHOT.jar weather-service.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/weather-service.jar"]