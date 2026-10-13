# Overwatch Tracker API

A REST API for logging Overwatch matches and tracking per-hero performance statistics.

Overwatch doesn't expose a public API for personal match history, so this project lets you record your matches manually and get aggregated stats for each hero you play: win rate, average eliminations, damage, healing and more.

This is a personal learning project, built to practice Spring Boot and backend engineering fundamentals.

## Tech Stack

- **Java 21**
- **Spring Boot 4** (Web MVC, Data JPA)
- **H2** (in-memory database)
- **Flyway** (database migrations)
- **JUnit 5 + Mockito** (unit tests)
- **Maven**

## Getting Started

### Prerequisites

- JDK 21+

### Running the application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

On startup, Flyway creates the schema and seeds the database with the full hero roster.

> **Note:** the database is in-memory, so all recorded matches are lost when the application stops.

### H2 Console

To inspect the database while the app is running, open `http://localhost:8080/h2-console` and connect with:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:testdb` |
| User | `sa` |
| Password | *(empty)* |

### Running the tests

```bash
./mvnw test
```

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
| `400 Bad Request` | The performance doesn't belong to the match in the URL |

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
```

## Roadmap

- [ ] Migrate from H2 to PostgreSQL
- [ ] Input validation with Bean Validation
- [ ] Controller tests with MockMvc
- [ ] Full unit test coverage of the service layer, measured with JaCoCo
- [ ] Search heroes by name (`GET /heroes?name=Ana`)
