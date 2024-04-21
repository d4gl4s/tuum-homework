FROM openjdk:17-jdk-alpine

VOLUME /tmp
EXPOSE 8080

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
