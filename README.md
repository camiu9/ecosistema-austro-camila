# Ecosistema Austro - Evaluacion de Creditos

Proyecto para evaluar solicitudes de credito. Tiene una pantalla web, dos servicios en Quarkus y una base PostgreSQL.

La forma recomendada de revisarlo es con Docker Compose, porque levanta todo el ambiente con un solo comando y no exige instalar Java o Maven para ejecutar la aplicacion.

## Que incluye

- `frontend/`: formulario de solicitud, resultado de la evaluacion e historial.
- `services/orchestrator-service/`: API principal. Valida la solicitud, consulta riesgos, aplica la regla de negocio y guarda la evaluacion.
- `services/risk-service/`: servicio mock de riesgos. Entrega score y deudas para una cedula.
- `postman/`: coleccion lista para probar los endpoints.
- `docs/`: notas de arquitectura, Docker, pruebas y API.
- `docker-compose.yml`: ambiente local completo.

## Requisitos

Para levantar todo:

- Docker Desktop.

Para correr pruebas sin Docker:

- Java 17.
- Maven 3.9 o superior.
- Node.js 20 o superior, si se quiere compilar el frontend localmente.

La carpeta `tools/` no se sube al repositorio. Puede existir en una maquina local para tener Java o Maven portables, pero no forma parte de la entrega porque son binarios pesados y GitHub no los acepta.

## Levantar el proyecto

Desde la raiz del repositorio:

```bash
docker compose up --build
```

Al terminar, quedan disponibles estas URLs:

- Frontend: http://localhost:5173
- Orquestador: http://localhost:18080
- Swagger del orquestador: http://localhost:18080/swagger-ui
- Servicio de riesgos: http://localhost:8081
- Swagger de riesgos: http://localhost:8081/swagger-ui
- PostgreSQL: `localhost:5432`

El orquestador se publica en `18080` para evitar conflictos con procesos que ya usen `8080`.

Para detener el ambiente:

```bash
docker compose down
```

## Comunicacion

El proyecto usa REST.

Para este ejercicio REST es suficiente: es claro para el frontend, se prueba bien con Postman y queda documentado de forma directa en Swagger. No se uso gRPC porque el flujo no necesita streaming, contratos binarios ni comunicacion interna de alta frecuencia.

## Regla de aprobacion

Una solicitud queda `APPROVED` cuando se cumplen estas dos condiciones:

```text
score > 70
(deudaMensual + montoSolicitado) < (salario * 0.40)
```

Si una condicion falla, la solicitud queda `REJECTED`.

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

## Seguridad aplicada

- No se arma SQL concatenando datos del usuario.
- La persistencia usa Hibernate ORM/Panache y entidades JPA.
- La cedula ecuatoriana se valida con el algoritmo de modulo 10.
- Los montos y plazos se validan antes de procesar la solicitud.
- El CORS del orquestador acepta solo el origen del frontend local.
- Los errores salen con una estructura controlada, sin mostrar stack traces.

## Documentacion

- API y Swagger: `docs/API.md`
- Arquitectura: `docs/ARQUITECTURA.md`
- Docker: `docs/DOCKER.md`
- Pruebas: `docs/PRUEBAS.md`
- Coleccion Postman: `postman/ecosistema-austro.postman_collection.json`

La documentacion viva de los endpoints esta en Swagger cuando el ambiente esta levantado.

## Postman

Importar esta coleccion:

```text
postman/ecosistema-austro.postman_collection.json
```

La coleccion trae variables locales para probar el orquestador y el servicio de riesgos.
