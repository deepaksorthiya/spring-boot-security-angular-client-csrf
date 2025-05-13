# Stage 1: Build Stage
FROM bellsoft/liberica-runtime-container:jdk-21-stream-musl AS builder

WORKDIR /home/app
COPY . /home/app/spring-boot-security-angular-client-csrf
WORKDIR /home/app/spring-boot-security-angular-client-csrf
RUN  chmod +x mvnw && ./mvnw -Dmaven.test.skip=true clean package

# Stage 2: Layer Tool Stage
FROM bellsoft/liberica-runtime-container:jdk-21-cds-slim-musl AS optimizer

WORKDIR /home/app
COPY --from=builder /home/app/spring-boot-security-angular-client-csrf/target/*.jar spring-boot-security-angular-client-csrf.jar
RUN java -Djarmode=tools -jar spring-boot-security-angular-client-csrf.jar extract --layers --launcher

# Stage 3: Final Stage
FROM bellsoft/liberica-runtime-container:jre-21-stream-musl

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080
COPY --from=optimizer /home/app/spring-boot-security-angular-client-csrf/dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-security-angular-client-csrf/spring-boot-loader/ ./
COPY --from=optimizer /home/app/spring-boot-security-angular-client-csrf/snapshot-dependencies/ ./
COPY --from=optimizer /home/app/spring-boot-security-angular-client-csrf/application/ ./