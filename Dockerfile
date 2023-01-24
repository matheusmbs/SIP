FROM ubuntu:latest
RUN \
apt-get update -y && \
apt-get install default-jre  x11-apps -y

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
CMD java -e DISPLAY=unix$DISPLAY -Xmx4g -jar /app.jar