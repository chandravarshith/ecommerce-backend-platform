<div align="center">

# Notification Service

**Kafka-driven email delivery for the E-Commerce Backend Platform.**

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?logo=springboot&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-consumer-231F20?logo=apachekafka&logoColor=white)
![JavaMail](https://img.shields.io/badge/Email-JavaMail%20%2B%20Gmail%20SMTP-EA4335?logo=gmail&logoColor=white)

</div>

[← Back to platform overview](../README.md)

---

## Overview

The notification service listens for events on Kafka and turns them into emails. It exposes no REST endpoints; all interaction happens through the message broker. Today it handles one event type: the welcome message produced when a user signs up through the [user authentication service](../user-authentication-service/README.md).

| | |
|---|---|
| **Port** | `8081` |
| **Application name** | `notification-service` |
| **Consumes** | Kafka topic `SIGNUP` |
| **Consumer group** | `NOTIFICATION_SERVICE` |
| **Email transport** | Gmail SMTP (`smtp.gmail.com:587`, STARTTLS) |

## How It Works

```mermaid
flowchart LR
    Auth[User Authentication<br/>Service] -- "JSON event" --> Topic{{Kafka topic<br/>SIGNUP}}
    Topic --> Listener[KafkaConsumerClient<br/>@KafkaListener]
    Listener --> Util[EmailUtil]
    Util --> SMTP[Gmail SMTP]
    SMTP --> User([New user's inbox])
```

1. `KafkaConsumerClient` subscribes to the `SIGNUP` topic in consumer group `NOTIFICATION_SERVICE`.
2. Each message is deserialised into an `EmailDto`.
3. A JavaMail session is opened against Gmail SMTP, authenticating with the event's `sender` address and the configured app password.
4. `EmailUtil` builds the message and sends it to the `recipient`.

### Event payload

The message value is a JSON string:

```json
{
  "sender": "you@gmail.com",
  "recipient": "asha@example.com",
  "subject": "Welcome to Product Catalog System!",
  "message": "Thank you for successfully registering with us. Have a pleasant stay!"
}
```

| Field | Description |
|---|---|
| `sender` | Gmail account used to authenticate with SMTP |
| `recipient` | Address the email is delivered to |
| `subject` | Email subject |
| `message` | Email body (plain text) |

## Tech Stack

- Java 17
- Spring Boot 3.5.9
- Spring for Apache Kafka 3.3.9
- JavaMail (`com.sun.mail:javax.mail` 1.5.5)
- Lombok

## Prerequisites

- JDK 17
- A running Kafka broker on port `9092`
- A Gmail account with 2-Step Verification and an [app password](https://support.google.com/accounts/answer/185833)

## Configuration

| Variable | Required | Description |
|---|---|---|
| `KAFKA_COMPUTER_IP` | Yes | Host of the Kafka broker; the service connects to `<host>:9092` |
| `EMAIL_PWD` | Yes | Gmail app password used to authenticate with SMTP |

```bash
export KAFKA_COMPUTER_IP="localhost"
export EMAIL_PWD="your-gmail-app-password"
```

Relevant `application.properties`:

```properties
spring.application.name=notification-service
server.port=8081
spring.kafka.bootstrap-servers=${KAFKA_COMPUTER_IP}:9092
email.password=${EMAIL_PWD}
```

> The sender address is not configured here. It arrives in each event, set by the authentication service through its `EMAIL_USERNAME` variable, so the two services must use the same Gmail account and app password.

## Run Locally

Start Kafka first, then:

```bash
cd notification-service
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run`.

### Sending a test event

With a broker on `localhost:9092`, publish a message to the topic using Kafka's console producer:

```bash
kafka-console-producer.sh --bootstrap-server localhost:9092 --topic SIGNUP
> {"sender":"you@gmail.com","recipient":"friend@example.com","subject":"Hello","message":"Test email"}
```

Alternatively, sign up a user through the authentication service and the event is published for you.

## Project Structure

```text
notification-service/
├── pom.xml
├── mvnw / mvnw.cmd
└── src/
    ├── main/
    │   ├── java/org/example/notificationservice/
    │   │   ├── NotificationServiceApplication.java
    │   │   ├── clients/KafkaConsumerClient.java   # @KafkaListener on SIGNUP
    │   │   ├── dtos/EmailDto.java                 # event payload
    │   │   └── utils/EmailUtil.java               # builds and sends the MIME message
    │   └── resources/application.properties
    └── test/
        └── java/org/example/notificationservice/NotificationServiceApplicationTests.java
```

## Notes

- Delivery uses Gmail's SMTP relay; Gmail may rewrite or restrict the visible `From` address depending on account settings.
- The `From` header is set in `EmailUtil`; the authenticated `sender` from the event is used only for SMTP login.
- Send failures are logged to the console and are not retried.

## Testing

```bash
./mvnw test
```

Runs the Spring context-load test, which needs the environment variables above to be set.
