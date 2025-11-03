# # Используем лёгкий образ с Java 17
# FROM openjdk:17-jdk-slim
#
# # Рабочая директория внутри контейнера
# WORKDIR /app
#
# # Копируем собранный .jar файл
# COPY target/okuylu_back-0.0.1-SNAPSHOT.jar app.jar
#
# # Открываем нужный порт
#
# EXPOSE 4445
#
# # Запускаем Spring Boot приложение
# ENTRYPOINT ["java", "-jar", "app.jar"]
# Используем образ с Java 17 и Maven
# Stage 1: Build
# Stage 1: Build
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/okuylu_back-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4445
ENTRYPOINT ["java", "-jar", "app.jar"]

