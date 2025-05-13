[![Java Maven Build Test](https://github.com/deepaksorthiya/spring-boot-security-angular-client-csrf/actions/workflows/maven-build.yml/badge.svg)](https://github.com/deepaksorthiya/spring-boot-security-angular-client-csrf/actions/workflows/maven-build.yml)
[![Docker Hub badge][dockerhub-badge]][dockerhub]

[dockerhub-badge]: https://img.shields.io/docker/pulls/deepaksorthiya/spring-boot-security-angular-client-csrf

[dockerhub]: https://hub.docker.com/repository/docker/deepaksorthiya/spring-boot-security-angular-client-csrf

---

### ** Spring Boot Security with CSRF protection for SPA(Single Page Applications) Using Angular Framework **

---

# Getting Started

## Requirements:

```
Git: 2.49.0
Spring Boot: 3.4.5
Maven: 3.9+
Java: 21
(Optional)Docker Desktop: Tested on 4.41.0
```

## Clone this repository:

```bash
git clone https://github.com/deepaksorthiya/spring-boot-security-angular-client-csrf.git
cd spring-boot-security-angular-client-csrf
```

## Build Project:

```bash
./mvnw clean package -DskipTests
```

## Run Project:

```bash
./mvnw spring-boot:run
```

OR

```bash
java -jar .\target\spring-boot-security-angular-client-csrf-0.0.1-SNAPSHOT.jar
```

## (Optional)Build Docker Image(docker should be running):

```bash
./mvnw clean spring-boot:build-image -DskipTests
```

## (Optional)Running On Docker

```bash
docker run -p 8080:8080 --name spring-boot-security-angular-client-csrf deepaksorthiya/spring-boot-security-angular-client-csrf:0.0.1-SNAPSHOT
```

## Users for Testing

```
USER1 ==> Username: user Password: password
USER2 ==> Username: admin Password : admin
```

## Testing

visit to access application
http://localhost:8080

## Maven Clean Plugin Config

add below plugin config in [pom.xml](pom.xml) if you want
to remove directories during clean phase.

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-clean-plugin</artifactId>
    <configuration>
        <filesets>
            <fileset>
                <directory>src/main/frontend/.angular</directory>
            </fileset>
            <fileset>
                <directory>src/main/frontend/dist</directory>
            </fileset>
            <fileset>
                <directory>src/main/frontend/node</directory>
            </fileset>
            <fileset>
                <directory>src/main/frontend/node_modules</directory>
            </fileset>
        </filesets>
    </configuration>
</plugin>
```

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot//io/validation.html)
* [Flyway Migration](https://docs.spring.io/spring-boot/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

## Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)


