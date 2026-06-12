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

## 2026-06-11 - Phase 4 Customer Share Cards

**Scope:** Implement Phase 4 customer share-card snapshots only.

**Actions completed:**

- Expanded `PublicShareSnapshot` with customer-visible facility summaries and share-scoped public image metadata.
- Added `ShareSnapshotService`, `ShareSnapshotMapper`, local-profile share snapshot storage, MyBatis XML, and Flyway V5 child snapshot tables.
- Added broker-only share generation at `POST /broker/inspections/{inspectionId}/share`.
- Added public share rendering at `GET /share/{shareId}` using only persisted snapshot rows.
- Added a Bootstrap 5.3 public share-card template with public address, public pricing, verification label, visible facility summary, and public image references.
- Added a share-scoped public image route that streams selected image attachments by share ID and display order without exposing storage keys or original filenames.
- Kept storage keys, original filenames, internal addresses, private memos, private price notes, contact logs, stakeholder contacts, internal risk fields, and search-index-only fields out of public DTOs/templates/mapper output.
- Added regression tests for snapshot stability after internal updates, denied-field absence in JSON and rendered HTML, share mapper privacy shape, public route rendering, broker share-link generation, and verification labels for every status.

**Constraints honored:**

- Existing public share cards remain stable after internal inspection records change.
- Public routes read snapshot rows, not live internal inspection records.
- No external listing upload, AI ad-copy generation, customer CRM, payment, legal judgment, or commercial district scoring was started.
- No JPA, React, Vue, or Next.js was introduced.

**Validation receipts:**

- Full verification: `./gradlew test` passed.
- Runtime QA: `./gradlew bootRun` started on the default `local` profile at `http://localhost:8080`.
- Broker create QA: authenticated GET `/broker/inspections/new` returned a CSRF token; authenticated POST to `/broker/inspections` returned `HTTP/1.1 302` to `/broker/inspections/41/edit`.
- Share generation QA: authenticated POST `/broker/inspections/41/share` returned `HTTP/1.1 302` to `/share/b2eb74da-b41e-4e2a-a30b-e70b1d9f26ec`.
- Public render QA: unauthenticated GET of that share URL returned `HTTP/1.1 200` and rendered `QA Share Card`, `PUBLIC_DISTRICT_QA`, `Agent checked`, `급배수 확인`, and a share-scoped `/images/1` URL without the private marker values.
- Public image QA: unauthenticated GET `/share/{shareId}/images/1` returned `HTTP/1.1 200`, `Content-Type: image/png`, and the selected PNG bytes.
- Snapshot stability QA: after updating internal inspection `41` to `UPDATED_PUBLIC_TITLE`, `UPDATED_PUBLIC_ADDRESS`, `UPDATED_PRIVATE_MEMO`, and `UPDATED_RISK`, the same public share URL still rendered the original `QA Share Card`, `PUBLIC_DISTRICT_QA`, and `Agent checked`.
- Cleanup QA: stopped the local `bootRun` processes; follow-up process check found no running `bootRun`/`ImjangboxApplication` process.

## 2026-06-11 - Phase 5 Share Snapshot Audit Logging

**Scope:** Complete the next unchecked Phase 5 MVP task: audit logging for share-card creation and updates.

**Actions completed:**

- Added `share_snapshot_audit_logs` in Flyway V6 with share ID, inspection ID, action, broker actor, and creation timestamp.
- Added share audit row/write records and mapper methods for inserting and reading audit records.
- Updated `ShareSnapshotService` to write `CREATED` for the first generated snapshot of an inspection and `UPDATED` for later broker-generated snapshot versions of the same inspection.
- Passed the authenticated broker username from `BrokerInspectionController` into share snapshot generation.
- Kept public share DTOs, templates, and public routes unchanged so audit data remains internal-only.
- Added regression coverage for `CREATED` and `UPDATED` audit actions, controller actor propagation, and V6 privacy-safe persistence shape.

**Constraints honored:**

- Existing public share cards remain stable and are still backed by snapshot rows, not live internal records.
- Audit rows do not add private memo, private price note, contact-log content, internal risk memo, internal address, file storage keys, or audit data to public DTOs/templates.
- Phase 5 broker authentication was not duplicated.
- No JPA, React, Vue, Next.js, or prohibited product automation features were introduced.

**Validation receipts:**

- Focused verification: `./gradlew test --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest` passed.
- Full verification: `./gradlew test` passed.
- Manual QA: `./gradlew bootRun --args='--server.port=18101'` started on the default `local` profile.
- Broker/public share QA: authenticated create returned a broker edit redirect for inspection `43`; two authenticated share-generation POSTs returned `/share/d0278ab2-9708-4cad-88a8-a07560459760` and `/share/934822df-8ebd-418e-927b-4fffa62a9c8e`; unauthenticated GETs of both share pages returned public content and excluded `PRIVATE_MEMO_AUDIT_QA`, `PRIVATE_RISK_AUDIT_QA`, and `PRIVATE_INTERNAL_AUDIT_QA`.
- Cleanup QA: stopped `bootRun`; follow-up process check found no running `bootRun`/`ImjangboxApplication` process.

## 2026-06-11 - Phase 5 MyBatis Integration Tests

**Scope:** Complete the next unchecked Phase 5 hardening task: integration tests for MyBatis/MySQL behavior using the chosen local-friendly test database approach.

**Actions completed:**

- Added a dedicated `mybatis-integration` test profile backed by H2 in MySQL compatibility mode.
- Added a test-only final-schema Flyway migration under `src/test/resources/db/mybatis-integration` so mapper integration tests are deterministic without Docker or an external MySQL server.
- Added `MyBatisPersistenceIntegrationTest` covering actual mapper XML execution for inspection insert/read, facility answers, attachment metadata reads, search-index upsert refresh, share snapshot rows, share facility/image child rows, share image lookup, and share audit logs.
- Fixed MyBatis constructor result-map aliases for primitive `long`, `int`, and `boolean` row-record constructor parameters.
- Kept production persistence on MySQL/Flyway/MyBatis; the H2 dependency is test-runtime only.

**Constraints honored:**

- No broker authentication work was duplicated.
- No Phase 6 or unrelated product features were started.
- Public share snapshots and templates remain separate from internal inspection records.
- Private memo, private price note, contact-log content, internal risk memo, internal address, storage keys, and audit rows remain out of public share DTOs/templates.

**Validation receipts:**

- Focused integration verification: `./gradlew test --tests com.imjangbox.inspection.persistence.MyBatisPersistenceIntegrationTest` passed.
- Focused privacy/share verification: `./gradlew test --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest` passed.
- Full verification: `./gradlew test` passed.

## 2026-06-11 - Phase 5 File Storage Validation And Access Control

**Scope:** Complete the next unchecked Phase 5 hardening task: file-storage validation for size, type, and access control.

**Actions completed:**

- Added `AttachmentFilePolicy` as the shared file-boundary allowlist for internal attachments and public share image content types.
- Kept broker upload validation in `InspectionService` and strengthened it to require matching filename extension, declared content type, and file header before any `FileStorage.store` call.
- Preserved existing attachment limits: maximum 5 non-empty attachments and maximum 10 MiB per attachment.
- Updated public share image lookup to refuse non-image snapshot rows before loading storage.
- Kept public image URLs share-scoped as `/share/{shareId}/images/{displayOrder}` and continued to avoid public exposure of storage keys, local paths, and original filenames.
- Added focused tests for extension mismatch, oversize rejection before storage, local path containment, broker-only upload access, public share image streaming without filename headers, raw storage path 404s, and non-image share image row refusal.

**Constraints honored:**

- Internal attachments remain reachable only through broker-authenticated create/update flows or share-scoped public image snapshots.
- Public share cards do not expose `private_memo`, `price_private_note`, `stakeholder.phone`, `contact_log.content`, `internal_risk_memo`, storage keys, local paths, or original filenames.
- No unrelated Phase 6 or new product features were started.
- No JPA, React, Vue, Next.js, or prohibited product automation features were introduced.

**Validation receipts:**

- Focused verification: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.file.LocalFileStorageTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest` passed.
- Full verification: `./gradlew test` passed.
- Manual QA: `./gradlew bootRun --args='--server.port=18102 --imjangbox.file-storage.local-root=/tmp/imjangbox-phase5-file-storage-qa'` started on the default `local` profile.
- Broker upload QA: authenticated create with CSRF and a PNG attachment returned `HTTP/1.1 302` to `/broker/inspections/41/edit`.
- Public share QA: authenticated share generation returned `HTTP/1.1 302` to `/share/60cc0025-9d4f-4af6-beb1-fa076b0bbf1e`; unauthenticated GET of that page rendered `PUBLIC_FILE_QA` and did not render `PRIVATE_` markers, `qa-photo.png`, or `inspection-files`.
- Public image QA: unauthenticated GET `/share/60cc0025-9d4f-4af6-beb1-fa076b0bbf1e/images/1` returned `HTTP/1.1 200`, `Content-Type: image/png`, and no `Content-Disposition` filename header.
- Raw storage route QA: unauthenticated GET `/inspection-files/41/qa-photo.png` returned `HTTP/1.1 404`.
- Cleanup QA: stopped `bootRun`; follow-up bounded curl to port `18102` failed to connect.

## 2026-06-11 - Phase 5 Operational Documentation

**Scope:** Complete the next unchecked Phase 5 hardening task: operational docs for deployment, configuration, and backup.

**Actions completed:**

- Added `docs/operations.md` covering current `local`, `test`, `mybatis-integration`, and `local-db` profile behavior.
- Documented required MySQL and broker secret configuration without committing real keys or real credentials.
- Documented separate Kakao REST API and Kakao JavaScript key configuration, with both integrations disabled by default.
- Documented local file-storage root configuration, public share image routing expectations, and file backup requirements.
- Documented Flyway/MySQL migration expectations, logical database backup/restore flow, small MVP server deployment checklist, smoke tests, and rollback steps.
- Updated `README.md` to point operators to the new guide and changed the `local-db` example to placeholder-only values.
- Updated `TASKS.md`, `CHECKPOINT.md`, and the implementation plan so the next unchecked Phase 5 task is full manual QA.

**Constraints honored:**

- No product code or unrelated Phase 6 features were changed.
- Current privacy boundaries remain documented: public share cards use snapshots, and public image routes stay share-scoped.
- No real secrets, Kakao keys, or realistic fake credentials were added.

**Validation receipts:**

- Full verification: `./gradlew test` passed.

## 2026-06-11 - Phase 5 Full Manual QA

**Scope:** Complete the next unchecked Phase 5 MVP task: full manual QA across inspection capture, map search coverage, and customer share-card views.

**Actions completed:**

- Started the local app on port `18103` with Kakao map rendering enabled using a harmless browser-key marker and isolated local file storage at `/tmp/imjangbox-phase5-manual-qa-files`.
- Verified authenticated broker form loading for CAFE inspections, CSRF rendering, facility template rendering, enabled Kakao map container output, and separation from the Kakao REST key.
- Created inspection `41` through the broker multipart form with public values, private marker values, contact-log content, CAFE facility answers, and a PNG attachment.
- Confirmed the broker edit page rendered saved internal values and selected facility answers.
- Generated a public share snapshot and verified unauthenticated public share rendering, customer-safe verification text, public facility values, share-scoped public image URL, image streaming, and raw storage-route denial.
- Updated the internal inspection after share creation and confirmed the original public share snapshot stayed stable and did not expose updated or private values.
- Verified unauthenticated broker access returns `401`, malformed business-type input renders the safe no-template state, and executable markup is not echoed.

**Constraints honored:**

- No product code was changed for this QA task.
- Public share cards continued to use snapshot rows rather than live internal records.
- Private memo, private price note, contact-log content, internal risk memo, internal address markers, original filename, and raw storage path text stayed out of public share HTML.
- No Phase 5 broker authentication, audit logging, mapper integration, file validation, or operations-doc work was duplicated.

**Validation receipts:**

- Manual QA: authenticated GET `/broker/inspections/new?businessType=CAFE` returned `HTTP/1.1 200`, rendered one `_csrf` token, CAFE facility labels, `data-kakao-map`, encoded browser-key marker text, and no REST-key marker.
- Manual QA: authenticated multipart POST `/broker/inspections` returned `HTTP/1.1 302` to `http://localhost:18103/broker/inspections/41/edit`; the edit page returned `HTTP/1.1 200` and rendered the saved public title, private memo, and selected `OK`/`NEEDS_CHECK` facility answers.
- Manual QA: authenticated POST `/broker/inspections/41/share` returned `HTTP/1.1 302` to `http://localhost:18103/share/9127e990-1c42-410e-a124-6c58f1671e15`.
- Manual QA: unauthenticated GET of the share URL returned `HTTP/1.1 200`, rendered `Manual QA Share Title`, `PUBLIC_MANUAL_QA_DISTRICT`, `Agent checked`, `급배수 확인`, and `/images/1`, with zero `PRIVATE_`, original filename, or `inspection-files` leaks.
- Manual QA: unauthenticated GET `/share/9127e990-1c42-410e-a124-6c58f1671e15/images/1` returned `HTTP/1.1 200`, `Content-Type: image/png`, and no `Content-Disposition` filename header.
- Manual QA: unauthenticated GET `/inspection-files/41/manual-qa.png` returned `HTTP/1.1 404`.
- Manual QA: after authenticated update of inspection `41`, the original share URL still rendered the original public title/address and had zero `UPDATED_` or `PRIVATE_` marker leaks.
- Manual QA: unauthenticated GET `/broker/inspections/new` returned `HTTP/1.1 401`; malformed business type returned `HTTP/1.1 200` with the no-template empty state and no executable script echo.
- Cleanup QA: stopped `bootRun`; bounded curl to port `18103` failed to connect.
- Focused regression verification: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest --tests com.imjangbox.map.KakaoMapViewFactoryTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.file.LocalFileStorageTest` passed.
- Full verification: `./gradlew test` passed.

## 2026-06-11 - Phase 5 Documentation Review

**Scope:** Complete the remaining Phase 5 documentation review task after product code landed.

**Actions completed:**

- Revisited `AGENTS.md`, `TASKS.md`, `WORK_LOG.md`, `CHECKPOINT.md`, `plans/`, `docs/`, `README.md`, `CHANGELOG.md`, and `SECURITY.md`.
- Updated current-state summaries to say Phase 0 through Phase 5 are complete.
- Removed stale wording that described customer share cards as a future/pending feature.
- Marked the final Phase 5 documentation review task complete in `TASKS.md`.
- Updated the checkpoint so the next step is to wait for an explicit release-preparation or Phase 6 request.
- Confirmed `docs/operations.md` and `docs/privacy-and-share-cards.md` already matched the completed share-card, file-storage, backup, and privacy boundaries.

**Constraints honored:**

- Documentation-only change; no product code was modified.
- No Phase 6 work was started.
- Public share snapshot, audit logging, mapper integration, file validation, and operations boundaries remain unchanged.

**Validation receipts:**

- `git diff --check` passed.
- Documentation validation command from `docs/VALIDATION.md` passed with `VALIDATION:PASS`.
- Stale-language scan found no remaining matches for Phase 4 pending/share-card planned/Phase 5 started wording.
- `./gradlew test` was not rerun because this was a documentation-only change.

## 2026-06-12 - v0.2.0-alpha Release Preparation

**Scope:** Prepare repository documentation for the next alpha release after completed Phase 0 through Phase 5 work.

**Actions completed:**

- Read `AGENTS.md`, `CHECKPOINT.md`, `TASKS.md`, `WORK_LOG.md`, `README.md`, `CHANGELOG.md`, `SECURITY.md`, `CONTRIBUTING.md`, `docs/`, and `plans/`.
- Inspected the current source/test/resource layout to confirm the completed Phase 0 through Phase 5 surface: broker inspection flows, facility templates, map/geocoding/search boundaries, public share snapshots, share-scoped images, audit logs, MyBatis integration tests, file validation, and operations docs.
- Confirmed local Git tags show `v0.1.0-alpha` already exists and current `main` contains Phase 4/5 commits afterward, so the next alpha candidate is `v0.2.0-alpha`.
- Updated `README.md` and `CHANGELOG.md` away from older `v0.1.0-alpha` in-progress wording.
- Created `docs/release-notes/v0.2.0-alpha.md` as the release notes draft.
- Checked local docs and `.github` files for issue status. No local GitHub issue export or open-issue ledger exists, and no network access was required for this release-preparation pass.
- Confirmed `SECURITY.md`, `CONTRIBUTING.md`, `docs/operations.md`, `docs/VALIDATION.md`, and `docs/privacy-and-share-cards.md` are consistent with the completed Phase 5 state.
- Updated `TASKS.md`, `CHECKPOINT.md`, `WORK_LOG.md`, and `CHANGELOG.md` to record the release-preparation state.

**Constraints honored:**

- No product code was modified.
- No Phase 6 product features were started.
- No OMO, LazyCodex, child-agent, subagent, or delegated-agent workflow was used.

**Validation receipts:**

- Documentation validation command from `docs/VALIDATION.md` passed with `VALIDATION:PASS`.
- `git diff --check` passed after removing README status-block trailing spaces.
- `./gradlew test` passed.

## 2026-06-12 - v0.2.0-alpha Published Release Documentation Sync

**Scope:** Synchronize repository documentation after the GitHub `v0.2.0-alpha` pre-release was published.

**Actions completed:**

- Updated README, CHANGELOG, and release notes wording from release-candidate draft state to published pre-release state.
- Recorded `v0.2.0-alpha` as the latest published pre-release and kept `v0.1.0-alpha` as the previous published alpha.
- Updated TASKS and CHECKPOINT so the next work area is GitHub issue cleanup and next product planning, not release publication.

**Constraints honored:**

- Documentation-only change; no product code was modified.
- No Phase 6 work was started.
- No OMO, LazyCodex, child-agent, subagent, or delegated-agent workflow was used.

**Validation receipts:**

- Documentation validation command from `docs/VALIDATION.md` passed with `VALIDATION:PASS`.
- `git diff --check` passed.
- `./gradlew test` passed.

## 2026-06-12 - GitHub Issue Cleanup Documentation Sync

**Scope:** Synchronize repository tracking docs after GitHub issue cleanup following the published `v0.2.0-alpha` pre-release.

**Actions completed:**

- Recorded Issue #1 Phase 4 share-card snapshot generation as closed and completed.
- Recorded Issue #2 public share-card privacy/security verification as closed and completed.
- Recorded Issue #4 mobile-first broker inspection form UX as still open and the next product-facing improvement candidate.
- Updated the next checkpoint toward mobile-first broker form UX planning.

**Constraints honored:**

- Documentation-only change; no product code was modified.
- No Phase 6 work was started.
- No release tags or published GitHub Release metadata were modified.
- No OMO, LazyCodex, child-agent, subagent, or delegated-agent workflow was used.

## 2026-06-12 - Issue #4 Mobile-First Broker Form UX Planning

**Scope:** Plan the mobile-first broker inspection form UX improvement without implementing product code.

**Actions completed:**

- Read `AGENTS.md`, `CHECKPOINT.md`, `TASKS.md`, `WORK_LOG.md`, `README.md`, `docs/`, and `plans/`.
- Checked local repository docs for Issue #4 context. The local context records Issue #4 as open for mobile-first broker inspection form UX planning; no separate issue export exists in repository docs.
- Inspected the current broker inspection create/edit form template, backing form object, controller, and MVC tests to identify the current fields, validation shape, facility behavior, map boundary, attachment input, CSRF behavior, and edit-only share-card action.
- Created `plans/2026-06-12-issue-4-mobile-first-broker-form-ux-plan.md` with the quick-save vs detailed follow-up field split, proposed mobile-first page structure, implementation steps, validation plan, acceptance criteria, and out-of-scope boundaries.
- Updated `TASKS.md`, `CHECKPOINT.md`, `docs/PROJECT_MEMORY.md`, and `docs/VALIDATION.md` to record the planning result and next checkpoint.

**Constraints honored:**

- Planning-only documentation change; no product code was modified.
- No Phase 6 work was started.
- No OMO, LazyCodex commands, child-agent, subagent, delegated-agent, or spawned-agent workflow was used.
- Existing privacy, CSRF, validation, attachment, facility, map, search-index, and share-card behavior was preserved in the plan.

**Validation receipts:**

- Documentation validation command from `docs/VALIDATION.md` passed with `VALIDATION:PASS` on 2026-06-12.
- `git diff --check` passed on 2026-06-12.
