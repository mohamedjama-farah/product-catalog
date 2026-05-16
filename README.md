# Product Catalog

[![Java CI with Maven](https://github.com/mohamedjama-farah/product-catalog/actions/workflows/maven.yml/badge.svg)](https://github.com/mohamedjama-farah/product-catalog/actions/workflows/maven.yml)
[![Coverage Status](https://coveralls.io/repos/github/mohamedjama-farah/product-catalog/badge.svg?branch=main)](https://coveralls.io/github/mohamedjama-farah/product-catalog)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mohamedjama-farah_product-catalog&metric=alert_status)](https://sonarcloud.io/dashboard?id=mohamedjama-farah_product-catalog)

A simple Java Swing desktop application to manage a product catalog.

## Domain Model
- **Product**: id, name, price, categoryId
- **Category**: id, name
- **Relation**: Many Products → one Category

## How to build and run tests

```bash
mvn clean verify
```

Requires: Java 17, Maven, Docker

## Technologies
- Java 17 + Java Swing
- MongoDB + Testcontainers
- JUnit 4 + Mockito + AssertJ
- JaCoCo + PIT + SonarCloud + Coveralls
- Maven + GitHub Actions
