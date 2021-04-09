FROM openjdk:8-jdk-alpine
MAINTAINER wanderer.ge


COPY gradlew .
COPY build.gradle .
COPY gradle.properties .
COPY src src

#RUN ./gradlew import
RUN ./gradlew build

#COPY build/libs/wanderer-user-content-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/build/libs/wanderer-user-content-1.0-SNAPSHOT.jar"]