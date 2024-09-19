# build stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY . .
RUN mvn clean install -DskipTests=true
# Kiểm tra xem file JAR có tồn tại
RUN ls -la /app/target

# run stage
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/exe201be-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/src/main/resources/templates ./templates
# Kiểm tra xem file JAR có được copy đúng cách
RUN ls -la /app

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
