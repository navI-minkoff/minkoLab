# Сборка Maven/JDK 21
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
# Установление зависимостей
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Запуск собранного приложения на образе
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]