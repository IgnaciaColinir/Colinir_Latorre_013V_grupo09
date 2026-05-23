# 1. Usamos la imagen oficial de Java 21 
FROM eclipse-temurin:21-jdk-alpine

# 2. Le decimos a Docker en qué carpeta del contenedor vamos a trabajar
WORKDIR /app

# 3. Copiamos el archivo .jar ya compilado desde tu carpeta target hacia el contenedor
COPY target/ms-fichas-0.0.1-SNAPSHOT.jar app.jar

# 4. Atiende por el puerto 8094
EXPOSE 8090

# 5. El comando exacto para encender tu aplicación cuando Docker despierte
ENTRYPOINT ["java", "-jar", "app.jar"]