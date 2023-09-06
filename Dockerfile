# Docker image for springboot file run
# VERSION 1.0.0
# Author: liujh-r

FROM openjdk:8

# 作者信息
MAINTAINER liujh-r <liujh-r@glodon.com>

# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
# VOLUME /tmp

# 将jar包添加到容器中并更名
# ADD team4-0.0.1-SNAPSHOT.jar team4.jar

CMD ["--server.port=8060"]

EXPOSE 8060

# 运行jar包
RUN bash -c 'touch /team4-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java", "-jar","/team4-0.0.1-SNAPSHOT.jar"]