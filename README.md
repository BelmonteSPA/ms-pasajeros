# Belmonte SpA - Microservicio de Pasajeros (ms-pasajeros)

Gestiona de forma aislada a los pasajeros / solicitantes de traslados corporativos. Es un servicio independiente
(no depende de otros microservicios); lo consumirá `ms-viajes` para validar al pasajero antes de crear una solicitud.

## Tecnologías

- Java 25 / Spring Boot 4.0.6
- MySQL (Laragon) + Spring Data JPA, migraciones con Flyway
- Spring Cloud (cliente Eureka, desactivado por ahora)
- Datafaker para datos sintéticos (el anexo prohíbe datos reales)

## Puerto

`http://localhost:8082`

## Endpoints (`/api/v1/pasajeros`)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Lista todos los pasajeros |
| GET | `/{id}` | Obtiene un pasajero (404 si no existe) |
| POST | `/` | Crea un pasajero (409 si el email ya existe) |
| PUT | `/{id}` | Actualiza datos (preserva `activo`) |
| DELETE | `/{id}` | Elimina un pasajero |
| PATCH | `/{id}/estado` | Alterna el estado: activo (1) pasa a inactivo (0) y viceversa |
| GET | `/{id}/validar` | `true` si existe y está activo (lo usará ms-viajes vía Feign) |
| GET | `/activos` | Lista pasajeros activos |
| GET | `/empresa/{empresa}` | Búsqueda parcial por empresa |

Ejemplo de `POST`:

```json
{
  "nombre": "Ana Prueba",
  "email": "ana@ejemplo.test",
  "telefono": "+56911112222",
  "empresa": "Empresa Test"
}
```

## Base de datos (MySQL con Laragon)

1. Iniciar MySQL desde Laragon (puerto 3306).
2. La base `db_pasajeros` se crea sola al arrancar (`createDatabaseIfNotExist=true`); también puedes crearla a mano: `CREATE DATABASE db_pasajeros;`
3. Las tablas las crea Flyway al arrancar (`src/main/resources/db/migration`).
4. Credenciales por variables de entorno (por defecto, las de Laragon):

| Variable | Por defecto |
|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/db_pasajeros?createDatabaseIfNotExist=true&serverTimezone=America/Santiago` |
| `DB_USER` | `root` |
| `DB_PASSWORD` | (vacía) |

## Ejecución local

Con perfil `dev` se cargan 100 pasajeros sintéticos si la tabla está vacía:

```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

## Nota

Si falta el wrapper de Maven (`mvnw`, carpeta `.mvn`), generarlo con `mvn wrapper:wrapper` o crearlo desde el IDE.
