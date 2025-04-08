# Step 1: Use the official OpenJDK image as the base image
FROM openjdk:17-jdk-slim as build

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the Spring Boot application JAR file into the container
COPY target/reactive_project-0.0.1-SNAPSHOT.jar /app/reactive_project-0.0.1-SNAPSHOT.jar

# Step 4: Expose the port that your app will run on (default for Spring Boot is 8080)
EXPOSE 8080

# Step 5: Run the application with the `java -jar` command
ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]
