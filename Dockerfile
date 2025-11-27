FROM eclipse-temurin:17-jdk


WORKDIR /app

COPY target/Smart-Task-Manager-0.0.1-SNAPSHOT.jar STM.jar

LABEL authors="amansingh"

# Copy React built files into Spring Boot static folder
COPY src/main/resources/static/ /app/static/

ENTRYPOINT ["java", "-jar", "STM.jar"]