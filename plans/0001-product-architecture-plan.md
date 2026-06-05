# Product Architecture Plan

**Project:** imjangbox
**Created:** 2026-06-04
**Status:** Planning artifact; no product code has been implemented.

## Objective

Build a privacy-first Spring Boot broker ledger for commercial-property inspections and customer-safe proposal share cards.

## Stack Decisions

- Java 21 and Spring Boot 3.x for the backend application.
- Spring MVC, Thymeleaf, and Bootstrap 5.3 for server-rendered UI.
- MyBatis and MySQL for persistence.
- JUnit 5 for tests.
- Gradle for build automation.
- Kakao Maps for map UI.
- `GeocodingGateway` for geocoding abstraction.
- `FileStorage` for file persistence abstraction.

## Module Boundaries

| Module | Responsibility |
| --- | --- |
| `property` | Property identity, internal/public address structures, core facts. |
| `inspection` | Broker field inspection workflow, notes, pricing context, contact logs. |
| `facility` | Business-type templates and facility-check answers. |
| `share` | Public snapshot creation, public DTOs, share-card rendering. |
| `map` | Kakao Maps integration, coordinates, geocoding gateway use. |
| `file` | Attachment metadata and `FileStorage` boundary. |
| `common` | Shared validation, errors, IDs, and test helpers. |

## Privacy Architecture

Public surfaces must use share snapshots, not internal records.

Denied public fields:

- `private_memo`
- `price_private_note`
- `stakeholder.phone`
- `contact_log.content`
- `internal_risk_memo`

Implementation rule: public controllers and templates should accept only public share DTOs or snapshot view models.

## Persistence Direction

- Use MyBatis mapper interfaces and XML or annotation mappings selected during Phase 0/1.
- Keep public snapshot persistence separate from internal inspection persistence.
- Name internal/public address columns distinctly.
- Add a `search_index` structure for map and keyword search.

## UI Direction

- Internal broker screens use Thymeleaf forms and Bootstrap 5.3 components.
- Public share cards use customer-safe snapshot view models only.
- UI text should avoid implying legal judgment, district scoring, or automated recommendation authority.

## Integration Direction

- `GeocodingGateway` returns explicit success/failure results.
- Kakao Maps JavaScript usage should stay in map-focused templates/static assets.
- `FileStorage` should hide storage paths and enforce access-control decisions through services.

## Test Plan

- Context-load test in Phase 0.
- Domain tests for verification status coverage.
- Mapper tests for internal persistence and snapshot persistence.
- Projection tests proving denied fields are absent.
- MVC/template tests for broker forms and public share cards.
- Gateway tests for geocoding success and failure.
- File-storage tests for metadata and public access safety.

## Execution Order

1. Build skeleton and test harness.
2. Define privacy boundary and verification enum.
3. Implement internal inspection ledger.
4. Add facility templates and search index.
5. Add share snapshot generation and public cards.
6. Harden auth, audit, storage, and release operations.

For task-level detail, use `plans/2026-06-04-imjangbox-implementation-plan.md` and `TASKS.md`.
