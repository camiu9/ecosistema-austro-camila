# Pruebas

## Backend

Servicio de riesgos:

- Valida que el score este entre 0 y 100.
- Valida que una misma cedula entregue el mismo score.
- Valida que las deudas tengan mensualidades no negativas.
- Prueba los endpoints de score y deudas.

```bash
cd services/risk-service
mvn test
```

Orquestador:

- Valida cedulas ecuatorianas correctas e incorrectas.
- Prueba la regla de aprobacion.
- Prueba el calculo de deuda mensual total.

```bash
cd services/orchestrator-service
mvn test
```

## Frontend

```bash
cd frontend
npm install
npm run build
```

## Pruebas manuales

- Crear una evaluacion con cedula valida.
- Crear una evaluacion con cedula invalida y revisar el mensaje.
- Revisar que el historial se actualice despues de crear una evaluacion.
- Apagar el orquestador y confirmar que el frontend muestre error.

## Postman

Archivo:

```text
postman/ecosistema-austro.postman_collection.json
```

Variables incluidas:

- `orchestratorUrl`: `http://localhost:18080`
- `riskUrl`: `http://localhost:8081`
- `cedulaValida`: `1710034065`
