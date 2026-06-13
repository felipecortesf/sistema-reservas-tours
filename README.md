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

## Seguridad — Spring Security + JWT + BCrypt

Sistema de autenticación centralizado en ms-usuarios, replicado mediante JWT compartido en ms-reservas y ms-pagos.

### Autenticación
- **BCrypt**: passwords encriptados antes de guardar en BD
- **JWT (JJWT 0.11.5)**: tokens firmados con HMAC-SHA256, expiración 10 horas
- **Endpoint de login**: `POST /api/v1/auth/login` (ms-usuarios) retorna `token`, `email` y `rol`

### Control de Acceso Basado en Roles (RBAC)

| Microservicio | Endpoint | Regla |
|---|---|---|
| ms-usuarios | `/api/v1/usuarios/**` | Solo `ADMIN` |
| ms-usuarios | `/api/v1/auth/**` | Público |
| ms-reservas | `GET /api/v1/reservas/**` | Cualquier autenticado |
| ms-reservas | `POST/PUT /api/v1/reservas/**` | `ADMIN`, `AGENTE` |
| ms-reservas | `DELETE /api/v1/reservas/**` | Solo `ADMIN` |
| ms-pagos | `GET /api/v1/pagos/**` | Cualquier autenticado |
| ms-pagos | `POST/PUT /api/v1/pagos/**` | `ADMIN`, `AGENTE` |
| ms-pagos | `DELETE /api/v1/pagos/**` | Solo `ADMIN` |

### Uso

```bash
# 1. Login
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@reservatours.com","password":"admin123"}'

# 2. Usar el token en peticiones protegidas
curl http://localhost:8083/api/v1/reservas \
  -H "Authorization: Bearer <token>"
```

## Comunicación Asíncrona — Apache Kafka

Comunicación basada en eventos entre ms-reservas y ms-notificaciones, complementando OpenFeign para desacoplamiento y consistencia eventual.

### Flujo
Al crear una reserva (`POST /api/v1/reservas`), ms-reservas publica un evento con los datos de la reserva. ms-notificaciones lo consume automáticamente y crea un registro de notificación pendiente con el mensaje de confirmación para el cliente.

### Configuración
- **Broker**: Kafka 4.3.0 (modo KRaft, sin Zookeeper) — `localhost:9092`
- **Topic**: `reserva.creada`
- **Serialización**: JSON (Spring Kafka JsonSerializer/JsonDeserializer)

### Levantar Kafka
```bash
/opt/homebrew/opt/kafka/bin/kafka-server-start /opt/homebrew/etc/kafka/server.properties
```

## Service Discovery y API Gateway

- **ms-eureka** (8761): todos los microservicios se registran automáticamente vía `@EnableDiscoveryClient`
- **ms-gateway** (8080): punto de entrada único, enruta a los 10 microservicios de negocio usando Service Discovery (`lb://nombre-servicio`)
- Los FeignClients usan descubrimiento por nombre (sin URLs hardcodeadas), excepto donde Eureka no está disponible

### Ejemplo
```bash
# A través del Gateway (puerto único 8080)
curl http://localhost:8080/api/v1/reservas
curl http://localhost:8080/api/v1/tours
curl http://localhost:8080/api/v1/pagos
```

## Pruebas Unitarias — JUnit 5 + Mockito

Tests unitarios de la capa de servicio con mocks de repositorios, cubriendo casos exitosos, casos de error (ResourceNotFoundException) y reglas de negocio.

| Microservicio | Tests | Casos destacados |
|---|---|---|
| ms-reservas | 7 | save() asigna estado CONFIRMADA por defecto, findById lanza excepción si no existe |
| ms-catalogo-tours | 7 | reducirCupo() con y sin stock disponible |
| ms-pagos | 7 | confirmarPago() cambia estado PENDIENTE → PAGADO |

### Ejecutar tests
```bash
cd ms-reservas && ./mvnw test
cd ms-catalogo-tours && ./mvnw test
cd ms-pagos && ./mvnw test
```

## Despliegue

El sistema corre localmente y se expone a internet mediante Ngrok, apuntando al API Gateway. Esto permite acceso real a los 10 microservicios de negocio a través de una sola URL pública.

```bash
ngrok http 8080
```

La URL generada (ej: `https://xxxx.ngrok-free.dev`) sirve como punto de acceso único:
```bash
curl https://xxxx.ngrok-free.dev/api/v1/tours
curl https://xxxx.ngrok-free.dev/api/v1/reservas -H "Authorization: Bearer <token>"
```

## Stack Tecnológico Completo

- Java 25, Spring Boot 3.5.14
- Spring Cloud: Eureka, Gateway, OpenFeign
- Spring Security + JJWT 0.11.5 + BCrypt
- Apache Kafka 4.3.0 (KRaft mode)
- MySQL 8.4 + Flyway
- JUnit 5 + Mockito
- SpringDoc OpenAPI (Swagger)
- Ngrok (exposición pública)

## Docker / Docker Compose

Infraestructura de base de datos y mensajería containerizada para entornos limpios o CI.

### Servicios incluidos
- **MySQL 8.4** (puerto 3307): crea automaticamente las 10 bases de datos del sistema via script de inicializacion
- **Kafka 4.0** (puerto 9094, modo KRaft): broker de mensajeria sin Zookeeper

### Uso
```bash
docker compose up -d
```

Esto crea con un solo comando toda la infraestructura de datos necesaria. Los microservicios Spring Boot corren localmente (`./mvnw spring-boot:run`) y se conectan a estos contenedores ajustando `SPRING_DATASOURCE_URL` al puerto 3307 y `spring.kafka.bootstrap-servers` al puerto 9094 si se usa esta opcion en vez de las instalaciones locales.

```bash
docker compose down
```

## Despliegue completo con Docker Compose

El sistema completo (12 microservicios + MySQL + Kafka) se levanta con un solo comando.

### Requisitos
- Docker Desktop corriendo

### Levantar todo el sistema
```bash
docker compose up -d --build
```

Esto construye las 12 imagenes (multi-stage: Maven build + JRE runtime) y levanta 14 contenedores:
- 10 microservicios de negocio + Eureka + Gateway
- MySQL 8.4 con las 10 bases de datos
- Kafka 4.0 (KRaft)

Todos los microservicios se registran automaticamente en Eureka y son accesibles via el Gateway en `http://localhost:8080`.

### Apagar el sistema
```bash
docker compose down
```

### Notas
- Variables de entorno (`SPRING_DATASOURCE_URL`, `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`, `SPRING_KAFKA_BOOTSTRAP_SERVERS`) se sobreescriben automaticamente para apuntar a los contenedores via la red interna de Docker (`mysql`, `kafka`, `ms-eureka`)
- Cada microservicio tiene su propio `Dockerfile` (multi-stage: `maven:3.9-eclipse-temurin-25` para build, `eclipse-temurin:25-jre` para runtime)
