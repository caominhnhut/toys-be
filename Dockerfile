FROM maven:3.6.0-jdk-8-alpine as build

RUN mkdir -p /build
WORKDIR /build

COPY ./ ./

RUN mvn clean package -DskipTests=true

FROM openjdk:8

WORKDIR /build

COPY --from=build /build/target/toys-be-0.0.1.jar ./toys-be-0.0.1.jar

# This line should be commented when debuging
ENTRYPOINT ["java", "-jar", "/build/toys-be-0.0.1.jar"]