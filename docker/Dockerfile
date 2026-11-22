FROM maven:3.10.0-eclipse-temurin-25 AS build

WORKDIR /workspace
COPY app/pom.xml app/pom.xml
COPY app/src app/src
RUN mvn -f app/pom.xml --batch-mode --no-transfer-progress package -DskipTests

FROM eclipse-temurin:25-jre-alpine AS runtime

WORKDIR /app
RUN addgroup -S app && adduser -S -G app app
COPY --from=build --chown=app:app /workspace/app/target/task-manager.jar /app/task-manager.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/task-manager.jar"]
