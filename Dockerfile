FROM openjdk:17

WORKDIR /app

COPY target/taskmanagementsystem-0.0.1-SNAPSHOT.jar Task-Management-System.jar

COPY .env .env

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Task-Management-System.jar"]