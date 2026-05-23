# Laboratorio 2 — Arquitectura Hexagonal PersonApp

Este proyecto es una implementación completa del patrón de Arquitectura Hexagonal utilizando Spring Boot 2.7.11 y Java 11. 

El sistema gestiona 4 entidades de dominio (`Persona`, `Profesion`, `Telefono` y `Estudio`) con soporte para persistencia políglota a través de MariaDB y MongoDB. La aplicación ofrece tanto una API REST como una Interfaz de Línea de Comandos (CLI).

## Estructura del Proyecto

El proyecto se divide en 7 módulos Maven:
- **`common`**: Excepciones, enumeraciones y utilidades transversales.
- **`domain`**: Modelos de negocio (Entities) puros, sin dependencias de frameworks.
- **`application`**: Casos de uso (UseCases) y puertos (Ports) de entrada y salida.
- **`maria-output-adapter`**: Adaptador de persistencia relacional con Spring Data JPA.
- **`mongo-output-adapter`**: Adaptador de persistencia NoSQL con Spring Data MongoDB.
- **`rest-input-adapter`**: Adaptador de entrada que expone una API REST con Swagger.
- **`cli-input-adapter`**: Adaptador de entrada que provee una interfaz interactiva de consola.

## Requisitos

- **JDK 11**
- **Maven 3.8+**
- **Docker & Docker Compose**

## Ejecución del Entorno (Bases de Datos)

El proyecto incluye un archivo `docker-compose.yml` pre-configurado para levantar ambos motores de base de datos con las credenciales y schemas esperados por los properties de la aplicación (`persona_db`).

```bash
# Levantar MariaDB (puerto 3307) y MongoDB (puerto 27017) en segundo plano
docker-compose up -d
```

## Compilación

```bash
# Desde el directorio raíz del proyecto (lab2)
mvn clean install -DskipTests
```

## Ejecución

El proyecto expone dos adaptadores de entrada independientes. Ambos pueden conectarse tanto a MariaDB como a MongoDB (el CLI lo pide interactivamente, la API REST lo recibe como variable en la URL).

### 1. Interfaz de Línea de Comandos (CLI)

```bash
# Ejecutar el adaptador CLI
mvn spring-boot:run -pl cli-input-adapter
```

### 2. API REST

```bash
# Ejecutar el adaptador REST
mvn spring-boot:run -pl rest-input-adapter
```
La API estará disponible en el puerto `3000`.

- **Swagger UI**: [http://localhost:3000/swagger-ui.html](http://localhost:3000/swagger-ui.html)
- **API Docs**: [http://localhost:3000/api-docs](http://localhost:3000/api-docs)

Ejemplos de Rutas REST:
- `GET /api/v1/persona/MARIA`
- `GET /api/v1/profesion/MONGO`
- `POST /api/v1/telefono`
- `GET /api/v1/estudio/MARIA/123456789/1` (búsqueda por llave compuesta ccPersona/idProfesion)
