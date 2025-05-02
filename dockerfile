FROM eclipse-temurin:23-jdk-alpine as build

WORKDIR /workspace/backapp

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:23-jre-alpine
VOLUME [ "/tmp" ]
COPY --from=build /workspace/backapp/target/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]