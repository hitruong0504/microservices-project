# Build stage
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

# Run stage
FROM amazoncorretto:21-alpine

RUN adduser -D cloudgateway

WORKDIR /run

COPY --from=build /app/target/*.jar /run/app.jar

RUN chown -R cloudgateway:cloudgateway /run

USER cloudgateway

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "/run/app.jar"]