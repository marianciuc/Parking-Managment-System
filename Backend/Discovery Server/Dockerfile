FROM openjdk:22-jdk-slim
LABEL authors="Vladimir Marianciuc"

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

ENV SPRING_CONFIG_SERVER_URL=http://config-server:8888
ENV SPRING_APPLICATION_NAME=discovery-service

WORKDIR /app
COPY target/discovery-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8761

ENTRYPOINT ["sh", "-c", "sleep 25 && exec java -jar app.jar"]

