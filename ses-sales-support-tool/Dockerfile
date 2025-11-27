## Multi-stage build for Spring Boot (Java 17)
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /workspace

# Copy sources
COPY pom.xml ./
COPY src ./src

# Build (skip tests for faster image build)
RUN mvn -q -DskipTests package

# Runtime image
FROM eclipse-temurin:17-jre

ENV TZ=Asia/Tokyo \
    JAVA_OPTS=""

WORKDIR /app

# Copy fat jar
COPY --from=builder /workspace/target/ses-sales-support-tool-*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


