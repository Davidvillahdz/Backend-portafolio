# 🚀 DevPortfolio – Plataforma de Portafolios y Asesorías

Este repositorio contiene el código fuente del **Backend** para la plataforma de gestión de portafolios de programadores. Está construido con **Java 17** y **Spring Boot**, siguiendo una arquitectura RESTful y desplegado en la nube usando **Render**.

## 👥 Equipo del Proyecto

**Repositorio del proyecto:** [https://github.com/Juanfernando518/Proyecto--Interciclo.git](https://github.com/Juanfernando518/Proyecto--Interciclo.git)

### Integrantes
* **David Villa** - [GitHub: Davidvillahdz](https://github.com/Davidvillahdz)
* **Juan Alvarez** - [GitHub: Juanfernando518](https://github.com/Juanfernando518)

---

## 📖 Descripción del Sistema

El sistema gestiona la autenticación segura, la base de datos de proyectos, usuarios y el sistema de agendamiento de citas con notificaciones por correo electrónico.


## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17 (JDK 17)
* **Framework Principal:** Spring Boot 4.x
* **Seguridad:** Spring Security + JWT (JSON Web Tokens)
* **Base de Datos:** PostgreSQL (Producción en Render / Local en Docker)
* **ORM:** Spring Data JPA (Hibernate)
* **Email:** JavaMailSender (Gmail SMTP) con ejecución asíncrona (`@Async`)
* **Construcción:** Gradle (Kotlin DSL)
* **Despliegue:** Render (Web Service + Managed PostgreSQL)

---

## 📂 Estructura del Proyecto

El proyecto sigue una arquitectura en capas clásica:

* `config/`: Configuración de CORS, Seguridad, Filtro JWT y Beans de aplicación.
* `controller/`: Controladores REST (`Auth`, `Usuario`, `Proyecto`, `Asesoria`, `Admin`).
* `service/`: Lógica de negocio (`EmailService`, `AsesoriaService`, `ProyectoService`, etc.).
* `repository/`: Interfaces para la comunicación con la Base de Datos (JPA).
* `entity/`: Modelos de base de datos (`Usuario`, `Proyecto`, `Asesoria`, `Rol`).
* `dto/`: Objetos de transferencia de datos (`LoginRequest`, `RegisterRequest`, `ProyectoDto`).

---

## ⚙️ Configuración y Variables de Entorno

Para ejecutar este proyecto (tanto en local como en Render), es necesario configurar las siguientes variables de entorno en el archivo `application.yml` o en el panel de control del servidor:

| Variable | Descripción | Ejemplo |
| :--- | :--- | :--- |
| `DB_URL` | URL de conexión a PostgreSQL | `jdbc:postgresql://host:5432/db_name` |
| `DB_USERNAME` | Usuario de la base de datos | `postgres` / `ups` |
| `DB_PASSWORD` | Contraseña de la base de datos | `secret123` |
| `JWT_SECRET` | Clave para firmar los tokens | (Cadena Hexadecimal Larga) |
| `SPRING_MAIL_USERNAME` | Correo remitente (Gmail) | `tu-correo@gmail.com` |
| `SPRING_MAIL_PASSWORD` | Contraseña de aplicación | (16 caracteres generados por Google) |

> **Nota:** La configuración de correo incluye `starttls.required` y `timeouts` de 15000ms para evitar bloqueos en el despliegue en la nube.

---

## 🔌 Endpoints Principales (API)

### 🔐 Autenticación (`/api/auth`)
* `POST /register`: Registrar un nuevo usuario (Rol: USUARIO o PROGRAMADOR).
* `POST /login`: Iniciar sesión y obtener el **Bearer Token**.

### 👤 Usuarios (`/api/usuarios`)
* `GET /programadores`: Listar todos los expertos disponibles (Público).
* `GET /me`: Obtener perfil del usuario autenticado.
* `PUT /me`: Actualizar perfil (Foto, Descripción, Horario, Modalidad).

### 📂 Proyectos (`/api/proyectos`)
* `POST /`: Crear un nuevo proyecto (Requiere Rol PROGRAMADOR).
* `GET /programador/{id}`: Ver los proyectos de un experto específico.
* `DELETE /{id}`: Eliminar un proyecto propio.

### 📅 Asesorías (`/api/asesorias`)
* `POST /`: Solicitar una cita (Dispara correo automático al programador).
* `GET /recibidas`: Ver solicitudes pendientes (Para programadores).
* `PUT /{id}/responder`: Aceptar o Rechazar cita (Dispara correo al cliente con link de WhatsApp si es aceptada).

### 🛡️ Admin (`/api/admin`)
* `GET /stats`: Ver estadísticas de la plataforma (Usuarios totales, Citas, etc.).

---

## 🚀 Despliegue en Producción

El backend se encuentra desplegado en **Render**.

1.  **Base de Datos:** PostgreSQL gestionada en Render (`portafolio_db_mugt`).
2.  **Servicio Web:** Conectado al repositorio de GitHub, con despliegue automático en cada `push`.
3.  **CORS:** Configurado para aceptar peticiones únicamente desde:
    * `http://localhost:4200` (Desarrollo)
    * `https://portfolio-integrador-31c6f.web.app` (Producción en Firebase)

---

## 🧪 Ejecución Local

1.  Clonar el repositorio.
2.  Asegurarse de tener PostgreSQL corriendo en el puerto 5432.
3.  Ejecutar el comando de Gradle:

```bash
./gradlew bootRun