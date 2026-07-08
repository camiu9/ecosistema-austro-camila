# Arquitectura

## Vista general

El sistema tiene tres componentes principales:

- Frontend React para ingresar solicitudes y ver el historial.
- Orquestador Quarkus para validar, decidir y guardar evaluaciones.
- Servicio de riesgos Quarkus para consultar score y deudas.

PostgreSQL guarda las evaluaciones realizadas.

## Flujo de una evaluacion

1. El usuario ingresa cedula, monto, plazo y salario en el frontend.
2. El frontend envia la solicitud al orquestador.
3. El orquestador valida la cedula ecuatoriana y los montos.
4. El orquestador consulta score y deudas en el servicio de riesgos.
5. Se aplica la regla de aprobacion.
6. El resultado se guarda en PostgreSQL.
7. El frontend muestra la decision y actualiza el historial.

## Orquestador

Responsabilidades:

- Exponer `POST /v1/credit-evaluations`.
- Exponer `GET /v1/credit-evaluations`.
- Validar la entrada antes de procesar.
- Consumir el servicio de riesgos por REST.
- Guardar cada evaluacion en base de datos.
- Devolver errores claros para el frontend.

## Servicio de riesgos

Responsabilidades:

- Exponer el score de una cedula.
- Exponer las deudas de una cedula.
- Respetar las latencias pedidas en el enunciado.

Los datos se calculan a partir de la cedula para que una misma cedula entregue siempre el mismo resultado.

## Base de datos

Tabla principal: `credit_evaluations`.

Campos guardados:

- Cedula
- Monto solicitado
- Plazo
- Salario
- Score
- Deuda mensual
- Capacidad de pago
- Estado final
- Fecha de creacion

## Seguridad aplicada

- No se concatena SQL con datos del usuario.
- Se usa Hibernate ORM/Panache.
- CORS limitado al origen del frontend.
- Validaciones en la entrada de la API.
- Respuestas de error sin stack traces.

## Documentacion de endpoints

La documentacion de API se revisa en Swagger:

- Orquestador: http://localhost:18080/swagger-ui
- Riesgos: http://localhost:8081/swagger-ui
