# Ciudad Reporta API

API REST para una red social de reportes ciudadanos. Los usuarios reportan problemas urbanos (baches, iluminación, etc.) con fotos y ubicación, comentan, votan y se suscriben a categorías.

## Tecnologías

- Java 21
- Spring Boot 4.0.6
- PostgreSQL
- Maven
- Docker

## Cómo empezar

```bash
# 1. Clonar y entrar al directorio
git clone <repo>
cd api

# 2. Copiar y configurar variables de entorno
cp .env.example .env
# Editar .env con tus credenciales

# 3. Opción A: Con Docker
docker compose up -d

# 3. Opción B: Sin Docker (necesitas PostgreSQL local)
./mvnw spring-boot:run
```

## Endpoints

### Auth

| Método | Ruta | Body | Descripción |
|--------|------|------|-------------|
| POST | `/auth/register` | `{nombre, email, password, rolId}` | Registrar usuario |
| POST | `/auth/verify` | `{email, codigo}` | Verificar cuenta con código |
| POST | `/auth/resend-code` | `{email}` | Reenviar código de verificación |
| POST | `/auth/login` | `{email, password}` | Iniciar sesión |

### Roles

| Método | Ruta | Body | Descripción |
|--------|------|------|-------------|
| GET | `/roles` | — | Listar roles |
| GET | `/roles/{id}` | — | Obtener rol |
| POST | `/roles` | `{nombre, nivelAcceso}` | Crear rol |
| PUT | `/roles/{id}` | `{nombre, nivelAcceso}` | Actualizar rol |
| DELETE | `/roles/{id}` | — | Eliminar rol |

### Usuarios

| Método | Ruta | Body | Descripción |
|--------|------|------|-------------|
| GET | `/usuario` | — | Listar usuarios |
| GET | `/usuario/{id}` | — | Obtener usuario |
| POST | `/usuario` | `{nombre, email, password, rolId}` | Crear usuario |

## Estado de la Base de Datos

### Tabla: `usuarios`

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | UUID | PK, auto-generado |
| nombre | VARCHAR(100) | NOT NULL |
| email | VARCHAR(100) | NOT NULL, UNIQUE |
| password_hash | VARCHAR(255) | NOT NULL |
| email_verified | BOOLEAN | NOT NULL, default false |
| codigo_verificacion | VARCHAR(255) | nullable |
| foto_url | VARCHAR(255) | nullable |
| activo | BOOLEAN | NOT NULL |
| rol_id | BIGINT | FK → roles.id, NOT NULL |
| created_at | TIMESTAMP | auto |
| updated_at | TIMESTAMP | auto |

### Tabla: `roles`

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGINT | PK, auto-increment |
| nombre | VARCHAR(255) | NOT NULL |
| nivel_acceso | INTEGER | NOT NULL |

### Tabla: `categorias`

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | BIGINT | PK, auto-increment |
| nombre | VARCHAR(100) | NOT NULL, UNIQUE |
| descripcion | VARCHAR(255) | nullable |

## Variables de Entorno

Ver `.env.example` para la lista completa de variables requeridas.

## Docker

```bash
# Iniciar servicios (PostgreSQL + API)
docker compose up -d

# Ver logs
docker compose logs -f

# Detener
docker compose down
```
