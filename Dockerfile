FROM openjdk:jre-alpine

ADD target/tyr-thorntail.jar /opt/thorntail.jar

EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/thorntail.jar -Djava.net.preferIPv4Stack=true
