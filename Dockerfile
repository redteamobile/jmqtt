# Builder
FROM maven:3-jdk-8-slim AS builder 
RUN mkdir /opt/jmqtt
WORKDIR /opt/jmqtt 
ADD . ./ 
RUN mvn -Ppackage-all -DskipTests clean install -U 

# App
FROM reg.redtea.io/devops/rt-openjdk-base:8-jre-alpine-openj9
MAINTAINER RedteaMobile <devops@redteamobile.com>  
COPY --from=builder  /opt/jmqtt/controller/target/jmqtt.jar /app/app.jar
RUN /bin/sh -c 'java -Xscmx64M -Xshareclasses -Xquickstart -jar /app/app.jar &' ; sleep 10 ; ps -ef |grep java|grep -v grep|awk '{print $1}'|xargs kill -1 
