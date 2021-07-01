FROM azul/zulu-openjdk-alpine:11-jre

COPY target/*.jar twitter-monitor.jar
EXPOSE 8030 9030

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar twitter-monitor.jar"]