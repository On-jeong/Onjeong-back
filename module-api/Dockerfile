FROM openjdk:11-jdk
LABEL maintainer="email"
ARG JAR_FILE=module-api/build/libs/module-api-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} module-api.jar
ENTRYPOINT ["java","-jar","/module-api.jar"]