# Docker

El proyecto esta preparado para levantarse con Docker Compose desde la raiz del repositorio.

```bash
docker compose up --build
```

Ese comando construye las imagenes y levanta:

- `austro-postgres`: base PostgreSQL.
- `austro-risk-service`: servicio mock de riesgos.
- `austro-orchestrator-service`: API que evalua y guarda solicitudes.
- `austro-frontend`: aplicacion web.

## Puertos locales

```text
5173  -> frontend
18080 -> orquestador
8081  -> servicio de riesgos
5432  -> PostgreSQL
```

Dentro del contenedor, el orquestador escucha en `8080`. En la maquina se publica como `18080` para evitar conflictos con otros servicios locales.

## Variables del ambiente

El `docker-compose.yml` configura el orquestador con:

- `QUARKUS_DATASOURCE_JDBC_URL`: conexion interna a PostgreSQL.
- `QUARKUS_DATASOURCE_USERNAME`: usuario de base.
- `QUARKUS_DATASOURCE_PASSWORD`: clave de base.
- `RISK_SERVICE_URL`: URL interna del servicio de riesgos.

El frontend se construye con:

- `VITE_API_URL=http://localhost:18080`

Con esa variable, la pantalla web consume el orquestador publicado en la maquina.

## Comandos utiles

Ver contenedores:

```bash
docker compose ps
```

Ver logs:

```bash
docker compose logs -f orchestrator-service
docker compose logs -f risk-service
```

Detener el ambiente:

```bash
docker compose down
```

Borrar tambien los datos locales de PostgreSQL:

```bash
docker compose down -v
```

Usar `-v` solo cuando no haga falta conservar evaluaciones guardadas.

## Sobre la carpeta tools

La carpeta `tools/` esta ignorada por Git. Puede usarse en una maquina local para guardar Java o Maven portables, pero no se entrega en el repositorio porque contiene binarios grandes.

Para revisar el proyecto no hace falta esa carpeta. Docker construye los servicios usando las imagenes declaradas en cada `Dockerfile`.
