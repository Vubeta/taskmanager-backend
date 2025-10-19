# --- Giai đoạn 1: Build (Build Stage) ---
# Sử dụng base image có JDK 17 (theo yêu cầu
# của build.gradle) để build ứng dụng.
# 'jammy' dựa trên Ubuntu 22.04.
FROM eclipse-temurin:17-jdk-jammy AS builder

# Đặt thư mục làm việc
WORKDIR /app

# 1. Copy các file build và Gradle wrapper
# Tận dụng Docker cache: Chỉ download lại dependencies
# khi các file này thay đổi.
COPY gradlew .
COPY gradle /app/gradle
COPY build.gradle .
COPY settings.gradle .

# 2. Cấp quyền thực thi cho Gradle wrapper
RUN chmod +x ./gradlew

# 3. Tải dependencies về
# --no-daemon tốt cho môi trường CI/CD và Docker
RUN ./gradlew dependencies --no-daemon

# 4. Copy mã nguồn
COPY src /app/src

# 5. Build ứng dụng
# Task 'build' sẽ tự động chạy 'test' và 'bootJar'
RUN ./gradlew build -x test --no-daemon

# 6. Tạo thư mục để giải nén các layer của JAR
RUN mkdir -p build/extracted-jar
# Giải nén JAR thành các layer riêng biệt
# (yêu cầu cấu hình 'layered()' trong build.gradle)
RUN java -Djarmode=layertools -jar build/libs/*.jar extract --destination build/extracted-jar


# --- Giai đoạn 2: Runtime (Final Stage) ---
# Sử dụng một base image JRE (Java Runtime) mỏng nhẹ.
# Image này không chứa JDK, giúp giảm kích thước
# và tăng cường bảo mật.
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 1. Tạo một user và group không phải root để chạy ứng dụng
# Đây là một Best Practice về bảo mật.
RUN groupadd --gid 1001 appuser && \
    useradd --uid 1001 --gid 1001 --shell /bin/bash --create-home appuser

# 2. Copy các layer đã được giải nén từ giai đoạn 'builder'
# Copy theo thứ tự từ ít thay đổi nhất (dependencies)
# đến thay đổi nhiều nhất (application)
COPY --from=builder /app/build/extracted-jar/dependencies/ ./
COPY --from=builder /app/build/extracted-jar/spring-boot-loader/ ./
COPY --from=builder /app/build/extracted-jar/snapshot-dependencies/ ./
COPY --from=builder /app/build/extracted-jar/application/ ./

# 3. Chuyển sang user không phải root
USER appuser

# 4. Expose port 8080 (port mặc định của Spring Boot)
EXPOSE 8080

# 5. Entrypoint để chạy ứng dụng
# 'JarLauncher' là trình khởi chạy khi sử dụng layered JARs
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]