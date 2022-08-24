FROM eclipse-temurin:11-jre-focal

EXPOSE 8080

COPY event-rest-api/target/event-rest-api-0.0.1-SNAPSHOT.jar  /opt/event-rest-api.jar

ENTRYPOINT ["java", "-jar", "/opt/event-rest-api.jar", "--wallet",  "/etc/secret-conf-vol/"]
