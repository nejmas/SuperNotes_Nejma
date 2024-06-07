# Use the Gradle image as base
FROM gradle:8.4.0-jdk21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR files into the container
COPY app/build/libs/*.jar /app/

# Command to run the Java application
CMD ["sh", "-c", "java -jar app-all.jar"]

# Alternatively, if you want to use app.jar:
# CMD ["java", "-jar", "app.jar"]
