# Etapa 1: Build con Maven (compila la app y genera el jar)
FROM maven:3.9.0-eclipse-temurin-21 as builder

WORKDIR /app

# Copiamos pom.xml y código fuente
COPY pom.xml .
COPY src ./src

# Compilamos y empaquetamos sin tests para acelerar el build
RUN mvn clean package -DskipTests

# Etapa 2: Contenedor ligero con JDK para ejecutar la app
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copiamos el jar generado desde la etapa anterior
COPY --from=builder /app/target/backend-pos-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
