FROM azul/zulu-openjdk-alpine:11-jre

COPY build/libs/*.jar dictionary-service.jar
EXPOSE 8015 9015

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar dictionary-service.jar"]
