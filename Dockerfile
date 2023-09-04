# Docker image for springboot file run
# VERSION 1.0.0
# Author: liujh-r

FROM java:8

COPY *.jar /team4.jar

CMD ["--server.port=8060"]

EXPOSE 8060

ENTRYPOINT ["java", "-jar", "/team4.jar"]