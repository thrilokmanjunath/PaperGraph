FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Ensure mvnw has execution permissions
RUN chmod +x mvnw
# Download dependencies
RUN ./mvnw dependency:go-offline
COPY src ./src
# Build the application
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx300m", "-Xss512k", "-XX:CICompilerCount=2", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]
