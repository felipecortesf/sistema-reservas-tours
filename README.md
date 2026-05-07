# Sistema de Gestión de Reservas de Tours - Río de Janeiro

## Descripción
Sistema de gestión de reservas para agencia de turismo en Río de Janeiro, basado en arquitectura de microservicios con Spring Boot. Permite gestionar reservas, embarques, comunicación con la agencia operadora y notificaciones automáticas por WhatsApp.

## Estudiante
- **Nombre:** Felipe Cortes
- **Asignatura:** Desarrollo FullStack I (DSY1103)
- **Proyecto:** Sistema de Reservas de Tours

## Arquitectura
El sistema está compuesto por 11 microservicios independientes:

| Microservicio | Puerto | Descripción |
|--------------|--------|-------------|
| ms-eureka | 8761 | Servidor de registro y descubrimiento de servicios |
| ms-gateway | 8080 | Punto de entrada único (API Gateway) |
| ms-usuarios | 8081 | Gestión de usuarios y roles |
| ms-catalogo-tours | 8082 | Catálogo de tours disponibles |
| ms-reservas | 8083 | Gestión de reservas con notificación automática WhatsApp |
| ms-notificaciones | 8084 | Envío de notificaciones por WhatsApp |
| ms-embarques | 8085 | Gestión de embarques y retrasos |
| ms-comunicacion-agencia | 8086 | Canal de comunicación entre cliente, Kary y Felipe |
| ms-reportes | 8087 | Reportes diarios automáticos |
| ms-whatsapp | 8088 | Integración con WhatsApp Business API |
| ms-notificaciones-push | 8089 | Notificaciones push a clientes |

## Tecnologías
- Java 25
- Spring Boot 3.5.14
- Spring Cloud (Eureka, Gateway, OpenFeign)
- MySQL 9.6
- Flyway Migration
- Lombok
- SLF4J
- WhatsApp Business API

## Funcionalidades Implementadas
- CRUD completo en todos los microservicios
- Patrón CSR (Controller-Service-Repository)
- Validaciones con Bean Validation
- Manejo centralizado de errores con @ControllerAdvice
- Logs estructurados con SLF4J
- Comunicación entre microservicios con OpenFeign
- Notificaciones automáticas por WhatsApp a las 13h (hora Brasil)
- Reporte diario automático a las 22h
- Flyway para migraciones de base de datos
- Eureka para Service Discovery
- API Gateway como punto de entrada único

## Requisitos Previos
- Java 25
- MySQL 9.6
- Maven 3.9+

## Pasos para Ejecutar

### 1. Crear bases de datos en MySQL
```sql
CREATE DATABASE usuarios_db;
CREATE DATABASE reservas_db;
CREATE DATABASE catalogo_db;
CREATE DATABASE notificaciones_db;
CREATE DATABASE embarques_db;
CREATE DATABASE agencia_db;
CREATE DATABASE reportes_db;
CREATE DATABASE whatsapp_db;
CREATE DATABASE notificaciones_push_db;
```

### 2. Levantar los servicios en orden
```bash
# Terminal 1 - Eureka
cd ms-eureka && ./mvnw spring-boot:run

# Terminal 2 - Gateway
cd ms-gateway && ./mvnw spring-boot:run

# Terminal 3 - Usuarios
cd ms-usuarios && ./mvnw spring-boot:run

# Terminal 4 - Catalogo
cd ms-catalogo-tours && ./mvnw spring-boot:run

# Terminal 5 - Reservas
cd ms-reservas && ./mvnw spring-boot:run

# Terminal 6 - Notificaciones
cd ms-notificaciones && ./mvnw spring-boot:run

# Terminal 7 - Embarques
cd ms-embarques && ./mvnw spring-boot:run

# Terminal 8 - Comunicacion
cd ms-comunicacion-agencia && ./mvnw spring-boot:run

# Terminal 9 - Reportes
cd ms-reportes && ./mvnw spring-boot:run

# Terminal 10 - WhatsApp
cd ms-whatsapp && ./mvnw spring-boot:run

# Terminal 11 - Notificaciones Push
cd ms-notificaciones-push && ./mvnw spring-boot:run
```

### 3. Verificar en Eureka
Abrir: http://localhost:8761

## Endpoints Principales

### Usuarios
- GET /api/v1/usuarios
- GET /api/v1/usuarios/{id}
- POST /api/v1/usuarios
- DELETE /api/v1/usuarios/{id}

### Tours
- GET /api/v1/tours
- GET /api/v1/tours/activos
- GET /api/v1/tours/{id}
- POST /api/v1/tours
- PUT /api/v1/tours/{id}/reducir-cupo

### Reservas
- GET /api/v1/reservas
- GET /api/v1/reservas/{id}
- GET /api/v1/reservas/fecha/{fecha}
- POST /api/v1/reservas
- POST /api/v1/reservas/notificar

### Embarques
- GET /api/v1/embarques
- GET /api/v1/embarques/fecha/{fecha}
- PUT /api/v1/embarques/{id}/retraso

### Comunicacion
- POST /api/v1/mensajes/cliente-pregunta
- POST /api/v1/mensajes/kary-responde
- POST /api/v1/mensajes/kary-alerta-felipe
