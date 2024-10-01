# Use the official OpenJDK image as a base
FROM openjdk:22-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container
COPY target/smart-meter-server.jar /app/smart-meter-server.jar

# Expose the WebSocket port (change if necessary)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/smart-meter-server.jar"]