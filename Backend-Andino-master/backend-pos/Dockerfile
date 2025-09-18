# Usamos una imagen base con JDK 21 (OpenJDK)
FROM openjdk:21-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo jar generado por Maven al contenedor
COPY target/backend-pos-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 (por defecto de Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
