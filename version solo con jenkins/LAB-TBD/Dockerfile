FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} LAB-TBD-0.0.1-SNAPSHOT.jar
EXPOSE 8097
ENTRYPOINT ["java","-jar","/LAB-TBD-0.0.1-SNAPSHOT.jar"]
