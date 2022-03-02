FROM openjdk:17-ea-3-jdk-oraclelinux8

RUN mkdir /opt/target/

COPY target/hlf-chaincode-definition-creator-1.0-jar-with-dependencies.jar /opt/target/hlf-chaincode-definition-creator-1.0-jar-with-dependencies.jar
COPY create-archive.sh /opt/create-archive.sh

# CMD cd /opt/ && /opt/create-archive.sh asset-transfer-basic.org2.example.com 30489 basic_1.0 kubernetes.research.dev.seeburger.de:30080

CMD ["sh", "-c", "echo start; tail -f /dev/null"]
