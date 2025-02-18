
# Project Overview

This project implements two services:
1. Weather API Service: Provides weather information based on IP addresses and utilizes Redis for caching.
2. Rate Limiter Service: Protects the Weather API by applying rate limits via Spring Cloud Gateway.

# System Architecture Overview

Components:
1. API Gateway (Rate Limiter): Routes requests and enforces rate limits.
2. Weather API Service: Retrieves and processes weather data.
3. Redis Cache: Caches weather responses to minimize external API calls.
4. Client: Makes requests through the API Gateway.

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


