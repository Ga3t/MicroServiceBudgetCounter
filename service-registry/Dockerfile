FROM openjdk:21
COPY target/service-registry-0.0.1-SNAPSHOT.jar service-registry-0.0.1-SNAPSHOT.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/service-registry-0.0.1-SNAPSHOT.jar"]