# OpenLMIS Example Service
This example is meant to show an OpenLMIS 3.x Independent Service at work.

## Prerequisites
* Docker 1.11+

## Quick Start
1. Fork/clone this repository from GitHub.

 ```shell
 git clone https://github.com/OpenLMIS/openlmis-example.git
 ```
2. To properly test this example service is working, enter `spring.mail` values in the `application.properties` file.
3. Develop w/ Docker by running `docker-compose run --service-ports example`.  
See [Developing w/ Docker](#devdocker).
4. To start the Spring Boot application, run with: `gradle bootRun`.
5. Go to `http://<yourDockerIPAddress>:8080/api/` to see the APIs.

## Email notifications
To test that email notifications are working in the example service, POST to:

 `http://<yourDockerIPAddress>:8080/api/notifications`
 
 some JSON object like so (put Content-Type: application/json in the headers):
 
 ```
 { "recipient": "<aValidEmailAddress>", "subject": "Test message", "message": "This is a test." }
 ```

## Customizing REST controls
This example service also demonstrates how to enable/disable RESTful actions. 
Two classes, Foo and ReadOnlyFooRepository (in conjunction with Spring Data 
REST), expose a REST endpoint at /api/foos. This endpoint is read-only, which 
means POST/PUT/DELETE actions are not allowed. The ReadOnlyFooRepository does 
this by extending the Repository interface, rather than the CrudRepository 
interface, and defining the methods allowed. For read-only access, the save() 
and delete() methods, which are present in the CrudRepository, are not defined 
in the ReadOnlyFooRepository. By selectively exposing CRUD methods using Spring 
Data JPA, REST controls can be customized.

See the [Spring Data JPA reference](http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.definition-tuning) 
for more information.

## Building & Testing

Gradle is our usual build tool.  This template includes common tasks 
that most Services will find useful:

- `clean` to remove build artifacts
- `build` to build all source
- `generateMigration -PmigrationName=<yourMigrationName>` to create a 
"blank" database migration script. A file 
will be generated under `src/main/resources/db/migration`. Put your 
migration SQL into that file.
- `test` to run unit tests

While Gradle is our usual build tool, OpenLMIS v3+ is a collection of 
Independent Services where each Gradle build produces 1 Service. 
To help work with these Services, we use Docker to develop, build and 
publish these.

See [Developing with Docker](#devdocker). 

## <a name="devdocker"></a> Developing with Docker

OpenLMIS utilizes Docker to help with development, building, publishing
and deployment of OpenLMIS Services. This helps keep development to 
deployment environments clean, consistent and reproducible and 
therefore using Docker is recommended for all OpenLMIS projects.

To enable development in Docker, OpenLMIS publishes a couple Docker 
Images:

- [openlmis/dev](https://hub.docker.com/r/openlmis/dev/) - for Service 
development.  Includes the JDK & Gradle plus common build tools.
- [openlmis/postgres](https://hub.docker.com/r/openlmis/postgres/) - for 
quickly standing up a shared PostgreSQL DB

In addition to these Images, each Service includes Docker Compose 
instructions to:

- standup a development environment (run Gradle)
- build a lean image of itself suitable for deployment
- publish its deployment image to a Docker Repository

### Development Environment
Launches into shell with Gradle & JDK available suitable for building 
Service.  PostgreSQL connected suitable for testing. If you run the 
Service, it should be available on port 8080.

```shell
> docker-compose run --service-ports <serviceName>
$ gradle clean build
$ gradle bootRun
```

### Build Deployment Image
The specialized docker-compose.builder.yml is geared toward CI and build 
servers for automated building, testing and docker image generation of 
the service.

```shell
> docker-compose -f docker-compose.builder.yml run builder
> docker-compose -f docker-compose.builder.yml build image
```

### Publish to Docker Repository
TODO

### Docker's file details
A brief overview of the purpose behind each docker related file

- `Dockerfile`:  build a deployment ready image of this service 
suitable for publishing.
- `docker-compose.yml`:  base docker-compose file.  Defines the 
basic composition from the perspective of working on this singular 
vertical service.  These aren't expected to be used in the 
composition of the Reference Distribution.
- `docker-compose.override.yml`:  extends the `docker-compose.yml`
base definition to provide for the normal usage of docker-compose
inside of a single Service:  building a development environment.
Wires this Service together with a DB for testing, a gradle cache
volume and maps tomcat's port directly to the host.
- `docker-compose.builder.yml`:  an alternative docker-compose file
suitable for CI type of environments to test & build this Service
and generate a publishable/deployment ready Image of the service.

### Logging
See the Logging section in the Service Template README at 
https://github.com/OpenLMIS/openlmis-template-service/blob/master/README.md.
