# Sistema de Gestión de Reservas de Tours - Río de Janeiro

## Descripción
Sistema de gestión de reservas para agencia de turismo en Río de Janeiro, basado en arquitectura de microservicios con Spring Boot. Permite gestionar reservas, embarques, comunicación con la agencia operadora y notificaciones automáticas por WhatsApp.

## Estudiante
- **Nombre:** Felipe Cortes
- **Asignatura:** Desarrollo FullStack I (DSY1103)
- **Proyecto:** Sistema de Reservas de Tours
- **GitHub:** https://github.com/felipecortesf/sistema-reservas-tours
- **Trello:** https://trello.com/b/PB5uIzQ0/sistema-reservas-tours-dsy1103

## Arquitectura
El sistema está compuesto por 12 microservicios (10 de negocio + 2 de infraestructura):

### Microservicios de Negocio
| Microservicio | Puerto | Base de Datos | Descripción |
|--------------|--------|---------------|-------------|
| ms-usuarios | 8081 | usuarios_db | Gestión de usuarios y roles |
| ms-catalogo-tours | 8082 | catalogo_db | Catálogo de 16 tours de Río de Janeiro |
| ms-reservas | 8083 | reservas_db | Gestión de reservas con notificación WhatsApp automática |
| ms-notificaciones | 8084 | notificaciones_db | Envío de notificaciones por WhatsApp |
| ms-embarques | 8085 | embarques_db | Gestión de embarques y retrasos |
| ms-comunicacion-agencia | 8086 | agencia_db | Canal de comunicación Cliente-Kary-Felipe |
| ms-reportes | 8087 | reportes_db | Reportes diarios automáticos a las 22h |
| ms-whatsapp | 8088 | whatsapp_db | Integración Twilio WhatsApp Business API |
| ms-notificaciones-push | 8089 | notificaciones_push_db | Notificaciones push a clientes |
| ms-pagos | 8090 | pagos_db | Gestión de pagos con confirmación y métodos de pago |

### Microservicios de Infraestructura
| Microservicio | Puerto | Descripción |
|--------------|--------|-------------|
| ms-eureka | 8761 | Servidor de registro y descubrimiento de servicios |
| ms-gateway | 8080 | Punto de entrada único (API Gateway) |

## Comunicación entre Microservicios (OpenFeign)
- ms-reservas → ms-catalogo-tours
- ms-comunicacion-agencia → ms-reservas
- ms-notificaciones → ms-reservas
- ms-embarques → ms-catalogo-tours
- ms-pagos → ms-reservas

## Tecnologías
- Java 25
- Spring Boot 3.5.14
- Spring Cloud (Eureka, Gateway, OpenFeign)
- MySQL 8.4
- Flyway Migration
- Lombok
- SLF4J
- Twilio WhatsApp API
- Springdoc OpenAPI 2.8.8

## Funcionalidades Implementadas
- CRUD completo en todos los microservicios
- Patrón CSR (Controller-Service-Repository)
- Validaciones con Bean Validation (JSR 380)
- Manejo centralizado de errores con @ControllerAdvice
- Logs estructurados con SLF4J
- Comunicación entre microservicios con OpenFeign
- Notificaciones automáticas WhatsApp a las 13h hora Brasil
- Reporte diario automático a las 22h
- Flyway para migraciones de base de datos
- Eureka para Service Discovery
- API Gateway como punto de entrada único

## Requisitos Previos
- Java 25
- MySQL 8.4
- Maven 3.9+

## Pasos para Ejecutar

### 1. Crear bases de datos en MySQL
```sql
CREATE DATABASE IF NOT EXISTS usuarios_db;
CREATE DATABASE IF NOT EXISTS catalogo_db;
CREATE DATABASE IF NOT EXISTS reservas_db;
CREATE DATABASE IF NOT EXISTS notificaciones_db;
CREATE DATABASE IF NOT EXISTS embarques_db;
CREATE DATABASE IF NOT EXISTS agencia_db;
CREATE DATABASE IF NOT EXISTS reportes_db;
CREATE DATABASE IF NOT EXISTS whatsapp_db;
CREATE DATABASE IF NOT EXISTS notificaciones_push_db;
CREATE DATABASE IF NOT EXISTS pagos_db;
```

### 2. Levantar los servicios en orden
```bash
# Terminal 1 - Eureka (levantar primero)
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

# Terminal 12 - Pagos
cd ms-pagos && ./mvnw spring-boot:run
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
- GET /api/v1/embarques/{id}
- GET /api/v1/embarques/fecha/{fecha}
- PUT /api/v1/embarques/{id}/retraso

### Pagos
- GET /api/v1/pagos
- GET /api/v1/pagos/{id}
- GET /api/v1/pagos/reserva/{reservaId}
- GET /api/v1/pagos/estado/{estado}
- POST /api/v1/pagos
- PUT /api/v1/pagos/{id}/confirmar
- DELETE /api/v1/pagos/{id}

### Comunicacion
- POST /api/v1/mensajes/cliente-pregunta
- POST /api/v1/mensajes/kary-responde
- POST /api/v1/mensajes/kary-alerta-felipe

### WhatsApp
- POST /api/v1/whatsapp/enviar
- GET /api/v1/whatsapp
