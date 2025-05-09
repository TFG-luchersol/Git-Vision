# Etapa de construcción
FROM eclipse-temurin:23-jdk AS build

# Establecer directorio de trabajo
WORKDIR /workspace/app

# Copiar archivos necesarios
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Asegurar que mvnw tenga el formato correcto (LF) y permisos de ejecución
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Descargar dependencias sin compilar todo
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y compilar
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
