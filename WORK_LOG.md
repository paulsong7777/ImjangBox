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
