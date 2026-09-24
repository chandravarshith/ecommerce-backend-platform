<div align="center">

# Service Discovery

**Netflix Eureka registry for the E-Commerce Backend Platform.**

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?logo=springboot&logoColor=white)
![Eureka](https://img.shields.io/badge/Spring%20Cloud-Eureka%20Server-6DB33F?logo=spring&logoColor=white)

</div>

[← Back to platform overview](../README.md)

---

## Overview

The service registry lets services find each other by name rather than by host and port. Services that act as Eureka clients register themselves here on startup, and a load-balanced client can then resolve a name such as `user-authentication-service` to a live instance.

This service **must be running before** the services that register with it.

| | |
|---|---|
| **Port** | `8761` |
| **Application name** | `service-discovery` |
| **Dashboard** | http://localhost:8761 |

## How It Works

The application is a standard Spring Boot app annotated with `@EnableEurekaServer`. It is configured as a standalone registry, so it does not register with itself or fetch a registry from a peer.

```properties
spring.application.name=service-discovery
server.port=8761
eureka.instance.hostname=localhost
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

## Tech Stack

- Java 17
- Spring Boot 3.5.7
- Spring Cloud Netflix Eureka Server 4.3.0

## Prerequisites

- JDK 17

No database, broker or environment variables are needed.

## Run Locally

```bash
cd service-discovery
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run`.

Open **http://localhost:8761** to see the Eureka dashboard. As the other services start, they appear under *Instances currently registered with Eureka*:

| Registered name | Service |
|---|---|
| `USER-AUTHENTICATION-SERVICE` | user-authentication-service |
| `PRODUCTCATELOGSERVICE` | product-catalog-service |

## Project Structure

```text
service-discovery/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/org/example/servicediscovery/
    │   │   └── ServiceDiscoveryApplication.java
    │   └── resources/application.properties
    └── test/
        └── java/org/example/servicediscovery/ServiceDiscoveryApplicationTests.java
```

## Testing

```bash
./mvnw test
```

Runs the Spring context-load test.
