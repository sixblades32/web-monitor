FROM azul/zulu-openjdk-alpine:11-jre

COPY build/libs/*.jar auth-service.jar
EXPOSE 8010 9010

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar auth-service.jar"]