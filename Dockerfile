FROM maven:3-openjdk-17 AS builder
WORKDIR /workdir/server
COPY pom.xml /workdir/server/pom.xml
#RUN mvn dependency:go-offline
COPY src /workdir/server/src
RUN mvn package

FROM openjdk:17-ea-17-jdk as prod
WORKDIR /workdir/lib
ENV LOG_HOME=/workdir/logs
EXPOSE 8080
EXPOSE 18080
EXPOSE 1099
VOLUME /tmp
COPY --from=0  /workdir/server/target/australia-call-center-0.0.1-SNAPSHOT.jar ./app.jar
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.port=1099", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dlog-home=${LOG_HOME}/app", "-Dserver.tomcat.accesslog.directory=${LOG_HOME}/tomcat", "./app.jar"]