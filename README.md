# Similar Product - Backend Technical Test

Esta aplicación es un servicio (desarrollado con arquitectura hexagonal) que expone un endpoint (REST) el cual, dado un productId, devuelve la lista de productos similares consultando una API externa.

## ️ Tecnologías utilizadas

- Java 21

- Spring Boot 3.3

- Spring WebFlux

- Resilience4j

- Springdoc OpenAPI 3

- JUnit 5

- WebTestClient

- MockWebServer

- StepVerifier

- Mockito

- Reactor Test

- Docker

- GitHub Actions y GHCR


## Endpoints
`GET /product/{productId}/similar`

Códigos de respuesta:

- `200` : Devuelve la lista de similares.
- `204` : El producto existe, pero no tiene similares.
- `400` :	El productId es inválido (está vacío, o mal formado).
- `404` :	El producto no existe.


## Resiliencia

Implementada tanto en el servicio como en el adapter.


## Testing

Hay cobertura de tests:

- Unitarios
- De Integración
- End-to-end
- De contrato (OpenAPI)
- Acceptance
- De resiliencia



## Cómo levantar la aplicación

### Compilar
`mvn clean verify`

### Ejecutar localmente
`mvn spring-boot:run`

### Docker

- `docker build -t similar-product . `
- `docker run -p 5000:5000 similar-product`

Adicionalmente, se realizó un Makefile para ejecutar todo en local (con la API externa mockeada). 
Para ejecutarlo, seguir los siguientes pasos (además de tener instalado make):


### Ejecutar la build completa
`make build`


### Levantar la app sin Docker
`make run`


### Construir la imagen Docker
`make docker-build`


###  Ejecutar la imagen de Docker
`make docker-run`



La app arranca en `http://localhost:5000`

## CI/CD similarproduct.yml

Pipeline completo:

- Se compila el proyecto
- Se ejecutan todos los tests
- Se ejecuta análisis estático (Checkstyle y SpotBugs)
- Se genera el .jar de la aplicación
- Se construye la imagen Docker
- Se publica la imagen en GHCR
- Se ejecutan los smoke tests levantando el contenedor y consultando el `/actuator/health`


## OpenAPI

Disponible en:

`http://localhost:5000/v3/api-docs`
`http://localhost:5000/swagger-ui.html`