# Microservices - Eureka Service Discovery and API Gateway

This project implements:
- Eureka Service Discovery
- Spring Cloud Gateway routing
- Product Service with MongoDB
- Spring Security at the API Gateway

## Projects
- eureka-server : Eureka registry on port 8761
- api-gateway : API Gateway on port 8080
- product-service : Product REST service on port 8081

## Requirements
- Java 17+
- Maven
- MongoDB on localhost:27017
- Docker Desktop (only required for the Product Service Testcontainers tests)

## Start order
1. Start MongoDB.
2. Start `eureka-server`.
3. Start `product-service`.
4. Start `api-gateway`.

Open Eureka:
http://localhost:8761

## API
Gateway endpoint:
http://localhost:8080/api/products

Product service endpoint (direct):
http://localhost:8081/api/products

## Gateway security
The gateway protects API routes with HTTP Basic authentication.

Username: admin
Password: admin123

Example:
curl -u admin:admin123 http://localhost:8080/api/products

## Test Product Service
From product-service:
mvn test

Docker Desktop must be running because Testcontainers starts a MongoDB container for integration tests.

## Architecture

Client -> API Gateway (:8080) -> Eureka -> Product Service (:8081) -> MongoDB
