FROM azul/zulu-openjdk-alpine:11-jre

# Install prerequisites
RUN apk --no-cache add curl

COPY build/libs/*.jar staff-manager-bot.jar
EXPOSE 8025

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar staff-manager-bot.jar"]
