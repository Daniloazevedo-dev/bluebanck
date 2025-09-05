# syntax=docker/dockerfile:1

# Stage 1: build the application
FROM gradle:7.6-jdk8 AS builder
WORKDIR /home/gradle/src
COPY . .
RUN ./gradlew clean build -x test

# Stage 2: create the runtime image
FROM openjdk:8-jdk-alpine
COPY --from=builder /home/gradle/src/build/libs/*.jar bluebank.jar
EXPOSE 8081
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/bluebank.jar"]

