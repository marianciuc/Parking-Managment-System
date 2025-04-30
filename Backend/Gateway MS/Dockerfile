FROM openjdk:22-jdk-slim
LABEL authors="Vladimir Marianciuc"
WORKDIR /app

ENV ALLOWED_ORIGINS=http://localhost:5500,http://localhost:5173,https://localhost:5173,https://marianciuc.works

COPY target/Gateway-Service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8222
ENTRYPOINT ["sh", "-c", "sleep 80 && exec java -jar app.jar"]