FROM maven 
 
ADD pom.xml /pom.xml
COPY ./src /src
COPY ./config /config


EXPOSE 8080:8080
  
CMD mvn clean package jetty:run 
