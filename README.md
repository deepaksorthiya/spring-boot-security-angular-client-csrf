[![Java Maven Build Test](https://github.com/deepaksorthiya/spring-boot-security-angular-client-csrf/actions/workflows/maven-build.yml/badge.svg)](https://github.com/deepaksorthiya/spring-boot-security-angular-client-csrf/actions/workflows/maven-build.yml)
[![Docker Hub badge][dockerhub-badge]][dockerhub]

[dockerhub-badge]: https://img.shields.io/docker/pulls/deepaksorthiya/spring-boot-security-angular-client-csrf

[dockerhub]: https://hub.docker.com/repository/docker/deepaksorthiya/spring-boot-security-angular-client-csrf

---

### ** Spring Boot Security with CSRF protection for SPA(Single Page Applications) Using Angular Framework **

---

# Getting Started

## Live Demo URL

Render.com URL :
[Demo URL](https://spring-boot-security-angular-client-csrf.onrender.com)

wait for 15-20 seconds to get server up only if not running.

## Project Information

This project is using [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin) plugin.
It will build angular project from directory ```src/main/frontend``` and place build files in
```frontend/dist/frontend/browser```. Check config in [pom.xml](pom.xml).

```
<groupId>com.github.eirslett</groupId>
<artifactId>frontend-maven-plugin</artifactId>
<version>${frontend-maven-plugin.version}</version>
<configuration>
    <workingDirectory>${frontendSrcDir}</workingDirectory>
</configuration>
```

Maven Resource plugin will copy this angular build files and
place it under maven build directory ```/target/classes/static``` as spring boot
will serve static contents from this directory.

```
<artifactId>maven-resources-plugin</artifactId>
<executions>
    <execution>
        <id>copy-resources</id>
        <phase>process-classes</phase>
        <goals>
            <goal>copy-resources</goal>
        </goals>
        <configuration>
            <outputDirectory>${basedir}/target/classes/static</outputDirectory>
            <resources>
                <resource>
                    <directory>${frontendSrcDir}/dist/frontend/browser</directory>
                </resource>
            </resources>
        </configuration>
    </execution>
</executions>
```

## Requirements:

```
Git: 2.51+
Spring Boot: 3.5.9
Maven: 3.9+
Java: 25
(Optional)Docker Desktop: Tested on 4.54.0
```

## Clone this repository:

```bash
git clone https://github.com/deepaksorthiya/spring-boot-security-angular-client-csrf.git
cd spring-boot-security-angular-client-csrf
```

## Build Project:

In below command, Maven clean phase will delete node packages. check clean plugin config
in [POM File](pom.xml). Don't use clean phase second time if you don't want to remove node packages.

```bash
./mvnw clean package -DskipTests
```

Use below option if you don't want to build frontend second time.
Don't use clean phase second otherwise it will delete frontend build as well.

```bash
./mvnw package -DskipTests -DskipFrontendBuild=true
```

for native graalvm

```bash
./mvnw -Pnative native:compile
```

It will generate a ```spring-boot-security-angular-client-csrf``` in ```target``` folder.

## Run Project:

```bash
./mvnw spring-boot:run
```

OR

```bash
java -jar .\target\spring-boot-security-angular-client-csrf-0.0.1-SNAPSHOT.jar
```

for native image run

```bash
./target/spring-boot-security-angular-client-csrf
```

## Setup Angular Local Development Environment

see [README](src/main/frontend/README.md)

## (Optional)Build Docker Image(docker should be running):

```bash
./mvnw clean spring-boot:build-image -DskipTests
```

for native container image

```bash
./mvnw clean -Pnative spring-boot:build-image -DskipTests
```

```bash
docker build --progress=plain -f Dockerfile.native -t deepaksorthiya/spring-boot-security-angular-client-csrf .
```

## (Optional)Running On Docker

```bash
docker run -p 8080:8080 --name spring-boot-security-angular-client-csrf deepaksorthiya/spring-boot-security-angular-client-csrf
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

if you want
to remove node packages during maven clean phase
add below plugin config in [pom.xml](pom.xml). It's already added.
you can use

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


