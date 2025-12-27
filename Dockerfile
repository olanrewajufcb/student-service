
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/student-service.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
