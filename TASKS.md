# TASKS

**Project:** imjangbox
**Last updated:** 2026-06-12

## Phase 0 - Repository And Spring Boot Foundation

- [x] Create a Java 21 Gradle project for Spring Boot 3.x.
- [x] Add Spring MVC, Thymeleaf, validation, MyBatis, MySQL driver, and JUnit 5 dependencies.
- [x] Define package structure for property ledger, inspections, sharing, maps, files, and common support.
- [x] Add application configuration profiles for local development and tests.
- [x] Add database migration strategy decision before schema work starts.
- [x] Add first smoke tests for application context and MVC routing.
- [x] Document local run and test commands after the skeleton exists.

## Phase 1 - Domain Model, Privacy Boundary, And Persistence Plan

- [x] Design internal property, stakeholder, contact log, pricing, facility, verification, and address structures.
- [x] Define public share snapshot structures separately from internal records.
- [x] Encode denied public fields: `private_memo`, `price_private_note`, `stakeholder.phone`, `contact_log.content`, `internal_risk_memo`.
- [x] Define verification status enum values: `UNVERIFIED`, `OWNER_CLAIM`, `TENANT_CLAIM`, `CO_BROKER_CLAIM`, `AGENT_CHECKED`, `DOCUMENT_CHECKED`.
- [x] Create MySQL schema and MyBatis mapper plan for internal records.
- [x] Add tests proving public projections cannot expose private fields.

## Phase 2 - Field Inspection Capture

- [x] Build internal broker-facing forms with Thymeleaf and Bootstrap 5.3.
- [x] Capture commercial property basics, internal address, public address, premium/key-money, rent, area, condition, and broker notes.
- [x] Implement contact log capture as internal-only data.
- [x] Implement file attachment flow through `FileStorage`.
- [x] Add validation for required fields and safe defaults.
- [x] Add controller/service/mapper tests for inspection create and update flows.

## Phase 3 - Facility Templates, Maps, And Search Index

- [x] Design dynamic facility-check templates by business type.
- [x] Store facility answers independently from template definitions.
- [x] Integrate Kakao Maps through explicit UI and backend boundaries.
- [x] Implement `GeocodingGateway` for address-to-coordinate lookup.
- [x] Create separate `search_index` structure for map and keyword search.
- [x] Add tests for template rendering, geocoding failure handling, and search-index updates.

## Phase 4 - Customer Share Cards

- [x] Generate share card snapshots from internal records.
- [x] Render share cards with Thymeleaf and Bootstrap 5.3.
- [x] Ensure public address and internal address remain separated.
- [x] Ensure private fields are absent from public DTOs, templates, JSON responses, and snapshots.
- [x] Add customer-safe verification display rules.
- [x] Add regression tests for every denied field and each verification status.

## Phase 5 - Hardening, Operations, And Release Readiness

- [x] Add authentication/authorization for broker-only internal screens.
- [x] Add audit logging for share-card creation and updates.
- [x] Add integration tests for MyBatis/MySQL behavior using the chosen test database approach.
- [x] Add file-storage validation for size, type, and access control.
- [x] Add operational docs for deployment, configuration, and backup.
- [x] Run full manual QA across inspection capture, map search, and share card views.
- [x] Revisit AGENTS.md, TASKS.md, WORK_LOG.md, CHECKPOINT.md, plans/, and docs/ after product code landed.

## Release Publication - v0.2.0-alpha

- [x] Inspect completed Phase 0 through Phase 5 state from repository docs and current source layout.
- [x] Confirm the next alpha version should be `v0.2.0-alpha` because `v0.1.0-alpha` is already tagged and Phase 4/5 work landed afterward.
- [x] Update README release status away from the older `v0.1.0-alpha` in-progress wording.
- [x] Update CHANGELOG with the `v0.2.0-alpha` release scope.
- [x] Create release notes for `v0.2.0-alpha`.
- [x] Check local docs for GitHub issue status; no local issue export or open-issue ledger exists, and no network verification is required for this release-preparation pass.
- [x] Confirm SECURITY, CONTRIBUTING, operations, validation, and privacy docs are consistent with the Phase 5 state.
- [x] Run release documentation validation: docs validation, `git diff --check`, and `./gradlew test`.
- [x] Publish GitHub pre-release `v0.2.0-alpha`.
- [x] Synchronize repository documentation with the published `v0.2.0-alpha` release state.

## GitHub Issue Cleanup And Product Planning

- [x] Close completed Issue #1 Phase 4 share-card snapshot generation.
- [x] Close completed Issue #2 public share-card privacy/security verification.
- [x] Confirm Issue #4 mobile-first broker inspection form UX remains open as the next product-facing improvement candidate.
- [x] Keep Phase 6 unstarted unless explicitly requested.

## Next Work Area - Mobile-First Broker Form UX Planning

- [x] Plan Issue #4 mobile-first broker inspection form UX improvements.
- [x] Define the smallest product-facing UX pass before changing implementation.
- [x] Record the quick-save vs detailed follow-up field split and acceptance criteria in `plans/2026-06-12-issue-4-mobile-first-broker-form-ux-plan.md`.
- [ ] Implement the Issue #4 mobile-first broker form UX pass only after an explicit implementation request.
- [ ] Keep Issue #4 open until the planned UX pass is implemented and verified.
- [ ] Do not start Phase 6 implementation unless explicitly requested.
