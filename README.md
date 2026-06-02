# Ciudad Reporta API

API REST para red social de reportes ciudadanos. Los usuarios reportan problemas urbanos (baches, alumbrado, basura, etc.) con fotos y ubicación, votan y se suscriben a categorías.

## Tecnologías

- Java 21 + Spring Boot 4.0.6
- PostgreSQL 16 + Hibernate / JPA
- Maven + Docker
- Spring Security (BCrypt) + Spring Mail

## Cómo empezar

```bash
# 1. Clonar Repo y copiar variables de entorno
git clone https://github.com/OsHK00/CityReport.git

# Para desarrollo compilar o ejecutar
./mvnw.cmd compile
./mvnw.cmd spring-boot:run
```

> **Nota:** Para pruebas, desactivar verificación por email en `application.yml`:
> `app.verification.enabled: false`

---

## Endpoints

### Auth — `/auth`

| Método | Ruta | Body | Descripción |
|--------|------|------|-------------|
| POST | `/auth/register` | `RegistroRequest` | Registrar usuario |
| POST | `/auth/verify` | `VerificacionRequest` | Verificar email |
| POST | `/auth/resend-code` | `ResendCodeRequest` | Reenviar código |
| POST | `/auth/login` | `AuthRequest` | Iniciar sesión |

```json
// POST /auth/register
// Request — RegistroRequest
{ "nombre": "Juan", "email": "juan@email.com", "password": "123456", "rolId": 1 }

// Response — AuthResponse
{ "id": "uuid", "nombre": "Juan", "email": "juan@email.com", "emailVerified": true, "mensaje": "Registro exitoso" }
```

```json
// POST /auth/login
// Request — AuthRequest
{ "email": "juan@email.com", "password": "123456" }

// Response — AuthResponse
{ "id": "uuid", "nombre": "Juan", "email": "juan@email.com", "emailVerified": true, "mensaje": "Login exitoso" }
```

### Usuarios — `/usuario`

| Método | Ruta | Body | Descripción |
|--------|------|------|-------------|
| GET | `/usuario` | — | Listar todos |
| GET | `/usuario/{id}` | — | Obtener por UUID |
| POST | `/usuario` | `RegistroRequest` | Crear usuario |

```json
// GET /usuario
// Response — List<UsuarioResponse>
[
  {
    "id": "uuid",
    "nombre": "Juan",
    "email": "juan@email.com",
    "activo": true,
    "rol": "CIUDADANO",
    "fotoUrl": null,
    "createdAt": "2026-06-01T..."
  }
]
```

### Roles — `/roles`

| Método | Ruta | Body | Descripción |
|--------|------|------|-------------|
| GET | `/roles` | — | Listar roles |
| GET | `/roles/{id}` | — | Obtener rol |
| POST | `/roles` | `RolRequest` | Crear rol |
| PUT | `/roles/{id}` | `RolRequest` | Actualizar rol |
| DELETE | `/roles/{id}` | — | Eliminar rol |

```json
// POST /roles
// Request — RolRequest
{ "nombre": "CIUDADANO", "nivelAcceso": 1 }

// Response — RolResponse
{ "id": 1, "nombre": "CIUDADANO", "nivelAcceso": 1 }
```

### Categorías — `/categorias`

| Método | Ruta | Body | Descripción |
|--------|------|------|-------------|
| GET | `/categorias` | — | Listar todas |
| GET | `/categorias/{id}` | — | Obtener por ID |
| POST | `/categorias` | `CategoriaRequest` | Crear categoría |
| PUT | `/categorias/{id}` | `CategoriaRequest` | Actualizar |
| DELETE | `/categorias/{id}` | — | Eliminar |

```json
// POST /categorias
// Request — CategoriaRequest
{ "nombre": "Baches", "descripcion": "Huecos en la via publica" }

// Response — CategoriaResponse
{ "id": 1, "nombre": "Baches", "descripcion": "Huecos en la via publica" }
```

### Reportes — `/reportes`

| Método | Ruta | Body | Descripción |
|--------|------|------|-------------|
| GET | `/reportes` | — | Feed paginado (cursor) |
| GET | `/reportes/{id}` | — | Obtener reporte |
| POST | `/reportes` | `ReporteRequest` | Crear reporte |
| PUT | `/reportes/{id}` | `ReporteRequest` | Actualizar reporte |
| DELETE | `/reportes/{id}` | — | Eliminar reporte |
| POST | `/reportes/{id}/votos` | `VotoRequest` | Votar (UPVOTE/DOWNVOTE) |

```json
// POST /reportes
// Request — ReporteRequest
{
  "titulo": "Bache en Av. Principal",
  "descripcion": "Bache de 2 metros...",
  "latitud": 19.4326,
  "longitud": -99.1332,
  "categoriaId": 1,
  "usuarioId": "uuid-del-usuario"
}

// Response — ReporteResponse
{
  "id": "uuid",
  "titulo": "Bache en Av. Principal",
  "descripcion": "Bache de 2 metros...",
  "latitud": 19.4326,
  "longitud": -99.1332,
  "categoria": { "id": 1, "nombre": "Baches" },
  "usuario": { "id": "uuid", "nombre": "Juan" },
  "imagenes": [],
  "upvotes": 0,
  "downvotes": 0,
  "createdAt": "2026-06-01T..."
}
```

```json
// GET /reportes?cursor=&size=10
// Response — FeedResponse
{
  "data": [ ... ReporteResponse[] ... ],
  "nextCursor": "uuid-del-ultimo-reporte",
  "hasMore": true
}
```

```json
// POST /reportes/{id}/votos
// Request — VotoRequest
{ "tipo": "UPVOTE", "usuarioId": "uuid-del-usuario" }

// Response — VotoResponse
{ "reporteId": "uuid", "upvotes": 1, "downvotes": 0, "mensaje": "Voto registrado" }
```

> El feed usa paginación **cursor-based**. El primer llamado es `GET /reportes?cursor=&size=10`. Para la siguiente página se usa `GET /reportes?cursor={nextCursor}&size=10`.

---

## Variables de Entorno

Ver `.env.example` para la lista completa de variables requeridas.

## Docker

```bash
# Iniciar PostgreSQL
docker compose up -d db

# Iniciar todo (PostgreSQL + API)
docker compose up -d

# Ver logs
docker compose logs -f

# Detener
docker compose down
```
