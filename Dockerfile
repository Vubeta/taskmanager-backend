# Dùng JRE 17 chính thức
FROM eclipse-temurin:17-jre

# Chỉ định file .jar cần copy
ARG JAR_FILE=build/libs/*.jar

# Copy file jar vào image
COPY ${JAR_FILE} app.jar

# Lệnh chạy app khi container khởi động
ENTRYPOINT ["java", "-jar", "/app.jar"]
