# Project Memory

## Name

imjangbox

## Purpose

imjangbox is a commercial-property field inspection and proposal ledger for Korean commercial real estate brokers. It supports on-site property capture, business-fit organization, facility-condition tracking, premium/key-money details, verification status, and safe customer proposal cards.

## Primary Users

- Commercial real estate brokers recording field inspections.
- Broker teams reviewing internal notes, verification status, and proposal readiness.
- Customers receiving sanitized share cards.

## Product Boundary

The system is a broker workflow and proposal ledger. It is not a listing-platform uploader, legal-decision engine, commercial district scoring engine, or automatic advertising-copy generator.

## Required Technical Direction

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

## Repository State

As of 2026-06-04, the repository had no product code, build files, or docs before initialization. Planning/memory documents were added first so implementation can start from explicit constraints.

As of 2026-06-05, Phase 2 broker inspection ledger create/edit flow exists. Internal records are modeled separately from public share snapshots; broker inspection routes require authentication; Flyway migrations and MyBatis mapper XML define internal inspection persistence, contact logs, and attachment metadata without exposing public snapshot DTOs.

As of 2026-06-06, Phase 3 is complete. Dynamic facility-check template definitions by business type are stored separately from inspection-specific answers, exposed through a facility mapper/service boundary, and rendered on the broker inspection form from MySQL data or local-profile configuration. Facility answers are persisted independently from template definitions and reload on broker edit screens. `GeocodingGateway` exists as an explicit backend boundary with disabled local defaults and a Kakao REST adapter. The broker inspection form also has a Kakao Maps browser UI boundary that uses `KAKAO_MAP_JAVASCRIPT_KEY`, stays disabled by default, and remains separate from the backend REST geocoding key. `property_search_index` is a separate search/map structure refreshed from broker-safe fields on inspection create/update.

As of 2026-06-11, Phase 4 is complete. Broker edit pages can create customer share links backed by persisted `public_share_snapshots` rows and child snapshot rows for customer-visible facility summaries and share-scoped image metadata. Public `/share/{shareId}` pages render from snapshots only, not live internal inspection rows, so existing cards remain stable after internal updates.

As of later on 2026-06-11, Phase 5 is complete. Share-card generation writes internal audit records to `share_snapshot_audit_logs`; first snapshots are marked `CREATED`, and later broker-generated snapshot versions for the same inspection are marked `UPDATED`. SQL-backed MyBatis integration tests run through a `mybatis-integration` test profile using H2 in MySQL compatibility mode with a test-only final schema, covering mapper XML behavior for inspection rows, facility answers, search-index upserts, share snapshots, share children, and share audit logs. File upload validation enforces attachment count, size, allowed content type, filename extension/content-type consistency, and header/content-type consistency before storage. Operational docs cover profiles, secrets, Kakao key separation, file/database backups, small-server deployment, smoke tests, and rollback. Full manual QA across inspection capture, map UI/search-index coverage, and public share-card views passed on 2026-06-11.

As of 2026-06-12, Issue #4 mobile-first broker inspection form UX planning is complete in `plans/2026-06-12-issue-4-mobile-first-broker-form-ux-plan.md`. The plan keeps the existing create/edit routes, validation, CSRF behavior, attachment boundary, dynamic facility answers, Kakao map view model, search-index shape, and public share-card snapshot behavior intact. It proposes a mobile-first quick-save section for required capture fields and attachments, with optional facility, location, memo, internal-only, contact-log, and share-card work grouped as detailed follow-up. Implementation has not started.

## Key Assumptions

- Public customer share cards should be generated from snapshot data.
- Internal broker records may contain sensitive notes, contact details, risk memos, and operational context.
- Public proposal content should be curated and factual, not automatically generated promotional copy.
- Facility-check templates should be data-driven by business type.
- Geocoding failures should be represented as explicit internal result states, not as partial inspection writes.
- Kakao Maps browser display should be configured separately from backend geocoding and should not expose `KAKAO_REST_API_KEY`.
- Search and maps should use a dedicated search/index structure rather than private internal notes.
- Share cards should be regenerated as new snapshots when customer-facing content needs to change.
- Share-card snapshot generation should be auditable internally without expanding public share DTOs or templates.
- Mapper integration tests should remain deterministic in local `./gradlew test` runs and should not require Docker or an external database service.
- The Issue #4 broker form UX pass should be a mobile-first reshape of the existing server-rendered Thymeleaf/Bootstrap form, not a new draft-save workflow or Phase 6 feature.
