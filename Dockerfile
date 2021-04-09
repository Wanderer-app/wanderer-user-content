FROM openjdk:8-jdk-alpine
MAINTAINER wanderer.ge


COPY gradlew .
COPY src src

RUN ./gradlew import
RUN ./gradlew build
RUN ./gradlew test

#COPY build/libs/wanderer-user-content-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/build/libs/wanderer-user-content-1.0-SNAPSHOT.jar"]