# Etapa 1: Build con Maven y JDK 21 para compilar con release 21
FROM maven:3.9.0-eclipse-temurin-21 as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# Etapa 2: Contenedor ligero con JRE para ejecutar la app
FROM openjdk:21-slim

WORKDIR /app

RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

USER appuser

COPY --from=builder /app/target/backend-pos-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
