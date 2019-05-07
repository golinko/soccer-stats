# Soccer-Stats API

API for getting some soccer statistics from provided dataset in csv format

## Getting Started

These instructions will get you a copy of the project up and running on your 
local machine for development and testing purposes
### Prerequisites

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. 
One way is to execute the `main` method in the `com.golinko.soccerstats.SoccerStatsApplication` class 
from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Running the tests

To run the tests you can also run JUnit `com.golinko.soccerstats.SoccerStatsApplicationTests` class from your IDE or just

```shell
mvn clean verify
```

## API Specification

After the application up and running you can visit [Swagger UI](http://localhost:8081/soccer-stats/swagger-ui.html) 
to get API endpoints specifications.
There you can also try them out by clicking on the `Try it out` button
