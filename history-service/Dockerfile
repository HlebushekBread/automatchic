FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY common-module ./common-module
COPY app-service ./app-service
COPY history-service ./history-service
COPY notification-service ./notification-service

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/history-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]