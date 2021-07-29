#Api transaccional para prueba BNEXT
Api para el tratamiento de usuarios y sus contactos.

## Solución técnica adoptada para la implementación del proyecto test-bnext-transactional-api
Para el proyecto he decidido utilizar Spring-boot como el framewrok Java más extendido y del que más experiencia tengo.
Para ello partí del siguiente initializer:
https://start.spring.io/ con las siguientes características (Gradle, Java 11, Spring Boot 2.4.8) 
al que añadí las siguientes dependencias:

- [Lombok](https://github.com/projectlombok/lombok), para la generación automática de código.
- [JUnit](https://junit.org/junit4/), utilizado para la implementación de test unitarios.
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb), para la integración con la base de datos de documentos MongoDB.
- [Spring Boot Starter Validation](https://www.baeldung.com/spring-boot-bean-validation), para la validación de datos.
- [MapStruct](https://mapstruct.org/), para el mapeo entre beans de Java.
- [Springfox](https://springfox.github.io/springfox/docs/snapshot/), para crear interfaz gráfica de la API (swagger).

A nivel de estrutura de clases, he optado por la típica de 3 capas para un API Rest (Controllers, Services y Data Access)

- Controllers: Capa que funciona de puerta de entrada para las peticiones, abstrae a la capa de lógica de negocio (Services).
Decide qué hacer con la entrada y cómo emitir la respuesta.
- Services: Capa que implementa la lógica de negocio necesario y sirve de enlace entre la capa controlador y la capa de acceso a datos.
- Data Access: Capa de acceso a datos. Esta capa tiene como objetivo facilitar la comunicación entre la aplicación y la base de datos.

## Arrancar la aplicacón en local
Modificamos el application.properties modificando la siguiente línea:
```bash
spring.data.mongodb.uri=mongodb://mongo-db:27017/TestBnextDB
```
quedando así:
```bash
spring.data.mongodb.uri=mongodb://localhost:27017/TestBnextDB
```
Arrancamos solamente la imágen de MongoBD y ejecutamos el Application.java en modo normal o debug, según necesidad.

## Arrancar la aplicación con Docker

Generar la build del proyecto:

Construir la aplicación Docker
```bash
docker-compose build --no-cache
```
Levantar el entorno Docker (MongoDB + Aplicación Java)
```bash
docker-compose up
```
## Inclusión de swagger y colección de Postman
Para ver la documentación de la api, una vez levantada la aplicación, acceder a:
http://localhost:8080/api/v1/swagger-ui/index.html

También se ha incluido la colección Postman en la carpeta "data", ubicada en el directorio raíz del proyecto.
