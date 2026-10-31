# syntax=docker/dockerfile:1

# Stage 1: build the executable JAR with Maven on JDK 21 (tests run in CI, so they are skipped here)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY HospitalManagementSystem/pom.xml .
RUN mvn -B -q dependency:go-offline
COPY HospitalManagementSystem/src ./src
RUN mvn -B -q package -DskipTests

# Stage 2: slim Java 21 runtime with only the JAR
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --create-home appuser
COPY --from=build /build/target/*.jar app.jar
USER appuser
# Fits the 512 MB free tier on Render: capped heap, small thread stacks, serial GC.
# The demo profile (in-memory H2 with sample data) is the default; set SPRING_PROFILES_ACTIVE=
# and DB_URL / DB_USERNAME / DB_PASSWORD to use MySQL instead.
ENV JAVA_TOOL_OPTIONS="-Xmx300m -Xss512k -XX:+UseSerialGC -XX:MaxMetaspaceSize=128m" \
    SPRING_PROFILES_ACTIVE=demo \
    PORT=7000
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
