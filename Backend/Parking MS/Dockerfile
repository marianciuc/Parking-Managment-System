FROM openjdk:22-jdk-slim
LABEL authors="vladimirmarancuk"
WORKDIR /app
COPY target/parking-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 7022
ENTRYPOINT ["java", "-jar", "app.jar"]
