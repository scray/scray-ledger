FROM eclipse-temurin:11-jre-focal

EXPOSE 8080

COPY audit-rest-api/target/audit-rest-api-0.0.1-SNAPSHOT.jar  /opt/audit-rest-api.jar

ENTRYPOINT ["java", "-jar", "/opt/audit-rest-api.jar", "--wallet",  "/etc/secret-conf-vol/"]
