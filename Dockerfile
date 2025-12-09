FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 5000

ENTRYPOINT ["java", "-jar", "app.jar"]
