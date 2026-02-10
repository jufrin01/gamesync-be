# --- Stage 1: Build (Mengubah kode Java jadi .jar) ---
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- Stage 2: Run (Menjalankan aplikasi) ---
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]