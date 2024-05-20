FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080
ADD target/fastfood-*.jar /opt/api.jar
ENTRYPOINT exec java $JAVA_OPTS $APPDYNAMICS -jar /opt/api.jar

