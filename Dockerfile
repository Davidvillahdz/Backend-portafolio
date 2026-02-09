# ============================================
# ETAPA 1: BUILD (Construcción)
# ============================================
# Usamos una imagen con Gradle y JDK 17 listo para compilar
FROM gradle:8.5-jdk17-alpine AS builder

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero los archivos de configuración de Gradle
# (Esto permite que Docker guarde en caché las dependencias si no cambian)
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle

# Descargamos dependencias (sin compilar código aún)
RUN gradle dependencies --no-daemon

# Ahora sí, copiamos el código fuente
COPY src ./src

# Compilamos la aplicación y generamos el JAR (saltando los tests para ir rápido)
RUN gradle build -x test --no-daemon

# ============================================
# ETAPA 2: RUNTIME (Ejecución)
# ============================================
# Usamos una imagen ligera solo con Java (sin Gradle, para que pese menos)
FROM eclipse-temurin:17-jre-alpine

# Directorio de trabajo
WORKDIR /app

# Copiamos SOLO el archivo .jar generado en la etapa anterior
# Gracias a tu configuración en build.gradle, se llamará app.jar
COPY --from=builder /app/build/libs/app.jar app.jar

# Exponemos el puerto 8080 (informativo para Render)
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]