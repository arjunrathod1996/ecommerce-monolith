# Java 17 runtime
FROM eclipse-temurin:17-jre

# Working directory inside the container
WORKDIR /app

# Copy the generated JAR file
COPY target/*.jar app.jar

# Application port
EXPOSE 8080

# Start Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]