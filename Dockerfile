# Use Amazon Corretto 17 as the base image
FROM amazoncorretto:17

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/sfs-0.0.1-SNAPSHOT.jar app.jar

# Expose the default port your application runs on
EXPOSE 8080

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
