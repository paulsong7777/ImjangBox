# TASKS

**Project:** imjangbox
**Last updated:** 2026-06-13

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
- [x] Complete, push, verify, and close Issue #4 mobile-first broker inspection form UX.
- [x] Confirm all post-release GitHub issues #1, #2, and #4 are closed.
- [x] Keep Phase 6 unstarted unless explicitly requested.

## Issue #4 - Mobile-First Broker Form UX

- [x] Plan Issue #4 mobile-first broker inspection form UX improvements.
- [x] Define the smallest product-facing UX pass before changing implementation.
- [x] Record the quick-save vs detailed follow-up field split and acceptance criteria in `plans/2026-06-12-issue-4-mobile-first-broker-form-ux-plan.md`.
- [x] Implement the Issue #4 mobile-first broker form UX pass after the explicit implementation request.
- [x] Characterize the mobile-first target form structure with broker controller/template tests before restructuring.
- [x] Keep existing routes, field names, CSRF behavior, validation, attachment upload, facility template, map/geocode, contact log, and share-card behavior intact.
- [x] Verify the planned UX pass with focused broker/map/file/share tests, full tests, and manual mobile-width QA.
- [x] Do not start Phase 6 implementation unless explicitly requested.
- [x] Close GitHub Issue #4 after implementation, push, and CI verification.

## Product UX Elevation - Broker-Facing MVP Polish

- [x] Improve visible Korean-first product language on the home page, broker form, and public share card.
- [x] Reduce broker form noise with a prominent required quick-registration section and secondary follow-up sections.
- [x] Keep save/update actions separate from customer share-card generation.
- [x] Preserve existing routes, form field names, CSRF behavior, validation, attachment upload, facility bindings, Kakao map boundary, contact logs, public share privacy, and file-storage validation.
- [x] Add controller/template tests for Korean product wording, preserved bindings, internal-only separation, and share action separation.
- [x] Document the product UX language and grouping principles in `docs/product-ux-notes.md`.
- [x] Verify with focused tests, full tests, documentation validation, `git diff --check`, and manual local QA.

## Product Core Reset - Property Map/List Management First

- [x] Re-center the MVP from inspection-form-first to property map/list management first.
- [x] Add broker property management routes: `/broker` redirects to `/broker/inspections`, and `/broker/inspections` renders the core management screen.
- [x] Render `내 상가 매물` with map placeholder, presentational status chips, property cards, and a clean empty state.
- [x] Add read-only list support through the existing inspection service and mapper boundary without database schema changes.
- [x] Keep create/edit routes, field names, CSRF behavior, validation, facility bindings, Kakao map boundary, contact logs, attachment validation, and share-card POST behavior intact.
- [x] Point the home page to property management and registration instead of a form-only entry.
- [x] Make the create form feel like quick property registration while keeping detailed follow-up sections available.
- [x] Rebuild the public share card as a customer proposal template using only public snapshot data.
- [x] Add home, broker dashboard, mapper, and public share regression tests for the reset.
- [x] Document the reset and manual QA results before committing.

## Product UI Fit Pass - Property Dashboard And Proposal Polish

- [x] Keep this work outside Phase 6 and avoid new product features.
- [x] Refine `/broker/inspections` so the map area is smaller, less empty, and secondary to property cards.
- [x] Remove developer-facing dashboard map copy and replace it with natural product copy.
- [x] Improve property cards with product-style photo placeholders, price tiles, `만원` units, area/location/business/status hierarchy, and clearer edit/share actions.
- [x] Reframe the broker form as `상가 매물 등록`, with quick registration copy centered on title, address, customer-safe location, price, premium, area, recommended business, photos, and short memo.
- [x] Soften disabled map wording in the broker form while preserving Kakao map boundaries and field bindings.
- [x] Refine the public share card into a proposal-style customer template with price units and customer-safe verification labels.
- [x] Preserve share-card privacy rules for internal address, internal notes, contact logs, storage keys, local paths, and original filenames.
- [x] Update focused UI/privacy tests and project docs for the pass.

## Commercial Domain Fit Pass v1 - Business Categories And Facility Checks

- [x] Keep this work outside Phase 6 and avoid DB schema changes or Flyway migrations.
- [x] Expand broker-facing recommended business categories to practical commercial-property types.
- [x] Preserve migrationless compatibility for existing `CAFE`, `RESTAURANT`, and `GENERAL` values.
- [x] Render expanded Korean business labels in the broker form.
- [x] Expand default facility-check templates by business type for cafe/dessert, restaurant, bar/night, delivery/takeout, beauty, academy, clinic, office, retail, studio/workshop, fitness, unmanned store, storage/workspace, and general cases.
- [x] Show practical business labels on dashboard property cards without implying automatic suitability judgment.
- [x] Show broker-entered recommended business type on customer proposal cards without exposing internal fields.
- [x] Add focused tests for business-type labels, facility template generation, dashboard labels, public share labels, and privacy preservation.
- [x] Update project docs and validation notes for the commercial domain fit pass.

## Next Work Area - Explicit Product Planning

- [ ] Wait for explicit next-product planning or implementation request before starting any further product area.
- [ ] Do not start Phase 6 implementation unless explicitly requested.
