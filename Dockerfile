FROM openjdk:8-jdk-alpine

COPY build/libs/*.jar bluebank.jar

EXPOSE 8081

ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/bluebank.jar"]