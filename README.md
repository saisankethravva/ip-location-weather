
# Project Overview

This project implements two services:
1. Weather API Service: Provides weather information based on IP addresses and utilizes Redis for caching.
2. Rate Limiter Service: Protects the Weather API by applying rate limits via Spring Cloud Gateway.

# System Architecture Overview

Sequence Flow:

1. Postman Client sends request to Rate Limiter Service.
2. Rate Limiter Service checks Redis for rate limits.
3. If passed, forwards request to Weather API Service.
4. Weather API Service extracts client IP.
5. Checks Redis for cached weather data (TTL: 10 minutes).
6. If not cached:
Calls third-party IP location API.
Uses city to call third-party weather API.
Stores results in Redis.

![Screenshot 2025-02-17 at 11 44 05 PM](https://github.com/user-attachments/assets/686dc7dd-5154-4488-a0ef-338c8a615bf1)

# Prerequisites
1. Java 17 or higher
2. Docker (for containerization)
3. Redis (for caching)
4. Maven or Gradle (for building)

# 🛠 Installing Redis Locally

## 💻 MacOS:

```
brew install redis
brew services start redis
redis-cli ping  # Should return 'PONG'
```

## 🪟 Windows:

```
choco install redis
redis-server
redis-cli ping  # Should return 'PONG'
```

## 🚀 Running Services

1. Clone the Repositories:

```
https://github.com/saisankethravva/location-weather.git
open weather-service and ratelimiter-weatherService in separate windows in IntelliJ

```
2. Run Weather Service (Gradle):

```
./gradlew clean build
./gradlew bootRun
```
3. Run Rate Limiter Service (Maven):
```
mvn clean install
mvn spring-boot:run
```


Test with correct IP:


