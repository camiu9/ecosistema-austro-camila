# Pruebas

Las pruebas unitarias se ejecutan con Maven. Para correrlas localmente se necesita Java 17 y Maven instalados en la maquina.

Docker no es obligatorio para las pruebas unitarias, pero si es la forma recomendada para probar el flujo completo con frontend, APIs y base de datos.

## Servicio de riesgos

```bash
cd services/risk-service
mvn test
```

Estas pruebas revisan:

- Que el score este dentro del rango de 0 a 100.
- Que una misma cedula entregue el mismo resultado.
- Que las deudas tengan mensualidades validas.
- Que los endpoints de score y deudas respondan correctamente.

## Orquestador

```bash
cd services/orchestrator-service
mvn test
```

Estas pruebas revisan:

- Cedulas ecuatorianas validas e invalidas.
- Regla de aprobacion del credito.
- Calculo de deuda mensual total.

## Frontend

```bash
cd frontend
npm install
npm run build
```

Ese build confirma que la aplicacion React compile correctamente.

## Prueba manual recomendada

1. Levantar el ambiente:

```bash
docker compose up --build
```

2. Abrir el frontend:

```text
http://localhost:5173
```

3. Crear una solicitud con cedula valida.
4. Revisar que aparezca `Aprobado` o `Rechazado`.
5. Confirmar que la evaluacion se agregue al historial.
6. Probar una cedula invalida y revisar que el mensaje sea claro.

## Postman

Archivo:

```text
postman/ecosistema-austro.postman_collection.json
```

Variables incluidas:

- `orchestratorUrl`: `http://localhost:18080`
- `riskUrl`: `http://localhost:8081`
- `cedulaValida`: `1710034065`

La coleccion sirve para probar el flujo principal y los endpoints del servicio de riesgos sin usar el frontend.
