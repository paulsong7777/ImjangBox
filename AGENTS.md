# PROJECT KNOWLEDGE BASE

**Project:** imjangbox
**Generated:** 2026-06-04
**Branch:** main
**Commit:** none yet

## OVERVIEW

imjangbox is a commercial-property field inspection and proposal ledger for Korean commercial real estate brokers. It should help brokers capture property information on site, organize fit and risk details, and generate safe customer proposal cards without leaking internal records.

## CURRENT STATE

Phase 0 foundation exists: Java 21 Spring Boot 3.5.x Gradle project, wrapper, minimal MVC route, Thymeleaf template, package boundary markers, local/test profiles, first context and MVC smoke tests, and README commands.

## INTENDED STACK

- Java 21
- Spring Boot 3.x
- Thymeleaf
- Bootstrap 5.3
- MyBatis
- MySQL
- JUnit 5
- Gradle
- Kakao Maps
- `GeocodingGateway`
- `FileStorage`

## STRUCTURE

```text
/
|-- AGENTS.md
|-- TASKS.md
|-- WORK_LOG.md
|-- CHECKPOINT.md
|-- docs/
|   |-- architecture-constraints.md
|   |-- DOMAIN_RULES.md
|   |-- domain-overview.md
|   |-- integrations.md
|   |-- INIT_NOTEPAD.md
|   |-- privacy-and-share-cards.md
|   |-- PROJECT_MEMORY.md
|   |-- search-index.md
|   `-- VALIDATION.md
`-- plans/
    |-- 0001-product-architecture-plan.md
    `-- 2026-06-04-imjangbox-implementation-plan.md
```

## WHERE TO LOOK

| Task | Location | Notes |
| --- | --- | --- |
| Resume work | `CHECKPOINT.md` | Read first in a new session. |
| Phase backlog | `TASKS.md` | Phase 0 through Phase 5 task ledger. |
| Build file | `build.gradle` | Spring Boot 3.5.x, Java 21, MVC, Thymeleaf, validation, MyBatis, MySQL, tests. |
| Application entry point | `src/main/java/com/imjangbox/ImjangboxApplication.java` | Minimal Spring Boot app. |
| MVC smoke route | `src/main/java/com/imjangbox/web/HomeController.java` | Root route for Phase 0 QA only. |
| Package boundaries | `src/main/java/com/imjangbox/` | `property`, `inspection`, `share`, `facility`, `map`, `file`, `common`, and `web`. |
| Implementation plan | `plans/2026-06-04-imjangbox-implementation-plan.md` | Concrete product build sequence. |
| Architecture plan | `plans/0001-product-architecture-plan.md` | Module boundaries and stack decisions. |
| Domain constraints | `docs/DOMAIN_RULES.md` | Privacy, verification, address, facility, and map-search rules. |
| Privacy design | `docs/privacy-and-share-cards.md` | Public share-card deny rules and snapshot requirements. |
| Architecture constraints | `docs/architecture-constraints.md` | Required stack, forbidden stack, and module boundaries. |
| Integrations | `docs/integrations.md` | Kakao Maps, `GeocodingGateway`, and `FileStorage` boundaries. |
| Search indexing | `docs/search-index.md` | Separate map/search index expectations. |
| Project memory | `docs/PROJECT_MEMORY.md` | Purpose, stack, exclusions, and initial assumptions. |
| Initialization evidence | `docs/INIT_NOTEPAD.md` | Repository survey and validation receipts. |

## HARD PRODUCT RULES

- Customer share cards must never expose internal information.
- `private_memo`, `price_private_note`, `stakeholder.phone`, `contact_log.content`, and `internal_risk_memo` must never appear in public share responses.
- Share cards must use snapshot structures, not direct internal entity exposure.
- Internal address and public address must be separated.
- Verification status must include `UNVERIFIED`, `OWNER_CLAIM`, `TENANT_CLAIM`, `CO_BROKER_CLAIM`, `AGENT_CHECKED`, and `DOCUMENT_CHECKED`.
- Facility checks must support dynamic template-based items by business type.
- Map search should consider a separate `search_index` structure.

## EXPLICIT NON-GOALS

- Do not use React, Vue, or Next.js.
- Do not use JPA.
- Do not build automatic ad-copy generation.
- Do not build automatic business-type legal judgment.
- Do not build automatic commercial district scoring.
- Do not build external listing platform auto-upload.

## ENGINEERING CONVENTIONS

- Use Spring Boot MVC with Thymeleaf server-rendered pages.
- Use Bootstrap 5.3 for UI styling and layout.
- Use MyBatis SQL mappings rather than ORM entity exposure.
- Keep public share DTOs/snapshots structurally separate from internal models.
- Treat privacy-sensitive fields as denied by default in public response design.
- Prefer explicit gateway interfaces for Kakao Maps/geocoding and file storage boundaries.
- Keep verification status as a closed enum with tests for every public projection.

## COMMANDS

```bash
./gradlew test
./gradlew bootRun
```

When this repository is under a WSL Windows mount like `/mnt/c`, Gradle build output defaults to `/tmp/imjangbox-build` to avoid mount-level chmod failures. Override with `IMJANGBOX_BUILD_DIR` if needed.

## NOTES

- This file governs the entire repository until deeper `AGENTS.md` files are intentionally added.
- Do not modify product code during planning-only tasks.
- Phase 1 must not expose internal records directly through public share DTOs, templates, or responses.
