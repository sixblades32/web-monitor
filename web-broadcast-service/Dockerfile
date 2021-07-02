FROM azul/zulu-openjdk-alpine:11-jre

COPY target/*.jar web-broadcast-service.jar
EXPOSE 8020 9020

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar web-broadcast-service.jar"]