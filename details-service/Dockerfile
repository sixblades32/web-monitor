FROM azul/zulu-openjdk-alpine:11-jre

COPY build/libs/*.jar details-service.jar
EXPOSE 8045 9045

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar details-service.jar"]
