FROM azul/zulu-openjdk-alpine:11-jre

# Install prerequisites
RUN apk --no-cache add curl

COPY build/libs/*.jar discord-broadcast-service.jar
EXPOSE 9085

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar discord-broadcast-service.jar"]
