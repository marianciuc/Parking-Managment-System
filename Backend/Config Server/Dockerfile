FROM openjdk:22-jdk-slim
LABEL authors="Vladimir Marianciuc"

ENV SPRING_EUREKA_SERVICE_HOST=localhost
ENV SPRING_EUREKA_SERVICE_PORT=8761
ENV ZIPKIN_BASE_URL=http://zipkin-server:9411
ENV SPRING_APPLICATION_NAME=config-server
ENV SERVER_PORT=8888

WORKDIR /app
COPY target/config-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]