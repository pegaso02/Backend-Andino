# Etapa 1: Build con Maven (compila la app y genera el jar)
FROM maven:3.9.0 as builder

WORKDIR /app

# Copiamos solo el pom.xml para cachear las dependencias
COPY pom.xml .

# Descargamos las dependencias de Maven
# Esto solo se re-ejecutará si pom.xml cambia
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos y empaquetamos sin tests para acelerar el build
RUN mvn clean package -DskipTests

# Etapa 2: Contenedor ligero con JRE para ejecutar la app
FROM openjdk:21-slim

WORKDIR /app

# Creamos un usuario y grupo sin privilegios
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup

# Cambiamos al nuevo usuario
USER appuser

# Copiamos el jar generado desde la etapa anterior de forma genérica
COPY --from=builder /app/target/backend-pos-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
