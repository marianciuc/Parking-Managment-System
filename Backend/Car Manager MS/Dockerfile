FROM openjdk:22-jdk-slim
LABEL authors="Vladimri Marianciuc"
WORKDIR /app
COPY target/cars-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8009
ENTRYPOINT ["sh", "-c", "sleep 50 && exec java -jar app.jar"]