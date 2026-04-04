FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY app-service/pom.xml app-service/
COPY notification-service/pom.xml notification-service/

RUN mvn dependency:go-offline -B

COPY notification-service/src ./notification-service/src

RUN mvn clean package -DskipTests -pl app-service -am

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]