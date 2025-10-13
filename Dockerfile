# ====== Build stage ======
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace

# Gradle 캐시 최적화: 의존성만 먼저 다운로드
# 아래 패턴은 build.gradle 또는 build.gradle.kts, settings.* 중 "존재하는 것만" 매치
COPY gradlew* settings.gradle* build.gradle* /workspace/
COPY gradle /workspace/gradle
RUN chmod +x gradlew && ./gradlew --no-daemon --version

# 의존성만 받아 캐시
RUN ./gradlew dependencies || true

# 실제 소스 복사 후 빌드
COPY src /workspace/src
# 테스트는 로컬에서 하니 이미지 빌드시 스킵(원하면 제거)
RUN ./gradlew bootJar -x test

# ====== Runtime stage ======
FROM eclipse-temurin:21-jre-alpine

# 보안상 non-root
RUN addgroup -S app && adduser -S app -G app
USER app

WORKDIR /app
COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar /app/app.jar
# 또는 확정 이름이면: COPY --from=build /workspace/build/libs/good-restaurant-*.jar /app/app.jar

# 헬스체크(엔드포인트 맞춰서 수정)
HEALTHCHECK --interval=30s --timeout=3s --start-period=20s CMD wget -qO- http://localhost:8080/actuator/health | grep -q UP || exit 1

EXPOSE 8080
ENV SERVER_PORT=8080 \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseZGC"

ENTRYPOINT ["java","-jar","/app/app.jar"]
