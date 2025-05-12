# Backend Dockerfile
FROM maven:3.9-eclipse-temurin-17 as build

WORKDIR /app

# Copy pom.xml
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Production stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"] 