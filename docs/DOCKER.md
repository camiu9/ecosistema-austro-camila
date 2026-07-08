# Docker

El ambiente se levanta con Docker Compose desde la raiz del proyecto.

```bash
docker compose up --build
```

Ese comando crea o actualiza estos componentes:

- `austro-postgres`: base PostgreSQL con la base `austro_credit`.
- `austro-risk-service`: servicio de riesgos en el puerto `8081`.
- `austro-orchestrator-service`: API principal en el puerto local `18080`.
- `austro-frontend`: pantalla web en el puerto `5173`.

## Puertos

```text
5173  -> frontend
18080 -> orquestador
8081  -> servicio de riesgos
5432  -> PostgreSQL
```

El orquestador escucha en `8080` dentro del contenedor, pero en la maquina queda publicado como `18080`. Se dejo asi para evitar conflictos con otros programas que ya pueden estar usando `8080`.

## Variables usadas

El `docker-compose.yml` pasa estas variables al orquestador:

- `QUARKUS_DATASOURCE_JDBC_URL`: ruta interna hacia PostgreSQL.
- `QUARKUS_DATASOURCE_USERNAME`: usuario de base.
- `QUARKUS_DATASOURCE_PASSWORD`: clave de base.
- `RISK_SERVICE_URL`: direccion interna del servicio de riesgos.

El frontend recibe `VITE_API_URL=http://localhost:18080` al momento de construir la imagen. Con eso la pantalla apunta al orquestador publicado en la maquina.

## Comandos utiles

```bash
docker compose ps
docker compose logs -f orchestrator-service
docker compose logs -f risk-service
docker compose down
```

Si se quiere borrar tambien la data local de PostgreSQL:

```bash
docker compose down -v
```

Usar `-v` solo cuando no haga falta conservar las evaluaciones guardadas.
