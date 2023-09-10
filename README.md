# Spring Kafka Checkout

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-3.0.9-red)
![Gradle](https://img.shields.io/badge/Gradle-8.2.1-orange)

A Proof of Concept (POC) project simulating a microservice for e-commerce checkout. It uses Java 17, Spring Boot 3, Kafka, Gradle, and PostgreSQL.

## Introduction

The `spring-kafka-checkout` project simulates a microservice for e-commerce checkout. It demonstrates the integration of Java 17, Spring Boot 3, Kafka, and Gradle. This project provides a basic setup for handling asynchronous transaction processing, with events sent to a Kafka topic and consumed by different processors for status updates.

## Features

- Create, retrieve, and cancel transactions via REST API
- Kafka integration for event-driven processing
- Docker-compose setup for local development

## Getting Started

To run this project locally, make sure you have Docker and Docker Compose installed. Then, follow these steps:

1. Clone the repository:

```
git clone https://github.com/rafa-andrade/spring-kafka-checkout
cd spring-kafka-checkout
```

2. Start the dependencies using Docker Compose:

```
docker-compose up -d
```

3. Build and run the Spring Boot application:

```
./gradlew bootRun
```

4. Access the OpenAPI documentation at http://localhost:8080/swagger-ui/index.html

Usage
-----

**REST API Endpoints**

- **Create Transaction**
    - *Endpoint*: `POST /api/v1/transactions`
    - *Request Body*: `TransactionRequest`
    - *Response*: `UUID`

- **Get Transaction**
    - *Endpoint*: `GET /api/v1/transactions/{externalReference}`
    - *Path Variable*: `externalReference` (UUID)
    - *Response*: `TransactionResponse`

- **Cancel Transaction**
    - *Endpoint*: `DELETE /api/v1/transactions/{externalReference}`
    - *Path Variable*: `externalReference` (UUID)
    - 

Contributing
------------

1. Fork the repository
2. Create a new branch (`git checkout -b feature/new-feature`)
3. Make your changes and commit them (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a pull request


License
-------

This project is licensed under the MIT License.
