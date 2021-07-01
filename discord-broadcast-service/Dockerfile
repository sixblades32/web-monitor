FROM azul/zulu-openjdk-alpine:11-jre

COPY target/*.jar discord-broadcast-service.jar
EXPOSE 8085 9085

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar discord-broadcast-service.jar"]