# Git-Vision

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/0686fb60bd574220ab20c0631ae47871)](https://app.codacy.com/gh/TFG-luchersol/Git-Vision/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

Git-Vision es un proyecto diseñado para proporcionar una forma visual e interactiva de explorar repositorios Git. Combina un frontend en React con un backend en Spring Boot para ofrecer una experiencia fluida al analizar la historia y estructura de Git.

## Características

- Representación visual de repositorios Git.
- Exploración interactiva de commits, ramas y etiquetas.
- API RESTful para comunicación con el backend.
- Autenticación basada en JWT para acceso seguro.
- Interfaz moderna basada en React.

## Requisitos Previos

Asegúrate de tener instalados los siguientes componentes en tu sistema:

- **Java 23** o una versión más reciente.
- **Node.js 18** o una versión más reciente.
- **Docker** y **Docker Compose**.
- Herramienta de línea de comandos de Git.
- Tu IDE preferido (por ejemplo, IntelliJ IDEA, VS Code, Eclipse).

## Ejecución Local del Backend

El backend es una aplicación Spring Boot construida con Maven. Para ejecutarlo localmente:

1. Clona el repositorio:
   ```bash
   git clone https://github.com/TFG-luchersol/Git-Vision.git
   cd Git-Vision
  ```

2. Construye el proyecto:
  ```
  ./mvnw package
  ```

3. Ejecuta la aplicación:
  ```
  java -jar target/*.jar
  ```

4. Accede al backend en [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

Alternativamente, puedes ejecutar el backend directamente con Maven:
```
./mvnw spring-boot:run
```

## Configuración de la Base de Datos

La configuración predeterminada utiliza una base de datos H2 en memoria, precargada con datos de ejemplo desde `data.sql`. Puedes modificar los ajustes de la base de datos en `application.properties` para conectarte a una base de datos externa.

## Ejecución Local del Frontend

El frontend es una aplicación React ubicada en la carpeta `frontend`. Para ejecutarlo:

1. Navega al directorio `frontend`:
  ```
  cd frontend
  ```

2. Instala las dependencias:
  ```
  npm install
  ```

3. Inicia el servidor de desarrollo:
  ```
  npm start
  ```

4. Accede al frontend en [http://localhost:3000](http://localhost:3000).

## Despliegue con Docker y Docker Compose

Puedes desplegar tanto el backend como el frontend utilizando Docker y `docker-compose`. Sigue estos pasos:

1. Asegúrate de tener Docker y Docker Compose instalados en tu sistema.

2. Clona el repositorio si aún no lo has hecho:
  ```
  git clone https://github.com/TFG-luchersol/Git-Vision.git
  cd Git-Vision
  ```

3. Construye y ejecuta los contenedores:
  ```
  docker-compose up --build
  ```

4. Accede a las aplicaciones:
  - Backend: http://localhost:8080/swagger-ui/index.html
  - Frontend: http://localhost:3000

## Development Workflow

### Backend

- Usa tu IDE para abrir el proyecto y ejecutar la clase principal `GitVisionApplication`.
- Modifica el código del backend y reinicia la aplicación para ver los cambios
### Frontend

- Usa el servidor de desarrollo de React para recarga en vivo.
- Modifica los componentes de React y guarda para ver los cambios al instante.

## Estructura del Proyecto

| Componente          | Ubicación                                                               |
|---------------------|-------------------------------------------------------------------------|
| Clase Principal del Backend  | [GitVisionApplication.java](https://github.com/TFG-luchersol/Git-Vision/blob/main/src/main/java/com/example/gitvision/GitVisionApplication.java) |
| Propiedades del Backend  | [application.properties](https://github.com/TFG-luchersol/Git-Vision/blob/main/src/main/resources/application.properties) |
| Punto de Entrada del Frontend| [index.jsx](https://github.com/TFG-luchersol/Git-Vision/blob/main/frontend/src/index.jsx) |

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](https://github.com/TFG-luchersol/Git-Vision/blob/main/LICENSE) file for details.
