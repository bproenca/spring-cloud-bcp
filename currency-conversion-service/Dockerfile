FROM openjdk:13-jdk-alpine
VOLUME /tmp
EXPOSE 8100
ADD target/*.jar app.jar
ENV JAVA_OPTS=""
ENV SPRING_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar $SPRING_OPTS" ]