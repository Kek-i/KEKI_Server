FROM openjdk:11-jre
COPY build/libs/keki-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

RUN sudo ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
