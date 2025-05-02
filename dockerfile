FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace/backapp
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x ./mvnw && ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre-alpine
VOLUME [ "/tmp" ]
COPY --from=build /workspace/backapp/target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]