FROM openjdk:8u171-jdk-alpine3.8

ENV GRADLE_OPTS -Dorg.gradle.daemon=false

RUN mkdir -p /opt/project-log

WORKDIR /opt/project-log

EXPOSE 8080 5005

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .

RUN ./gradlew resolveDependencies

COPY src/main src/main

RUN ./gradlew bootjar

CMD ["java", "-Xdebug", "-Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n", "-jar", "/opt/project-log/build/libs/task-list-server-0.0.1.jar"]
