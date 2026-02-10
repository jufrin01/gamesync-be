# --- Stage 1: Build ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8000

# --- PERUBAHAN PENTING DI SINI ---
# Tambahkan -Xms256m -Xmx350m agar Java tidak memakan RAM lebih dari jatah gratisan
ENTRYPOINT ["java", "-Xms256m", "-Xmx350m", "-jar", "/app.jar"]