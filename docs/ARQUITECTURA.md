# Arquitectura

El sistema esta dividido en frontend, orquestador, servicio de riesgos y base de datos.

## Componentes

- Frontend React: recibe los datos de la solicitud y muestra el resultado.
- Orquestador Quarkus: valida, consulta riesgos, decide y guarda la evaluacion.
- Servicio de riesgos Quarkus: entrega score y deudas de una cedula.
- PostgreSQL: guarda las evaluaciones realizadas.

## Flujo de evaluacion

1. El usuario ingresa cedula, monto, plazo y salario.
2. El frontend envia la solicitud al orquestador.
3. El orquestador valida la cedula ecuatoriana y los valores numericos.
4. El orquestador consulta score y deudas en el servicio de riesgos.
5. Se aplica la regla de negocio.
6. El resultado se guarda en PostgreSQL.
7. El frontend muestra la decision y actualiza el historial.

## Orquestador

El orquestador concentra el flujo principal del credito.

Responsabilidades:

- Exponer `POST /v1/credit-evaluations`.
- Exponer `GET /v1/credit-evaluations`.
- Validar la entrada antes de procesar.
- Consumir el servicio de riesgos por REST.
- Aplicar la regla de aprobacion.
- Guardar cada evaluacion en base de datos.
- Responder errores claros al frontend.

## Servicio de riesgos

El servicio de riesgos es un mock con latencia simulada, como pide el ejercicio.

Responsabilidades:

- Devolver score para una cedula.
- Devolver deudas para una cedula.
- Mantener resultados estables para que una misma cedula no cambie en cada prueba.

## Persistencia

La tabla principal es `credit_evaluations`.

Guarda:

- Cedula.
- Monto solicitado.
- Plazo.
- Salario.
- Score.
- Deuda mensual.
- Capacidad de pago.
- Estado final.
- Fecha de creacion.

## Seguridad

- No se concatena SQL con datos recibidos del usuario.
- Se usa Hibernate ORM/Panache.
- La cedula se valida antes de consultar riesgos.
- Los valores numericos deben ser positivos.
- CORS queda limitado al origen del frontend.
- Las respuestas de error no exponen stack traces.

## REST o gRPC

Se eligio REST entre servicios.

Para este caso es una buena decision porque el flujo es simple, los contratos son faciles de leer desde Swagger y Postman, y no hay necesidad de streaming ni comunicacion binaria de alto rendimiento.

gRPC seria una alternativa razonable si el servicio de riesgos tuviera muchas llamadas internas, contratos compartidos entre equipos o necesidades fuertes de rendimiento. Para este ejercicio, REST cubre bien el alcance sin agregar complejidad.

## Documentacion de endpoints

Con Docker Compose levantado:

- Orquestador: http://localhost:18080/swagger-ui
- Riesgos: http://localhost:8081/swagger-ui
