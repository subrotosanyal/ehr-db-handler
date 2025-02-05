# Use the official OpenJDK image as a base
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built jar file
COPY build/libs/ehr-db-handler-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
