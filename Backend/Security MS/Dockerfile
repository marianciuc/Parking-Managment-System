FROM openjdk:22-jdk-slim
LABEL authors="Vladimir Marianciuc"

ENV SPRING_PORT=8022
ENV SPRING_CONFIG_SERVER_URL=http://config-server:8888
ENV SPRING_APPLICATION_NAME=auth-service

WORKDIR /app
COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8022

ENTRYPOINT ["sh", "-c", "sleep 50 && exec java -jar app.jar"]