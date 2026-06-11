# imjangbox Implementation Plan

**Created:** 2026-06-04
**Scope:** Concrete implementation sequence for the initial product build.
**Current repository state:** Phase 2 broker inspection ledger create/edit flow exists.

## Product Goal

Build a Spring Boot 3.x application that helps Korean commercial real estate brokers record commercial-property inspections, maintain internal verification and risk notes, and generate customer-safe proposal cards from public snapshots.

## Non-Negotiable Constraints

- Use Java 21, Spring Boot 3.x, Thymeleaf, Bootstrap 5.3, MyBatis, MySQL, JUnit 5, Gradle, Kakao Maps, `GeocodingGateway`, and `FileStorage`.
- Do not use React, Vue, Next.js, or JPA.
- Do not build automatic ad-copy generation.
- Do not build automatic business-type legal judgment.
- Do not build automatic commercial district scoring.
- Do not build external listing platform auto-upload.
- Public share responses must never expose `private_memo`, `price_private_note`, `stakeholder.phone`, `contact_log.content`, or `internal_risk_memo`.

## Architecture Direction

- Server-rendered Spring MVC pages with Thymeleaf and Bootstrap 5.3.
- MyBatis mapper layer for persistence.
- Internal domain records separate from public share snapshots.
- Gateway boundary for geocoding and Kakao Maps integration.
- Gateway boundary for file storage.
- Separate `search_index` data structure for map and search workflows.

## Phase 0 - Foundation

1. Create a Java 21 Gradle Spring Boot 3.x skeleton.
2. Add dependencies for MVC, Thymeleaf, validation, MyBatis, MySQL, and JUnit 5.
3. Add Gradle wrapper if missing.
4. Add package structure:
   - `property`
   - `inspection`
   - `share`
   - `facility`
   - `map`
   - `file`
   - `common`
5. Add first application context test.
6. Add local configuration placeholders with secrets excluded.

**Acceptance criteria:**

- `./gradlew test` passes.
- A minimal Spring Boot application starts.
- No product domain behavior is added before tests and boundaries are named.

## Phase 1 - Privacy-First Domain And Persistence

1. Model internal property inspection records.
2. Model public share snapshots separately.
3. Define verification status enum with:
   - `UNVERIFIED`
   - `OWNER_CLAIM`
   - `TENANT_CLAIM`
   - `CO_BROKER_CLAIM`
   - `AGENT_CHECKED`
   - `DOCUMENT_CHECKED`
4. Separate internal address and public address in schema and models.
5. Draft MySQL tables and MyBatis mappers.
6. Add public projection tests proving denied fields cannot appear.

**Acceptance criteria:**

- Internal records can contain private fields.
- Public snapshots cannot contain denied fields by type shape, mapper output, or template rendering.
- Verification status tests cover every enum value.

## Phase 2 - Broker Inspection Ledger

1. Build broker-facing inspection create/edit screens.
2. Capture business-fit notes, facility conditions, premium/key-money, rent, area, and verification status.
3. Store contact logs as internal-only data.
4. Store internal risk memo as internal-only data.
5. Add file attachments through `FileStorage`.
6. Add validation and controller/service/mapper tests.

**Acceptance criteria:**

- A broker can create and update an inspection.
- Private notes are visible only on internal screens.
- File attachment metadata is persisted without exposing private storage paths publicly.

## Phase 3 - Facility Templates, Map Search, And Geocoding

1. Define facility template records by business type.
2. Render dynamic facility checklist items from templates.
3. Persist facility answers separately from template definitions.
4. Implement `GeocodingGateway`.
5. Integrate Kakao Maps without embedding business logic in templates.
6. Build `search_index` update flow for map and keyword search.

**Acceptance criteria:**

- Facility checklist content can change by business type without code changes.
- Geocoding failures produce clear internal status without corrupting inspection records.
- Search uses `search_index`, not ad hoc direct scans of internal notes.

## Phase 4 - Customer Share Cards

1. Generate immutable or versioned share snapshots from internal records.
2. Render customer share cards from snapshots.
3. Exclude all private/internal fields from share DTOs, templates, and responses.
4. Display verification status in customer-safe language.
5. Keep public address distinct from internal address.

**Acceptance criteria:**

- Share cards work if internal records later change.
- Public share output contains no denied private fields or private values.
- Share cards do not expose direct internal entity identifiers unless explicitly designed as public IDs.

## Phase 5 - Hardening And Release

1. Broker authentication and authorization. Completed early during Phase 2 review hardening.
2. Add audit records for share snapshot creation and changes. Completed during Phase 5 hardening on 2026-06-11.
3. Add integration tests for MyBatis and MySQL behavior.
4. Add file access-control tests.
5. Add deployment/configuration docs.
6. Run manual QA for inspection capture, facility templates, map search, and customer share cards.

**Acceptance criteria:**

- Internal routes require broker access.
- Public routes expose only intended share-card surfaces.
- Build, tests, and manual QA pass before release.

## Test Strategy

- Unit tests for enum coverage, mappers, projection filtering, and gateway failure handling.
- MVC tests for internal forms and public share-card routes.
- Integration tests for MyBatis SQL behavior.
- Template rendering tests for denied-field absence.
- Manual browser QA for broker inspection and public share-card flows once UI exists.

## Initial Risks

- Privacy leakage through Thymeleaf templates if internal objects are passed directly.
- Search implementation drifting into internal-note scans unless `search_index` is introduced early.
- Facility templates becoming hard-coded if template tables are delayed.
- Address fields becoming ambiguous unless internal/public address names are explicit from Phase 1.

## First Worker Task

Implemented on 2026-06-05: Phase 0 created the Java 21 Spring Boot 3.x Gradle skeleton and context/MVC smoke tests without domain entities or share-card behavior.

## Second Worker Task

Implemented on 2026-06-05: Phase 1 created internal property inspection records, separate public share snapshot records, the closed verification-status enum, Flyway/MyBatis persistence shapes, and regression tests proving denied fields and sample private values are absent from public projections.

## Third Worker Task

Implemented on 2026-06-05: Phase 2 created broker-authenticated inspection create/edit screens, validated form capture for property basics/pricing/area/condition/internal notes, internal-only contact log persistence, hardened `FileStorage` attachment metadata flow, MyBatis write mappings, and controller/service/persistence-shape tests.

## Fourth Worker Task

Implemented on 2026-06-06: Phase 3 began with dynamic facility templates, independently persisted facility answers, and an explicit `GeocodingGateway` boundary. Geocoding now has disabled local defaults, Kakao Local REST adapter mapping, explicit failure reasons, configuration through `KAKAO_REST_API_KEY`, and focused regression tests.

## Fifth Worker Task

Implemented on 2026-06-06: Phase 3 integrated Kakao Maps through an explicit browser UI boundary on the broker inspection form. The map view is disabled by default, uses `KAKAO_MAP_JAVASCRIPT_KEY` when enabled, keeps the browser JavaScript key separate from `KAKAO_REST_API_KEY`, and has focused MVC/configuration tests plus live authenticated curl QA.

## Sixth Worker Task

Implemented on 2026-06-06: Phase 3 created the separate `property_search_index` structure for map and keyword search. Inspection create/update now refreshes the index from broker-safe fields only, V4/MyBatis/local mapper shapes keep it separate from internal notes, and regression tests cover template rendering, geocoding failures, and search-index updates.

## Seventh Worker Task

Implemented on 2026-06-11: Phase 4 created persisted customer share-card snapshots. Broker edit pages can generate share links, public `/share/{shareId}` pages render from snapshot rows only, customer-visible facility answers and public image metadata are stored as share-scoped child rows, and regression tests prove denied fields are absent and snapshots remain stable after internal inspection updates.

## Eighth Worker Task

Implemented on 2026-06-11: Phase 5 hardening added internal audit logging for share-card snapshot creation and regenerated share-card versions. `share_snapshot_audit_logs` records share ID, inspection ID, `CREATED` or `UPDATED`, and the broker actor while keeping public share DTOs/templates unchanged. Focused and full regression tests passed, and manual broker/public share QA confirmed the share surface still excludes private markers.
