# Ecosistema Austro - Evaluacion de Creditos

Proyecto de evaluacion de credito compuesto por una interfaz web, un servicio orquestador, un servicio de riesgos y una base PostgreSQL.

## Componentes

- `frontend/`: pantalla para registrar solicitudes y revisar el historial.
- `services/orchestrator-service/`: recibe la solicitud, valida los datos, consulta riesgos, decide y guarda el resultado.
- `services/risk-service/`: entrega score y deudas por cedula.
- `postman/`: coleccion para probar los endpoints.
- `docs/`: notas de arquitectura, Docker, pruebas y acceso a Swagger.

## Comunicacion entre servicios

El proyecto usa REST. Para este flujo es suficiente, facil de probar con Postman y claro para revisar desde Swagger.

No se uso gRPC porque no hay streaming, contratos binarios ni una necesidad fuerte de comunicacion interna de alto rendimiento.

## Regla de aprobacion

Una solicitud queda `APPROVED` cuando se cumple todo esto:

```text
score > 70
(deudaMensual + montoSolicitado) < (salario * 0.40)
```

Si una de esas condiciones no se cumple, la solicitud queda `REJECTED`.

## Herramientas

El repositorio incluye Java y Maven portables dentro de `tools/`. Antes de correr comandos Maven en PowerShell:

```powershell
. .\scripts\use-local-tools.ps1
```

Tambien se requiere Docker Desktop para levantar todo el ambiente con PostgreSQL.

## Levantar el proyecto con Docker

```bash
docker compose up --build
```

Ese comando construye las imagenes y deja arriba la base de datos, los dos servicios Quarkus y el frontend.

URLs:

- Frontend: http://localhost:5173
- Orquestador: http://localhost:18080
- Swagger del orquestador: http://localhost:18080/swagger-ui
- Riesgos: http://localhost:8081
- Swagger de riesgos: http://localhost:8081/swagger-ui
- PostgreSQL: localhost:5432

El orquestador se publica en `18080` para evitar choques con otros procesos que suelen usar `8080`.

Mas detalle sobre contenedores y puertos: `docs/DOCKER.md`.

## Pruebas

Servicio de riesgos:

```bash
cd services/risk-service
mvn test
```

Orquestador:

```bash
cd services/orchestrator-service
mvn test
```

Frontend:

```bash
cd frontend
npm install
npm run build
```

## Seguridad

- No hay SQL armado con textos recibidos del usuario.
- La persistencia se hace con Hibernate ORM/Panache y entidades JPA.
- La cedula ecuatoriana se valida con modulo 10.
- Los valores numericos deben ser positivos.
- El CORS del orquestador acepta solo `http://localhost:5173`.
- Los errores de la API salen con un formato controlado.

## Postman

Importar:

```text
postman/ecosistema-austro.postman_collection.json
```

La coleccion ya trae las variables locales para probar el orquestador y el servicio de riesgos.
