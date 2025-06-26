FROM openjdk:17-jdk-slim

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR de la aplicación
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} LAB-TBD-0.0.1-SNAPSHOT.jar

# Exponer el puerto
EXPOSE 8097

# Configurar variables de entorno
ENV SPRING_PROFILES_ACTIVE=docker

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/LAB-TBD-0.0.1-SNAPSHOT.jar"]
