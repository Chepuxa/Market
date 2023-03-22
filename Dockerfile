FROM maven:3.9.0-eclipse-temurin-11-alpine as builder
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
COPY src /build/src
RUN mvn clean package -DskipTests=true
FROM eclipse-temurin:11-alpine
COPY --from=builder /build/target/*.jar market.jar
ENTRYPOINT ["java", "-jar", "market.jar"]