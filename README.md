# 🎓 GestionSaberPro

Sistema web para la gestión y seguimiento de los resultados del examen Saber Pro - UTS.

## 🛠 Tecnologías
- Java 17 + Spring Boot 3.2.5
- Spring Security + Spring Data JPA
- PostgreSQL
- Thymeleaf + Bootstrap 5
- Apache POI (exportación Excel)
- Docker

## 👥 Roles del Sistema
| Rol | Funcionalidades |
|---|---|
| Administrador | CRUD Coordinadores, Docentes, Facultades |
| Coordinador | Gestión alumnos, calificar, aprobar pagos, informes, resolución |
| Docente | Buscar alumnos, ver informes y beneficios |
| Estudiante | Ver resultados, registrar pago, ver beneficios |

## 🚀 Ejecutar localmente
1. Crear base de datos PostgreSQL: `gestion_saberpro`
2. Configurar `application-local.properties` con tus credenciales
3. Ejecutar con perfil local:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## 🐳 Docker
```bash
docker build -t gestion-saberpro .
docker run -p 8081:8081 \
  -e DATABASE_URL=jdbc:postgresql://host/db \
  -e DB_USERNAME=user \
  -e DB_PASSWORD=pass \
  gestion-saberpro
```

## ☁️ Despliegue en Render
Ver sección de despliegue más abajo.
