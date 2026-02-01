
# Build stage

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# copy pom first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline

# copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# security: non-root user
RUN useradd -m appuser
USER appuser

COPY --from=build /app/target/order-management-system-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
