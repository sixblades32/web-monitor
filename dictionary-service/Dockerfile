FROM azul/zulu-openjdk-alpine:11-jre

# Install prerequisites
RUN apt-get update && apt-get install -y curl

COPY build/libs/*.jar dictionary-service.jar
EXPOSE 8015 9015

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar dictionary-service.jar"]
