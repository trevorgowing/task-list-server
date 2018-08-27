[![Build Status](https://travis-ci.com/trevorgowing/task-list-server.svg?branch=master)](https://travis-ci.com/trevorgowing/task-list-server)
[![codecov](https://codecov.io/gh/trevorgowing/task-list-server/branch/master/graph/badge.svg)](https://codecov.io/gh/trevorgowing/task-list-server)

# Task List Server

Hello world task api.

## Future Tasks

* Complete database constraint testing
* Secure the api with JWT bearer tokens

## Technology

### Production Code

* Language: [Java](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* Application Server (Servlet Container): [Apache Tomcat](http://tomcat.apache.org/)
* Application and Web Framework: [Spring Boot](https://projects.spring.io/spring-boot/)
* Database Integration: [Spring Data](https://projects.spring.io/spring-data/)
* JPA Provider: [Hibernate](http://hibernate.org/)
* Code Generation: [Project Lombok](https://projectlombok.org/)

### Test Code

* [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
  * [Junit](https://junit.org/junit4/)
  * [Mockito](https://site.mockito.org/)
  * [Hamcrest](http://hamcrest.org/)
* [Rest Assured](http://rest-assured.io/)

### Tooling

* Build: [Gradle](https://gradle.org/)
* Code Coverage: [Jacoco](https://www.jacoco.org/jacoco/)
* Code Formatting: [Spotless](https://github.com/diffplug/spotless) with [Google Style Guide](https://google.github.io/styleguide/javaguide.html)
* Containerization: [Docker](https://www.docker.com/)
* Database: [PostgresSQL](https://www.postgresql.org/)
* Database Migration: [Flyway](https://flywaydb.org/)

## Build, Run and Test

The recommended [gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) is used to build the application and is included in the source code.

### Build

* With Gradle: `./gradlew build`
* With Docker: `sudo docker build -t trevorgowing/task-list-server:X.X.X .`
* With Docker Compose: `sudo docker-compose build task-list-server`

### Run

* With Gradle: `./gradlew bootrun` (will build if not already built)
* With Docker: `sudo docker run trevorgowing/task-list-server:X.X.X` (needs to be built explicitly)
* With Docker Compose: `sudo docker-compose --build up`
* With Bash Script: `./start.sh` (runs docker-compose internally)

### Test

* With Gradle: `./gradlew test`

## Getting started

### Installation

1. [Install Docker](https://docs.docker.com/install/)
2. [Install Docker Compose](https://docs.docker.com/compose/install/)
3. Run start script `./start.sh`

### Configuration Defaults

Web server (Apache Tomcat):

* Host: localhost
* Port: 8080

Database (Postgres):

* Host: localhost
* Port: 5432
* Name: tasks
* Username: tasks
* Password: tasks

### API

#### User

```
GET /api/user Accept:application/json;charset=UTF-8
200 ContentType: application/json;charset=UTF-8
[
     {
         "id": 1,
         "username": "jsmith",
         "first_name": "John",
         "last_name": "Doe"
     }
 ]
```
 
```
GET /api/user/1 Accept:application/json;charset=UTF-8
200 ContentType: application/json;charset=UTF-8
 {
     "id": 1,
     "username": "jsmith",
     "first_name": "John",
     "last_name": "Doe"
 }
```

```
POST /api/user Accept:application/json;charset=UTF-8
 {
     "username": "jsmith",
     "first_name": "John",
     "last_name": "Doe"
 }
201 ContentType: application/json;charset=UTF-8
 {
     "id": 1,
     "username": "jsmith",
     "first_name": "John",
     "last_name": "Doe"
 }
```

```
PUT /api/user/1 Accept:application/json;charset=UTF-8
 {
     "username": "jsmith",
     "first_name": "John",
     "last_name": "Doe"
 }
200 ContentType: application/json;charset=UTF-8
 {
     "id": 1,
     "username": "jsmith",
     "first_name": "John",
     "last_name": "Doe"
 }
```

```
DELETE /api/user/1
204
```

#### User Tasks

```
GET /api/user/1/task Accept:application/json;charset=UTF-8
200 ContentType: application/json;charset=UTF-8
[
     {
         "id": 1,
         "name": "code",
         "description": "code a lot",
         "date_time": "2018-08-27T18:00:00.000Z"
     }
 ]
```
 
```
GET /api/user/1/task/1 Accept:application/json;charset=UTF-8
200 ContentType: application/json;charset=UTF-8
 {
     "id": 1,
     "name": "code",
     "description": "code a lot",
     "date_time": "2018-08-27T18:00:00.000Z"
 }
```

```
POST /api/user/1/task Accept:application/json;charset=UTF-8
 {
     "id": 1,
     "name": "code",
     "description": "code a lot",
     "date_time": "2018-08-27T18:00:00.000Z"
 }
201 ContentType: application/json;charset=UTF-8
 {
     "id": 1,
     "name": "code",
     "description": "code a lot",
     "date_time": "2018-08-27T18:00:00.000Z"
 }
```

```
PUT /api/user/1/task/1 Accept:application/json;charset=UTF-8
 {
     "id": 1,
     "name": "code",
     "description": "code a lot",
     "date_time": "2018-08-27T18:00:00.000Z"
 }
200 ContentType: application/json;charset=UTF-8
 {
     "id": 1,
     "name": "code",
     "description": "code a lot",
     "date_time": "2018-08-27T18:00:00.000Z"
 }
```

```
DELETE /api/user/1/task/1
204
```

## License

[MIT License](LICENSE)
