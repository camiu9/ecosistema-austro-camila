# API

La API esta documentada con Swagger UI. La forma mas simple de revisarla es levantar el ambiente con Docker Compose.

```bash
docker compose up --build
```

Luego abrir:

- Orquestador: http://localhost:18080/swagger-ui
- Riesgos: http://localhost:8081/swagger-ui

Si los servicios se ejecutan directamente desde Maven, el orquestador queda normalmente en:

- http://localhost:8080/swagger-ui

## Endpoints del orquestador

- `POST /v1/credit-evaluations`: registra una evaluacion de credito.
- `GET /v1/credit-evaluations`: lista las evaluaciones guardadas.

## Endpoints del servicio de riesgos

- `GET /v1/risks/{cedula}/score`: devuelve el score de una cedula.
- `GET /v1/risks/{cedula}/debts`: devuelve deudas asociadas a una cedula.

## Ejemplo de solicitud

```json
{
  "cedula": "1710034065",
  "requestedAmount": 100,
  "termYears": 2,
  "salary": 1500
}
```

## Ejemplo de respuesta aprobada

```json
{
  "cedula": "1710034065",
  "requestedAmount": 100,
  "termYears": 2,
  "salary": 1500,
  "score": 92,
  "monthlyDebt": 120,
  "debtCapacity": 600,
  "status": "APPROVED"
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

Los mensajes de validacion estan pensados para que se puedan mostrar en el frontend sin exponer nombres internos de clases o campos.
