# Overwatch Tracker API

A REST API for logging Overwatch matches and tracking per-hero performance statistics.

Overwatch doesn't expose a public API for personal match history, so this project lets you record your matches manually and get aggregated stats for each hero you play: win rate, average eliminations, damage, healing and more.

This is a personal learning project, built to practice Spring Boot and backend engineering fundamentals.

## Tech Stack

- **Java 17**
- **Spring Boot 4** (Web MVC, Data JPA, Validation)
- **PostgreSQL 17** (database, via Docker Compose)
- **Flyway** (database migrations)
- **JUnit 5 + Mockito** (unit tests)
- **MockMvc** (controller tests)
- **H2** (in-memory database for tests)
- **JaCoCo** (test coverage)
- **GitHub Actions** (CI)
- **Maven**

## Getting Started

### Prerequisites

- JDK 17+
- Docker (with Docker Compose)

### Running the application

Start the database, then the API:

```bash
docker compose up -d
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

On startup, Flyway creates the schema and seeds the database with the full hero roster. Data is stored in a Docker volume, so it survives restarts.

To reset the database to a clean state:

```bash
docker compose down -v
docker compose up -d
```

### Configuration

The database connection works out of the box with the development defaults. Each setting can be overridden with an environment variable:

| Variable | Default |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/overwatch_tracker` |
| `DB_USERNAME` | `overwatch` |
| `DB_PASSWORD` | `overwatch` |

### Accessing the database

```bash
docker compose exec postgres psql -U overwatch -d overwatch_tracker
```

### Running the tests

```bash
./mvnw test
```

Tests don't need Docker: they run against an in-memory H2 database, configured in `src/test/resources/application.properties`.

The coverage report is generated at `target/site/jacoco/index.html`.

## Data Model

```
Match 1 ──── N Performance N ──── 1 Hero
```

- **Hero**: a playable hero, with a name and a role (`TANK`, `DAMAGE`, `SUPPORT`).
- **Match**: a single game, with date, map, map type, mode, result, score and total duration.
- **Performance**: what you did with one hero in one match (eliminations, assists, deaths, damage, healing, mitigation, time played). A match can have several performances, since you can swap heroes mid-game.

## API Endpoints

| Method | Endpoint | Description | Success |
|---|---|---|---|
| `GET` | `/heroes` | List all heroes | `200` |
| `GET` | `/heroes?name={name}` | Search heroes by name (partial, case-insensitive) | `200` |
| `POST` | `/matches` | Create a match, optionally with its performances | `201` |
| `GET` | `/matches` | List all matches | `200` |
| `PUT` | `/matches/{id}` | Update a match | `200` |
| `POST` | `/matches/{id}/performances` | Add a performance to a match | `201` |
| `GET` | `/matches/{id}/performances` | List the performances of a match | `200` |
| `PATCH` | `/matches/{id}/performances/{performanceId}` | Update a performance | `200` |
| `DELETE` | `/matches/{id}/performances/{performanceId}` | Delete a performance | `204` |
| `GET` | `/statistics/heroes/{heroId}` | Aggregated statistics for a hero | `200` |

### Example: creating a match

Creating a match together with its performances is atomic: if any referenced hero doesn't exist, nothing is saved.

```bash
curl -X POST http://localhost:8080/matches \
  -H "Content-Type: application/json" \
  -d '{
    "dateTime": "2026-09-12T20:00:00",
    "matchMap": "Kings Row",
    "mapType": "HYBRID",
    "matchMode": "COMPETITIVE",
    "matchResult": "VICTORY",
    "matchScore": "3 vs 1",
    "totalDuration": "PT12M0S",
    "performanceRequestList": [
      {
        "heroId": 1,
        "heroOrder": 1,
        "timePlayed": "PT12M0S",
        "eliminations": 10,
        "assists": 5,
        "deaths": 2,
        "damageDealt": 5000,
        "healingDone": 0,
        "damageMitigated": 0
      }
    ]
  }'
```

Accepted values:

- `mapType`: `ESCORT`, `HYBRID`, `CONTROL`, `PUSH`, `FLASHPOINT`
- `matchMode`: `QUICK_PLAY`, `COMPETITIVE`
- `matchResult`: `VICTORY`, `DEFEAT`, `DRAW`
- `totalDuration` / `timePlayed`: ISO-8601 durations (`PT9M54S` = 9 min 54 s)

### Validation rules

Request bodies are validated before reaching the business logic:

| Field | Rule |
|---|---|
| `dateTime` | Required, cannot be in the future |
| `matchMap` | Required, cannot be blank |
| `mapType`, `matchMode`, `matchResult` | Required, must be one of the accepted values |
| `matchScore` | Required, format `<number> vs <number>` (e.g. `3 vs 1`) |
| `totalDuration`, `timePlayed` | Required, at least 1 second |
| `performanceRequestList` | Required (can be empty); every item is validated too |
| `heroId`, `heroOrder` | Required, positive |
| `eliminations`, `assists`, `deaths`, `damageDealt`, `healingDone`, `damageMitigated` | Required, zero or positive |

### Error responses

Errors return a consistent JSON body:

```json
{
  "dateTime": "2026-09-29T18:42:10.123",
  "statusCode": 404,
  "error": "Not Found",
  "message": "Match ID 10 not found.",
  "path": "/matches/10"
}
```

| Status | When |
|---|---|
| `404 Not Found` | The hero, match or performance doesn't exist |
| `400 Bad Request` | The request body fails validation (e.g. blank map, negative stats, future date) |
| `400 Bad Request` | The request body is malformed JSON or has an unknown enum value |
| `400 Bad Request` | The performance doesn't belong to the match in the URL |

Validation errors list every invalid field in `message`, separated by `;`:

```json
{
  "dateTime": "2026-11-03T17:09:09.451",
  "statusCode": 400,
  "error": "Bad Request",
  "message": "matchMap: must not be blank; performanceRequestList[0].eliminations: must be greater than or equal to 0",
  "path": "/matches"
}
```

## Testing

The project has **48 automated tests** with **100% instruction and branch coverage**, measured with JaCoCo. The application entry point (`main`) is excluded from the report, since it only starts Spring and holds no logic.

Tests are split by layer, each one mocking the layer below it:

| Layer | Tools | What is verified |
|---|---|---|
| Services | JUnit 5 + Mockito | Business rules, calculations and error cases, with repositories mocked |
| Controllers | `@WebMvcTest` + MockMvc | Routing, HTTP status codes, JSON request/response, request validation and exception-to-HTTP mapping, with services mocked |

For manual testing, a [Bruno](https://www.usebruno.com/) collection with every endpoint is available in `bruno/`. Open it in Bruno and select the `Local` environment.

Every push and pull request runs the full test suite on GitHub Actions, and the coverage report is uploaded as a build artifact.

## Project Structure

The code is organized by domain rather than by layer:

```
src/main/java/com/caio/overwatch_tracker/
├── hero/          # Hero entity, repository, controller
├── match/         # Match entity, DTO, service, controller
├── performance/   # Performance entity, DTO, service, controller
├── statistic/     # Statistics service, response DTO, controller
└── exception/     # Global exception handler and error response

src/main/resources/db/migration/   # Flyway migrations (schema + hero seed)
src/test/java/com/caio/overwatch_tracker/   # Service and controller tests, mirroring the main packages
.github/workflows/ci.yml           # CI pipeline
bruno/                             # Bruno collection for manual API testing
docker-compose.yml                 # Local PostgreSQL
```

## Roadmap

- [x] Migrate from H2 to PostgreSQL
- [x] Input validation with Bean Validation
- [x] Unit tests for the service layer
- [x] Controller tests with MockMvc
- [x] Test coverage with JaCoCo (100% instructions and branches)
- [x] Continuous integration with GitHub Actions
- [ ] Integration tests with a real database (Testcontainers)
- [x] Search heroes by name (`GET /heroes?name=Ana`)