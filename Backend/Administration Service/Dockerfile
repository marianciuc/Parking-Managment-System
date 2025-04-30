FROM openjdk:22-jdk-slim
LABEL authors="Vladimir Marianciuc"
WORKDIR /app
COPY target/admin-ms-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8023
ENTRYPOINT ["sh", "-c", "sleep 50 && exec java -jar app.jar"]