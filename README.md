
# 📌Project Overview

This project implements two services:
1. Weather API Service: Provides weather information based on IP addresses and utilizes Redis for caching.
2. Rate Limiter Service: Protects the Weather API by applying rate limits via Spring Cloud Gateway.

# 🗂 System Architecture Overview

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

# ⚙️ Prerequisites
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


Postman Client Request with IP: Displays request and response when providing an IP


<img width="630" alt="Screenshot 2025-02-17 at 11 56 07 PM" src="https://github.com/user-attachments/assets/2c17e865-30b7-46a0-a920-c48f66b1286c" />


Postman Client Request without IP: Shows how the system resolves the IP automatically.

<img width="1196" alt="Screenshot 2025-02-17 at 11 56 21 PM" src="https://github.com/user-attachments/assets/bd061196-e1dd-4bbb-a11f-03383d54f5aa" />

Redis Caching: Displays stored IP and weather data with TTL in Redis.

<img width="1362" alt="Screenshot 2025-02-17 at 11 56 53 PM" src="https://github.com/user-attachments/assets/9b6bdf27-ec9f-49f6-ab68-44c6255ee59e" />


Rate Limiting in Action: Demonstrates blocked requests due to exceeded limits.

<img width="1117" alt="Screenshot 2025-02-17 at 11 57 28 PM" src="https://github.com/user-attachments/assets/9b70670f-3fc6-4710-a37a-20d3d8fcdb3c" />


Exception Handling with incorrect IP

<img width="1135" alt="Screenshot 2025-02-17 at 11 57 44 PM" src="https://github.com/user-attachments/assets/701b1093-5172-46f5-898d-df162b1245cf" />


# 🐳 Docker Deployment

```
docker network create api-network
docker build -t wweather-service./weather-service
docker build -t ratelimiter-weatherService ./ratelimiter-weatherService
docker run --network api-network -p 8080:8080 weather-service
docker run --network api-network -p 8082:8082 ratelimiter-weatherService
```










