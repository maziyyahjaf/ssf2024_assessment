FROM eclipse-temurin:23-jdk AS builder

ARG COMPILE_DIR=/code_folder

WORKDIR ${COMPILE_DIR}

# Copy all necessary files for Maven build
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy the source code
COPY src src

# Build the project with Maven, skipping the tests
RUN ./mvnw clean package -Dmaven.test.skip=true

# Expose the server port as an environment variable
ENV SERVER_PORT=8080

# Expose the port for external access
EXPOSE ${SERVER_PORT}

# Entrypoint to run the application
ENTRYPOINT ["java",  "-jar", "target/noticeboard-0.0.1-SNAPSHOT.jar"]

# Stage 2: Production stage
FROM eclipse-temurin:23-jdk

# Argument for the directory where the final JAR will be deployed
ARG DEPLOY_DIR=/APP

# Copy the build JAR file from the build stage to the production stage
COPY --from=builder /code_folder/target/noticeboard-0.0.1-SNAPSHOT.jar noticeboard.jar

# Expose the server port as an environment variable
ENV SERVER_PORT=8080

HEALTHCHECK --interval=60s --start-period=120s \
CMD curl http://localhost:${SERVER_PORT}/status || exit 1

# Entrypoint to run the final applicatiom
ENTRYPOINT ["java", "-jar",  "noticeboard.jar"]
CMD ["--server.port=8080"]