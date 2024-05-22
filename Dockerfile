FROM eclipse-temurin:21-jdk-alpine

ARG OIDC_PROVIDER_ISSUER_URI
ENV OIDC_PROVIDER_ISSUER_URI $OIDC_PROVIDER_ISSUER_URI

EXPOSE 8080
ADD target/fastfood-*.jar /opt/api.jar
ENTRYPOINT exec java $JAVA_OPTS $APPDYNAMICS -jar /opt/api.jar

