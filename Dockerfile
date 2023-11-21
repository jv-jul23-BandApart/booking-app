#Builder stage
FROM openjdk:17-jdk-slim as builder
WORKDIR booking-app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} booking-app.jar
RUN java -Djarmode=layertools -jar booking-app \
  .jar extract

#Final stage
FROM openjdk:17-jdk-slim
WORKDIR booking-app
COPY --from=builder booking-app/dependencies/ ./
COPY --from=builder booking-app/spring-boot-loader/ ./
COPY --from=builder booking-app/snapshot-dependencies/ ./
COPY --from=builder booking-app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
EXPOSE 8080
