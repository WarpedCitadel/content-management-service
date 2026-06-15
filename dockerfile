FROM maven:3.9-amazoncorretto-25-alpine AS builder

WORKDIR /app

COPY .mvn/ .mvn

COPY mvnw pom.xml .

RUN chmod +x ./mvnw

COPY src/main ./src/main

RUN ./mvnw clean package

FROM amazoncorretto:25-alpine AS runner

WORKDIR /app

COPY --from=builder /app/target/content-management-service-0.0.1-SNAPSHOT.jar content-management-service-0.0.1-SNAPSHOT.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar", "/app/content-management-service-0.0.1-SNAPSHOT.jar"]