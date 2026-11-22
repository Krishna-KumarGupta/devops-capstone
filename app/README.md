# Task Manager API

Java 25 / Spring Boot REST API. From this directory, run the tests and start the
application with:

```sh
./mvnw test
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080`:

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/tasks` | List tasks |
| `GET` | `/api/tasks/{id}` | Get a task |
| `POST` | `/api/tasks` | Create a task |
| `PUT` | `/api/tasks/{id}` | Replace a task |
| `DELETE` | `/api/tasks/{id}` | Delete a task |
| `POST` | `/api/auth/login` | Validate a login request and return an acknowledgement |

Task create/update bodies accept `title` (required, up to 200 characters),
`description` (up to 2000 characters), and `completed`:

```json
{"title":"Prepare release","description":"Review the checklist","completed":false}
```

The login route is a placeholder: it validates that `username` and `password`
are present, but does not authenticate credentials or issue a token.

H2 in-memory storage is used by default. Override the database connection with
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, and
`SPRING_DATASOURCE_PASSWORD`. The actuator endpoints are available at
`/actuator/health` and `/actuator/prometheus`.

To run the application with PostgreSQL from the repository root:

```sh
docker compose -f docker/docker-compose.yml up --build
```

The compose file accepts `POSTGRES_DB`, `POSTGRES_USER`, and
`POSTGRES_PASSWORD`; its defaults are intended for local development.
