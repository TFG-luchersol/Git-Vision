# Git-Vision
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/0686fb60bd574220ab20c0631ae47871)](https://app.codacy.com/gh/TFG-luchersol/Git-Vision/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

Git-Vision es un proyecto diseñado para proporcionar una forma visual e interactiva de explorar repositorios Git. Combina un frontend en React con un backend en Spring Boot para ofrecer una experiencia fluida al analizar la historia y estructura de GitHub.

## Características

- Representación visual de repositorios GitHub.
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

## Ejecución Local

### Configuración

#### Backend

1. Inicialmente debes copiar el archivo `.env.example` y renombrarlo como `.env`.

```bash
  cp .env.example .env
```

2. Luego de ello, debes realizar las modificaciones necesarias a las variables de entorno en el apartado **Develop**, para que coincidan con tu base de datos. También puedes modificar cualquier otra variable que consideres necesaria, ya que las actuales son solo ejemplos.

3. Si lo ves necesario, puedes modificar los ajustes de la base de datos en los archivos:

   - `application.properties`
   - `application-dev.properties`

Estos archivos contienen configuraciones específicas que pueden requerir adaptación según tu entorno.

#### Frontend

1. Muevete a la carpeta `frontend`.

```bash
  cd frontend
```

2. Inicialmente debes copiar el archivo `.env.example` y renombrarlo como `.env`.

```bash
  cp .env.example .env
```

Actualmente la única variable existente es la variable `BACKEND_URL`, en ella debes de poner el dominio en el que se encuentra desplegado tu backend. Si no existe la variable, se tomará el dominio como `http://localhost:8080`.

### Ejecución Local del Backend

El backend es una aplicación Spring Boot construida con Maven. Para ejecutarlo localmente:

1. Clona el repositorio:
  ```bash
   git clone https://github.com/TFG-luchersol/Git-Vision.git
   cd Git-Vision
  ```

2. Ejecuta el proyecto:
  ```bash
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

### Ejecución Local del Frontend

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

### Ejecución Local por .bat

En caso de tener todo ya configurado, para futuras ocasiones puedes, en vez de ejecutar todos los comandos, ejecutar el archivo `./runner.bat`, el cuál iniciar tanto backend como frontend. En caso que querer realizar pruebas con el modo debug en backend, se recomienda iniciar el backend de forma normal y si le es más como puede ejecutar el archivo `./runner_frontend.bat` para ejecutar el frontend.

## Despliegue con Docker y Docker Compose

Puedes desplegar tanto el backend como el frontend utilizando Docker y `docker-compose`. Sigue estos pasos:

1. Inicialmente debes copiar el archivo `.env.example` y renombrarlo como `.env`.

```bash
  cp .env.example .env
```

**Nota:** Si ve necesario, puede hacer modificaciones en las variables dentro del archivo `.env` debajo del apartado **Docker** y en los archivos `application.properties` y `application-docker.properties`.

2. Asegúrate de tener Docker y Docker Compose instalados en tu sistema.

3. Clona el repositorio si aún no lo has hecho:
  ```
  git clone https://github.com/TFG-luchersol/Git-Vision.git
  cd Git-Vision
  ```

4. Construye y ejecuta los contenedores:
  ```
  docker-compose up --build
  ```

5. Accede a las aplicaciones:
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

## Contribuciones

¡Agradecemos sus contribuciones! Por favor, bifurque el repositorio y envíe una solicitud de incorporación de cambios.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulte el archivo [LICENSE](https://github.com/TFG-luchersol/Git-Vision/blob/main/LICENSE) por detalles.
