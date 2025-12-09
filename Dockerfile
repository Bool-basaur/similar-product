FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY pom.xml mvnw* ./
COPY .mvn .mvn
RUN mvn -B -o -e -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -T 1C clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 5000

HEALTHCHECK --interval=10s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:5000/actuator/health || exit 1

ENTRYPOINT ["java","-jar","/app/app.jar"]
