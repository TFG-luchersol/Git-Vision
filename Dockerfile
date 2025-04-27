# Etapa de construcción
FROM eclipse-temurin:23-jdk AS build

# Establecer el directorio de trabajo
WORKDIR /workspace/app

# Copiar los archivos necesarios y ejecutar maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline
  
COPY src src
RUN ./mvnw package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:23-jre

# Establecer el directorio de trabajo para la imagen final
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /workspace/app/target/*.jar app.jar

# Exponer el puerto 8080 para acceder a la aplicación
EXPOSE 8080

# Establecer el comando por defecto para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
