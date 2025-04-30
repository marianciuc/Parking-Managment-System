FROM openjdk:22-jdk-slim
LABEL authors="Vladmimir Marianciuc"
WORKDIR /app
COPY target/payments-ms-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8015
ENTRYPOINT ["sh", "-c", "sleep 50 && exec java -jar app.jar"]