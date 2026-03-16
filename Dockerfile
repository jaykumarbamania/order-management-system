
# Build stage

FROM maven:3.9.9-eclipse-temurin-21 AS build
ARG APP_VERSION
ENV APP_VERSION=$APP_VERSION
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

RUN useradd -ms /bin/bash appuser
USER appuser

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

## security: non-root user
#RUN useradd -m appuser
#USER appuser
#
#COPY --from=build /app/target/order-management-system-*.jar app.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]


#We use a multi-stage Docker build with Maven for dependency caching and a slim JRE runtime image. Runtime configuration like database credentials is injected via environment variables, making the image portable across local, EC2, and RDS environments.