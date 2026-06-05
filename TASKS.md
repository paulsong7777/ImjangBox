# TASKS

**Project:** imjangbox
**Last updated:** 2026-06-04

## Phase 0 - Repository And Spring Boot Foundation

- [x] Create a Java 21 Gradle project for Spring Boot 3.x.
- [x] Add Spring MVC, Thymeleaf, validation, MyBatis, MySQL driver, and JUnit 5 dependencies.
- [x] Define package structure for property ledger, inspections, sharing, maps, files, and common support.
- [x] Add application configuration profiles for local development and tests.
- [x] Add database migration strategy decision before schema work starts.
- [x] Add first smoke tests for application context and MVC routing.
- [x] Document local run and test commands after the skeleton exists.

## Phase 1 - Domain Model, Privacy Boundary, And Persistence Plan

- [ ] Design internal property, stakeholder, contact log, pricing, facility, verification, and address structures.
- [ ] Define public share snapshot structures separately from internal records.
- [ ] Encode denied public fields: `private_memo`, `price_private_note`, `stakeholder.phone`, `contact_log.content`, `internal_risk_memo`.
- [ ] Define verification status enum values: `UNVERIFIED`, `OWNER_CLAIM`, `TENANT_CLAIM`, `CO_BROKER_CLAIM`, `AGENT_CHECKED`, `DOCUMENT_CHECKED`.
- [ ] Create MySQL schema and MyBatis mapper plan for internal records.
- [ ] Add tests proving public projections cannot expose private fields.

## Phase 2 - Field Inspection Capture

- [ ] Build internal broker-facing forms with Thymeleaf and Bootstrap 5.3.
- [ ] Capture commercial property basics, internal address, public address, premium/key-money, rent, area, condition, and broker notes.
- [ ] Implement contact log capture as internal-only data.
- [ ] Implement file attachment flow through `FileStorage`.
- [ ] Add validation for required fields and safe defaults.
- [ ] Add controller/service/mapper tests for inspection create and update flows.

## Phase 3 - Facility Templates, Maps, And Search Index

- [ ] Design dynamic facility-check templates by business type.
- [ ] Store facility answers independently from template definitions.
- [ ] Integrate Kakao Maps through explicit UI and backend boundaries.
- [ ] Implement `GeocodingGateway` for address-to-coordinate lookup.
- [ ] Create separate `search_index` structure for map and keyword search.
- [ ] Add tests for template rendering, geocoding failure handling, and search-index updates.

## Phase 4 - Customer Share Cards

- [ ] Generate share card snapshots from internal records.
- [ ] Render share cards with Thymeleaf and Bootstrap 5.3.
- [ ] Ensure public address and internal address remain separated.
- [ ] Ensure private fields are absent from public DTOs, templates, JSON responses, and snapshots.
- [ ] Add customer-safe verification display rules.
- [ ] Add regression tests for every denied field and each verification status.

## Phase 5 - Hardening, Operations, And Release Readiness

- [ ] Add authentication/authorization for broker-only internal screens.
- [ ] Add audit logging for share-card creation and updates.
- [ ] Add integration tests for MyBatis/MySQL behavior using the chosen test database approach.
- [ ] Add file-storage validation for size, type, and access control.
- [ ] Add operational docs for deployment, configuration, and backup.
- [ ] Run full manual QA across inspection capture, map search, and share card views.
- [ ] Revisit AGENTS.md, TASKS.md, WORK_LOG.md, CHECKPOINT.md, plans/, and docs/ after first product code lands.
