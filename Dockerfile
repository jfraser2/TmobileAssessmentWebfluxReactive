# Stage 1: Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
#FROM openjdk:8 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Stage 2: Deploy Stage - Not the same context as builder
FROM maven:3.9.6-eclipse-temurin-21
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=dev", "-jar", "-Xms4G", "-Xmx10G", "app.jar"]
