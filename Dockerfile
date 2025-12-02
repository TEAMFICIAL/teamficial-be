FROM amazoncorretto:17-alpine-jdk
COPY ./build/libs/teamficial_be-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar",  "/app.jar" ]