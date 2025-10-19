# --- Giai đoạn 1: Build ---
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

# Copy Gradle wrapper và cấu hình
COPY gradlew .
COPY gradle /app/gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# Copy mã nguồn và build jar
COPY src /app/src
RUN ./gradlew clean bootJar -x test --no-daemon

# --- Giai đoạn 2: Runtime ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Tạo user không phải root (tùy chọn, giữ nguyên như cũ)
RUN groupadd --gid 1001 appuser && \
    useradd --uid 1001 --gid 1001 --shell /bin/bash --create-home appuser

# Copy file jar duy nhất
COPY --from=builder /app/build/libs/*.jar app.jar

USER appuser
EXPOSE 8080

# Chạy ứng dụng Spring Boot bình thường
ENTRYPOINT ["java","-jar","app.jar"]
