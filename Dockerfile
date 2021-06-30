FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD /build/libs/wanderer-user-content-1.0-SNAPSHOT.jar wanderer-user-content.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "wanderer-user-content.jar"]