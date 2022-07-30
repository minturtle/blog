FROM adoptopenjdk/openjdk11

MAINTAINER Minseok kim <halstjri@naver.com>

# default user
ENV USER serve

EXPOSE 8080

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# packages install
RUN apt-get update && apt-get upgrade -y
RUN apt-get install -y sudo vim net-tools ssh openssh-server


# Access Option
RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config
RUN sed -i 's/UsePAM yes/#UserPAM yes/g' /etc/ssh/sshd_config

#user add & set
RUN groupadd -g 999 $USER
RUN useradd -m -r -u 999 -g $USER $USER

RUN sed -ri '20a'$USER'    ALL=(ALL) NOPASSWD:ALL' /etc/sudoers

#set root & user passwd
RUN echo 'root:root' | chpasswd
RUN echo $USER':serve123' | chpasswd


RUN chmod +x ./gradlew
RUN ./gradlew Bootjar

USER $USER

WORKDIR /build/libs
ENTRYPOINT ["java", "-jar", "blog-0.0.1-SNAPSHOT.jar"]