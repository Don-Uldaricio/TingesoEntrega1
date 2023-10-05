FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} entrega1-0.0.1-SNAPSHOT.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar","/entrega1-0.0.1-SNAPSHOT.jar"]