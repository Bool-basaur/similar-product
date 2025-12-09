FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 5000

ENV EXTERNAL_BASE_URL=http://localhost:3001

ENTRYPOINT ["java", "-jar", "app.jar"]