FROM openjdk:19-alpine
ARG JAR_FILE
COPY target/rest-web-service-0.0.1-SNAPSHOT.jar /rest-web-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/rest-web-service-0.0.1-SNAPSHOT.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]