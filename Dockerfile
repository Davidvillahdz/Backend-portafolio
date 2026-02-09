# ============================================
# ETAPA 1: BUILD (Usando tu propio Gradle Wrapper)
# ============================================
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# 1. Copiamos TODO el proyecto de una vez
COPY . .

# 2. ¡IMPORTANTE! Damos permisos de ejecución al instalador de Gradle
# (Esto es vital porque al subir desde Windows a veces se pierden)
RUN chmod +x gradlew

# 3. Construimos la aplicación usando TU versión de Gradle
RUN ./gradlew bootJar --no-daemon

# ============================================
# ETAPA 2: RUNTIME (Ejecución)
# ============================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el JAR generado (asegurándonos de que se llame app.jar)
COPY --from=build /app/build/libs/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]