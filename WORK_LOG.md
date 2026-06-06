# WORK LOG

## 2026-06-04 - Repository Planning Initialization

**Scope:** Initialize repository memory and planning documents for LazyCodex/OmO-based work.

**Actions completed:**

- Inspected `/mnt/c/dev/imjangbox`.
- Confirmed the repository currently contains only `.git` metadata and no product code.
- Confirmed no root `AGENTS.md`, `TASKS.md`, `WORK_LOG.md`, `CHECKPOINT.md`, `plans/`, or `docs/` files existed before initialization.
- Confirmed Git branch is `main`; there are no commits yet.
- Used command-local Git safe-directory configuration for status/log checks instead of changing global Git configuration.
- Created root `AGENTS.md` with project purpose, stack constraints, prohibited technologies/features, and hard privacy rules.
- Created `TASKS.md` with Phase 0 through Phase 5.
- Created `CHECKPOINT.md` with exact resume instructions.
- Created `plans/2026-06-04-imjangbox-implementation-plan.md`.
- Created `plans/0001-product-architecture-plan.md`.
- Created supporting docs in `docs/`.

**Constraints honored:**

- Do not modify product code yet.
- Only inspect the current repository and create planning/memory documents.
- Do not introduce React, Vue, Next.js, JPA, or prohibited automation features.

**Validation receipts:**

- RED validation before docs existed: `MISSING:AGENTS.md`.
- GREEN validation after docs existed: `VALIDATION:PASS`.
- Manual QA transcript: `/tmp/ulw-qa-init-docs.txt`.

## 2026-06-05 - Phase 0 Spring Boot Foundation

**Scope:** Execute the first worker task from `plans/2026-06-04-imjangbox-implementation-plan.md`.

**Actions completed:**

- Created Java 21 Gradle Spring Boot 3.5.14 skeleton from Spring Initializr.
- Added Gradle wrapper, `settings.gradle`, and `build.gradle`.
- Added Spring MVC, Thymeleaf, validation, MyBatis, MySQL driver, JUnit 5, and MyBatis test dependencies.
- Added package boundary markers for `property`, `inspection`, `share`, `facility`, `map`, `file`, `common`, and `web`.
- Added local, local database, and test profiles with secrets kept in environment variables.
- Added a minimal root MVC route and Thymeleaf template for smoke testing only.
- Documented Flyway as the migration strategy to introduce when Phase 1 schema work starts.
- Documented run/test commands in `README.md`.
- Updated `TASKS.md`, `CHECKPOINT.md`, and `AGENTS.md` for the new project state.

**Constraints honored:**

- No domain entities or public share-card behavior were added.
- No JPA, React, Vue, Next.js, or prohibited product automation features were introduced.
- Database credentials are placeholders only and remain outside source control.

**Validation receipts:**

- `JAVA_HOME=/tmp/imjangbox-jdk PATH=/tmp/imjangbox-jdk/bin:$PATH ./gradlew test` passed.
- Manual QA: `curl -i http://localhost:18080/` returned `HTTP/1.1 200` with the `imjangbox` page.
- Malformed route QA: `curl -i http://localhost:18080/not-a-route` returned `HTTP/1.1 404`.
- Cleanup QA: stopped `bootRun`; follow-up curl to port `18080` failed to connect as expected.

## 2026-06-05 - Phase 1 Privacy-First Domain And Persistence

**Scope:** Execute Phase 1 from `TASKS.md` and `plans/2026-06-04-imjangbox-implementation-plan.md`.

**Actions completed:**

- Added internal inspection records for property data, pricing, stakeholder phone, contact-log content, private memo, internal risk memo, internal address, and public address.
- Added `VerificationStatus` with exactly `UNVERIFIED`, `OWNER_CLAIM`, `TENANT_CLAIM`, `CO_BROKER_CLAIM`, `AGENT_CHECKED`, and `DOCUMENT_CHECKED`.
- Added public share snapshot records and a factory that copies only allowlisted fields from internal records.
- Added Flyway schema migration for internal inspection tables, stakeholder/contact-log private tables, and separate `public_share_snapshots`.
- Added dynamic facility-check answer shape and matching internal persistence table.
- Added MyBatis mapper XML, mapper row shape, and mapper registration marker for internal inspection persistence.
- Added a minimal public share-card template for privacy-contract rendering tests.
- Added regression tests for enum coverage, public snapshot type shape, serialized public projection privacy, template-rendered public projection privacy, and migration/mapper privacy shape.

**Constraints honored:**

- Public snapshot records do not embed internal inspection, internal address, stakeholder, contact log, or internal pricing records.
- Public projection and template tests prove denied names and sample private values are absent.
- No JPA, React, Vue, Next.js, or prohibited product automation features were introduced.

**Validation receipts:**

- Baseline red test: `./gradlew test` failed because Phase 1 domain/share types were missing.
- Review-found failures fixed: MyBatis mapper registration, missing facility structure, stale memory docs, and Thymeleaf template test setup.
- Green verification: `./gradlew clean test --rerun-tasks` passed.
- Focused privacy verification: `./gradlew test --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest` passed.
- Manual QA: JShell projection probe printed `PublicShareSnapshot[...]` and returned `PASS` after checking denied sample values.
- Runtime QA: `./gradlew bootRun --args='--server.port=18082'` started successfully; `curl -i http://localhost:18082/` returned `HTTP/1.1 200`; `curl -i http://localhost:18082/not-a-route` returned `HTTP/1.1 404`.
- Cleanup QA: stopped `bootRun`; follow-up curl to port `18082` failed to connect as expected.

## 2026-06-05 - Phase 2 Broker Inspection Ledger

**Scope:** Execute Phase 2 from `TASKS.md` and `plans/2026-06-04-imjangbox-implementation-plan.md`.

**Actions completed:**

- Added broker-facing create/edit routes at `/broker/inspections/new`, `/broker/inspections`, and `/broker/inspections/{inspectionId}/edit`.
- Added a Bootstrap 5.3 Thymeleaf form for internal address, public address, area, pricing, verification status, business-fit notes, condition notes, private price notes, private memo, internal risk memo, contact log, and attachments.
- Added validated `InspectionForm`, `InspectionService`, MyBatis write rows, and mapper methods for create/update, append-only contact-log writes, and attachment metadata insert.
- Added a `FileStorage` boundary with `StoredFile` metadata and a local-profile storage implementation.
- Added a local-profile in-memory mapper so `bootRun` works without MySQL while `local-db` remains the MyBatis/MySQL path.
- Added Flyway V2 migration for area/business-fit/condition columns and internal file attachment metadata.
- Added HTTP Basic broker authentication for `/broker/**`, leaving public home/share routes open.
- Added CSRF protection for broker forms.
- Added attachment count, size, content-type sniffing, and filename/storage-key hardening.
- Added controller, service, and persistence-shape regression tests.

**Constraints honored:**

- Contact logs, private memos, private price notes, storage keys, and internal risk memos remain internal broker surfaces only.
- Broker inspection routes require a broker-authenticated request.
- Public share DTOs/templates were not changed to accept internal inspection records.
- No JPA, React, Vue, Next.js, or prohibited product automation features were introduced.

**Validation receipts:**

- Baseline red test: focused Phase 2 tests failed because `InspectionService`, `InspectionForm`, `BrokerInspectionController`, `FileStorage`, and write-row types were missing.
- Green verification: `IMJANGBOX_BUILD_DIR=/tmp/imjangbox-root-final4 ./gradlew clean test --rerun-tasks` passed.
- LSP diagnostics: unavailable because `jdtls` is not installed; Gradle compile/test was used as the Java verification substitute.
- Runtime QA: `IMJANGBOX_BUILD_DIR=/tmp/imjangbox-root-boot3 ./gradlew bootRun --args='--server.port=18086'` started successfully.
- Auth QA: unauthenticated `curl -i http://localhost:18086/broker/inspections/new` returned `HTTP/1.1 401`; public `curl -i http://localhost:18086/` returned `HTTP/1.1 200`.
- CSRF QA: authenticated GET of `/broker/inspections/new` rendered exactly one `_csrf` field.
- Create QA: authenticated multipart POST with `_csrf` and a text attachment returned `HTTP/1.1 302`; follow-up GET of `/broker/inspections/41/edit` returned `HTTP/1.1 200` and showed the created title.
- Cleanup QA: stopped `bootRun`; follow-up curl to port `18086` failed to connect as expected.

## 2026-06-06 - Phase 2 Hardening Pass

**Scope:** Harden the existing Phase 2 broker inspection ledger before starting Phase 3.

**Actions completed:**

- Kept contact-log update behavior append-only and exposed contact logs as a list on the internal persistence read row.
- Updated the default local-profile mapper so it retains contact logs and file attachment metadata instead of silently dropping child writes.
- Added local mapper regression tests for contact-log retention, append-only update behavior, explicit contact-log deletion, and attachment metadata reads.
- Made `LocalFileStorage` use `imjangbox.file-storage.local-root` from application properties instead of hardcoding `java.io.tmpdir` in Java code.
- Updated local file-storage tests to prove files are written under the configured root.
- Updated `README.md` with Phase 2 broker credentials, `/broker/inspections/new`, `local` vs `local-db`, attachment storage, and test commands.

**Constraints honored:**

- No Phase 3 facility, map, geocoding, or search-index features were started.
- Public share DTOs and templates remain separate from internal inspection records.
- Contact logs, attachment storage keys, private memos, private price notes, and internal risk memos remain internal-only surfaces.

**Validation receipts:**

- Focused local mapper verification: `./gradlew test --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest` passed.
- Focused file-storage verification: `./gradlew test --tests com.imjangbox.file.LocalFileStorageTest` passed.
- Focused persistence privacy-shape verification: `./gradlew test --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest` passed.
- Focused public share privacy verification: `./gradlew test --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest` passed.
- Full acceptance verification: `./gradlew test` passed.
- Manual QA: `./gradlew bootRun --args='--server.port=18091 --imjangbox.file-storage.local-root=/tmp/imjangbox-phase2-hardening-files'` started on the default `local` profile.
- Broker create QA: authenticated GET of `/broker/inspections/new` returned a CSRF token; authenticated multipart POST with a text attachment returned `HTTP/1.1 302` to `/broker/inspections/41/edit`; follow-up GET returned `HTTP/1.1 200` and rendered `Phase 2 hardening QA`.
- Attachment root QA: uploaded text attachment was written under `/tmp/imjangbox-phase2-hardening-files/inspection-files/41/`.
- Cleanup QA: stopped `bootRun`; follow-up curl to port `18091` failed to connect as expected.

## 2026-06-06 - Phase 3 Facility Template Definitions

**Scope:** Execute the first Phase 3 task from `TASKS.md`: design dynamic facility-check templates by business type.

**Actions completed:**

- Added `FacilityTemplateItem`, `FacilityTemplateMapper`, `FacilityTemplateService`, and a local-profile mapper backed by `imjangbox.facility-templates` configuration.
- Added Flyway V3 schema for `facility_check_templates`, seeded initial CAFE and RESTAURANT template definitions, and kept inspection-specific answers separate from template definitions.
- Added `business_type` to inspection write/read persistence so broker forms can select the relevant template family.
- Rendered a Bootstrap/Thymeleaf facility-check template section on the broker inspection form, while preserving the existing free-form condition memo.
- Added regression tests for template rendering and template/answer schema separation.

**Constraints honored:**

- Template items are data/configuration driven, not hard-coded in Java or Thymeleaf.
- Facility answers remain separate from template definitions.
- No public share DTOs or templates were changed to expose internal records.
- No JPA, React, Vue, Next.js, or prohibited product automation features were introduced.

**Validation receipts:**

- Baseline characterization: `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest` passed before production changes with the existing free-form condition memo pinned.
- Red Phase 3 test: focused tests failed before implementation because `FacilityTemplateItem`, `FacilityTemplateService`, and V3 migration were missing.
- Focused green verification: `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest` passed.
- Full verification: `./gradlew clean test --rerun-tasks` passed.
- LSP diagnostics: unavailable because `jdtls` is not installed; Gradle compile/test was used as the Java verification substitute.
- Manual QA: `./gradlew bootRun --args='--server.port=18092'` started on the default `local` profile.
- Broker template QA: authenticated GET `/broker/inspections/new?businessType=CAFE` returned `HTTP/1.1 200` and rendered `급배수 확인`, `전기 용량 확인`, and `facilityAnswers[0].templateItemKey`.
- Business-type variation QA: authenticated GET `/broker/inspections/new?businessType=RESTAURANT` returned `HTTP/1.1 200` and rendered `배기 덕트 확인` and `그리스트랩 확인`.
- Malformed business-type QA: authenticated GET `/broker/inspections/new?businessType=%3Cscript%3E` returned `HTTP/1.1 200` and rendered the no-template empty state without crashing.
- Cleanup QA: stopped `bootRun`; follow-up curl to port `18092` failed to connect as expected.

## 2026-06-06 - Phase 3 Facility Answer Persistence

**Scope:** Complete the second Phase 3 task from `TASKS.md`: store facility answers independently from template definitions.

**Actions completed:**

- Added `FacilityAnswerWriteRow` and MyBatis/local mapper methods for deleting, inserting, and reading inspection-specific facility answers.
- Persisted non-empty facility answers from broker create/update flows without storing answers in template-definition rows.
- Reloaded saved facility answers on edit and merged them with active templates, preserving historical saved rows if templates later change.
- Preserved selected facility answers when unrelated validation or attachment errors redisplay the form.
- Added service, controller, local mapper, and persistence-shape regression tests.

**Validation receipts:**

- Review red finding: post-implementation code review identified that editable facility answers were not persisted or preserved on validation errors.
- Red test: focused service/controller tests failed before implementation because `FacilityAnswerWriteRow` and mapper methods were missing.
- Focused green verification: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest` passed.
- Full verification: `./gradlew test` passed.
- Manual QA: `./gradlew bootRun --args='--server.port=18093'` started on the default `local` profile.
- Broker create/edit QA: authenticated GET `/broker/inspections/new?businessType=CAFE` returned `HTTP/1.1 200`; authenticated multipart POST with CSRF and `facilityAnswers[0].answer=OK` returned `HTTP/1.1 302`; follow-up GET of the edit URL returned `HTTP/1.1 200` and rendered `급배수 확인` with `OK` selected.
- Cleanup QA: stopped `bootRun`; follow-up curl to port `18093` failed to connect as expected.

## 2026-06-06 - Phase 3 GeocodingGateway Boundary

**Scope:** Complete the Phase 3 `GeocodingGateway` task before Kakao Maps UI/search-index work.

**Actions completed:**

- Added `GeocodingGateway` with explicit success/failure result types and failure reasons for invalid, ambiguous, unavailable, and provider-failed geocoding.
- Added a disabled local/default gateway that keeps startup green without a Kakao secret and returns explicit internal failure results.
- Added a Kakao Local REST adapter for address search, mapping Kakao `x`/`y` response fields to longitude/latitude coordinates.
- Added configuration properties for enabling Kakao geocoding with `KAKAO_REST_API_KEY` while keeping geocoding disabled by default.
- Added focused regression tests for disabled-mode failures, blank input, successful Kakao mapping, no-result invalid address, ambiguous multi-result responses, provider failures, and malformed provider documents.

**Validation receipts:**

- Red test: `./gradlew test --tests 'com.imjangbox.map.*'` failed before implementation because the gateway/result/provider types were missing.
- Focused green verification: `./gradlew test --tests 'com.imjangbox.map.*'` passed.
- Full verification: `./gradlew test` passed.
- Clean verification: `./gradlew clean test --rerun-tasks` passed.
- LSP diagnostics: unavailable because `jdtls` is not installed; Gradle clean compile/test was used as the Java verification substitute.
- Manual QA: `./gradlew bootRun --args='--server.port=18094'` started on the default `local` profile; `curl -i --max-time 5 http://localhost:18094/` returned `HTTP/1.1 200`.
- Gateway API QA: JShell against `/tmp/imjangbox-build/classes/java/main` returned `Failure[reason=UNAVAILABLE, ...]` for a normal address and `Failure[reason=INVALID_ADDRESS, ...]` for blank input through `DisabledGeocodingGateway`.
- Cleanup QA: stopped `bootRun`; follow-up curl to port `18094` failed to connect as expected and `/tmp/imjangbox-port-18094-check.txt` was removed.

## 2026-06-06 - Phase 3 Kakao Maps UI Boundary

**Scope:** Complete the Phase 3 task to integrate Kakao Maps through explicit UI and backend boundaries.

**Actions completed:**

- Added `KakaoMapProperties`, `KakaoMapView`, and `KakaoMapViewFactory` for browser map display configuration.
- Kept Kakao Maps browser display disabled by default and configured through `KAKAO_MAP_JAVASCRIPT_KEY`.
- Kept browser JavaScript-key configuration separate from backend `KAKAO_REST_API_KEY` geocoding configuration.
- Added a broker inspection form map section that renders a disabled status by default or a Kakao Maps SDK-backed map container when enabled.
- Added focused MVC and map factory regression tests for disabled rendering, enabled SDK data, URL encoding, default coordinates, and REST-key absence.

**Validation receipts:**

- Baseline characterization: `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest` passed before production changes.
- Red test: focused map/UI tests failed before implementation because `KakaoMapView`, `KakaoMapViewFactory`, and `KakaoMapProperties` were missing.
- Focused green verification: `./gradlew test --tests com.imjangbox.map.KakaoMapViewFactoryTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest` passed.
- Full verification: `./gradlew test` passed.
- Final clean verification: `./gradlew clean test --rerun-tasks` passed.
- LSP diagnostics: unavailable because `jdtls` is not installed; Gradle compile/test was used as the Java verification substitute.
- Enabled runtime QA: `KAKAO_MAP_JAVASCRIPT_KEY='browser key+only' ./gradlew bootRun --args='--server.port=18095 --imjangbox.maps.kakao.enabled=true'` started successfully; authenticated GET `/broker/inspections/new?businessType=CAFE` returned `HTTP/1.1 200` and rendered `data-kakao-map`, an encoded SDK URL, default coordinates, and no REST API key text.
- Auth QA: unauthenticated GET `/broker/inspections/new` on port `18095` returned `HTTP/1.1 401`.
- Malformed business-type QA: authenticated GET with `businessType=%3Cscript%3Ealert(1)%3C%2Fscript%3E` returned `HTTP/1.1 200`, rendered the no-template empty state, and did not echo executable markup.
- Disabled runtime QA: `./gradlew bootRun --args='--server.port=18096'` started successfully; authenticated GET `/broker/inspections/new` returned `HTTP/1.1 200`, rendered the disabled map status, and did not render a Kakao SDK URL.
- Cleanup QA: stopped both `bootRun` sessions; follow-up curls to ports `18095` and `18096` failed to connect, and `/tmp/imjangbox-kakao-map-qa.*` artifacts were removed.

## 2026-06-06 - Phase 3 Search Index Structure

**Scope:** Complete the Phase 3 tasks to create a separate search-index structure and cover template, geocoding, and search-index regressions.

**Actions completed:**

- Added `SearchIndexWriteRow` and dedicated `PropertyInspectionMapper` methods for upserting and reading `property_search_index` rows.
- Added local-profile search-index storage so the default broker flow refreshes index data without MySQL.
- Added Flyway V4 migration for `property_search_index` with safe searchable fields, nullable coordinate placeholders, and index keys for text, business type, verification, and coordinates.
- Refreshed the search index from `InspectionService` after create/update using broker-safe fields from `InspectionForm`.
- Kept private memo, private price note, contact-log content, internal risk memo, and raw internal address out of index text and persistence shape.
- Added regression tests for service refresh, local search-index storage, migration privacy shape, MyBatis statement shape, template rendering, geocoding failure handling, and search-index updates.

**Validation receipts:**

- Baseline characterization: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest` passed before production changes.
- Red test: focused search-index tests failed before implementation because `SearchIndexWriteRow`, mapper methods, V4 migration, and XML statements were missing.
- Focused green verification: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest` passed.
- Full verification: `./gradlew test` passed.
- Final clean verification: `./gradlew clean test --rerun-tasks` passed.
- LSP diagnostics: unavailable because `jdtls` is not installed; Gradle compile/test was used as the Java verification substitute.
- Runtime QA: `./gradlew bootRun --args='--server.port=18097'` started successfully; authenticated create with private leak markers returned `HTTP/1.1 302`, the edit page returned `HTTP/1.1 200`, and authenticated update returned `HTTP/1.1 302`.
- Cleanup QA: stopped `bootRun`; follow-up curl to port `18097` failed to connect, and `/tmp/imjangbox-search-index-qa.*` artifacts were removed.
