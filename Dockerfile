FROM openjdk:11-jre
COPY build/libs/keki-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]