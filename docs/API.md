# API

La documentacion de endpoints esta en Swagger UI.

## Swagger

Con Docker Compose levantado:

- Orquestador: http://localhost:18080/swagger-ui
- Riesgos: http://localhost:8081/swagger-ui

En modo desarrollo:

- Orquestador: http://localhost:8080/swagger-ui
- Riesgos: http://localhost:8081/swagger-ui

## Endpoints principales

Orquestador:

- `POST /v1/credit-evaluations`: crea una evaluacion de credito.
- `GET /v1/credit-evaluations`: lista las evaluaciones guardadas.

Riesgos:

- `GET /v1/risks/{cedula}/score`: consulta el score de una cedula.
- `GET /v1/risks/{cedula}/debts`: consulta las deudas de una cedula.

## Ejemplo de solicitud

```json
{
  "cedula": "1710034065",
  "requestedAmount": 100,
  "termYears": 2,
  "salary": 1500
}
```

## Ejemplo de error de validacion

```json
{
  "code": "VALIDATION_ERROR",
  "message": "La solicitud contiene datos invalidos",
  "details": [
    "Cedula: La cedula ecuatoriana no es valida"
  ],
  "timestamp": "2026-07-08T04:22:23Z"
}
```
