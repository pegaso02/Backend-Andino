# Etapa 1: Build con Maven y JDK 21 para compilar con release 21
FROM maven:3.9.6-eclipse-temurin-21-jammy as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Contenedor ligero con JRE para ejecutar la app
FROM openjdk:21-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
