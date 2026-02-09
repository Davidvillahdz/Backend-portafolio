# ============================================
# ETAPA 1: BUILD (Construcción)
# ============================================
FROM gradle:8.5-jdk17-alpine AS builder
WORKDIR /app

# 🔥 CORRECCIÓN AQUÍ: Usamos asteriscos (*) 
# Esto hace que 'gradle.properties' sea opcional y detecta si usas .gradle o .gradle.kts
COPY build.gradle* settings.gradle* gradle.properties* ./

COPY gradle ./gradle

# Descargamos dependencias
RUN gradle dependencies --no-daemon

# Copiamos el código fuente
COPY src ./src

# Compilamos saltando los tests
RUN gradle build -x test --no-daemon

# ============================================
# ETAPA 2: RUNTIME (Ejecución)
# ============================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el JAR generado
COPY --from=builder /app/build/libs/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]