# Build stage
FROM maven:3.6.3-openjdk-11 as builder
WORKDIR /opt/app
COPY ./src ./src
COPY pom.xml ./
RUN mvn clean install -DskipTests

# Package stage
FROM openjdk:11
WORKDIR /opt/app
RUN addgroup quotes_service_group; adduser --ingroup quotes_service_group --disabled-password quotes_service_user
USER quotes_service_user
COPY --from=builder /opt/app/target/quotes-service.jar quotes-service.jar
ENTRYPOINT ["java", "-jar", "quotes-service.jar"]