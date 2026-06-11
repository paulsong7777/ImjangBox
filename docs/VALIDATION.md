# Validation Notes

## Initialization Validation

The initial repository setup was documentation-only. Product code now exists, so use Gradle/JUnit and runtime smoke checks for implementation validation.

## Automated File/Content Check

Use this shell check after planning document edits:

```bash
bash -lc 'set -euo pipefail
required=(AGENTS.md TASKS.md WORK_LOG.md CHECKPOINT.md README.md CHANGELOG.md SECURITY.md plans/2026-06-04-imjangbox-implementation-plan.md plans/0001-product-architecture-plan.md docs/PROJECT_MEMORY.md docs/DOMAIN_RULES.md docs/VALIDATION.md docs/INIT_NOTEPAD.md docs/domain-overview.md docs/privacy-and-share-cards.md docs/architecture-constraints.md docs/integrations.md docs/search-index.md docs/operations.md docs/database-migration-strategy.md)
for f in "${required[@]}"; do test -f "$f" || { echo "MISSING:$f"; exit 1; }; done
grep -q "Phase 0" TASKS.md
grep -q "Phase 5" TASKS.md
grep -q "private_memo" AGENTS.md
grep -q "UNVERIFIED" plans/2026-06-04-imjangbox-implementation-plan.md
grep -q "Do not modify product code" WORK_LOG.md'
```

## Manual QA Surface

Product code exists. Use the running Spring Boot application as the main manual QA surface for broker and public web flows. Documentation-only filesystem checks remain useful only for project-memory updates.

## General Product Validation

- Run `./gradlew test`.
- Run MVC tests for internal broker flows and public share-card routes.
- Run template rendering tests proving denied fields are absent.
- Run browser QA for broker inspection capture and customer share-card pages.

## Phase 1 Validation

- `./gradlew test`
- `./gradlew clean test --rerun-tasks`
- `./gradlew test --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest`
- JShell projection probe against `/tmp/imjangbox-build/classes/java/main` must print a `PublicShareSnapshot[...]` and `PASS` after checking sample private values.
- Runtime smoke QA: `./gradlew bootRun --args='--server.port=18082'`, `curl -i http://localhost:18082/`, and `curl -i http://localhost:18082/not-a-route`.

## Phase 2 Validation

- `./gradlew test`
- Focused broker flow tests: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest`
- Runtime broker form QA: `./gradlew bootRun --args='--server.port=18083'`
- Manual HTTP QA:
  - unauthenticated `curl -i http://localhost:18083/broker/inspections/new` should return `401`
  - authenticated `curl -i -u broker:broker-password http://localhost:18083/broker/inspections/new` should return `200`
  - authenticated form GET should render one `_csrf` token
  - authenticated multipart POST invalid input with `_csrf` to `/broker/inspections` should return the form with `입력값을 확인해 주세요`
  - authenticated multipart POST valid create with `_csrf` to `/broker/inspections` should redirect to `/broker/inspections/{id}/edit`
  - authenticated multipart POST valid update with `_csrf` to `/broker/inspections/{id}` should redirect and the edit page should show updated values

## Phase 3 Facility Template Validation

- `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest`
- `./gradlew clean test --rerun-tasks`
- Runtime broker form QA: `./gradlew bootRun --args='--server.port=18092'`
- Manual HTTP QA:
  - authenticated `curl -i -u broker:broker-password 'http://localhost:18092/broker/inspections/new?businessType=CAFE'` should return `200` and render CAFE facility template labels and `facilityAnswers[...]` fields
  - authenticated `curl -i -u broker:broker-password 'http://localhost:18092/broker/inspections/new?businessType=RESTAURANT'` should return `200` and render RESTAURANT facility template labels
  - authenticated malformed business type should return `200` with the no-template empty state
  - authenticated create with `_csrf` and `facilityAnswers[0].answer=OK` should redirect to edit, and the edit page should render the answer as selected

## Phase 3 GeocodingGateway Validation

- `./gradlew test --tests 'com.imjangbox.map.*'`
- `./gradlew test`
- `./gradlew clean test --rerun-tasks`
- LSP diagnostics should be run when `jdtls` is installed; otherwise record `jdtls` as unavailable and rely on Gradle clean compile/test.
- Runtime smoke QA: `./gradlew bootRun --args='--server.port=18094'` and `curl -i --max-time 5 http://localhost:18094/` should return `HTTP/1.1 200`.
- Gateway API QA: JShell with `/tmp/imjangbox-build/classes/java/main` should show `DisabledGeocodingGateway` returning `UNAVAILABLE` for a normal address and `INVALID_ADDRESS` for blank input.
- Cleanup QA: stop `bootRun`, then a bounded curl to port `18094` should fail to connect.

## Phase 3 Kakao Maps UI Validation

- Baseline MVC check: `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest`
- Focused map/UI checks: `./gradlew test --tests com.imjangbox.map.KakaoMapViewFactoryTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest`
- Full verification: `./gradlew test`
- Final clean verification: `./gradlew clean test --rerun-tasks`
- LSP diagnostics should be run when `jdtls` is installed; otherwise record `jdtls` as unavailable and rely on Gradle compile/test.
- Enabled runtime QA: start with `KAKAO_MAP_JAVASCRIPT_KEY='browser key+only' ./gradlew bootRun --args='--server.port=18095 --imjangbox.maps.kakao.enabled=true'`; authenticated GET `/broker/inspections/new?businessType=CAFE` should render `data-kakao-map`, an encoded `data-kakao-sdk-src`, default coordinates, and no REST API key text.
- Malformed input QA: authenticated GET with `businessType=%3Cscript%3Ealert(1)%3C%2Fscript%3E` should return `HTTP/1.1 200`, render the no-template empty state, and not echo executable markup.
- Disabled runtime QA: start with `./gradlew bootRun --args='--server.port=18096'`; authenticated GET `/broker/inspections/new` should render the disabled map status and no Kakao SDK URL.
- Cleanup QA: stop both `bootRun` sessions, then bounded curls to ports `18095` and `18096` should fail to connect.

## Phase 3 Search Index Validation

- Baseline persistence check: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest`
- Focused search-index checks: same command should prove service refreshes `property_search_index`, local storage keeps it separate, and V4/MyBatis shapes exclude private fields.
- Full verification: `./gradlew test`
- Final clean verification: `./gradlew clean test --rerun-tasks`
- LSP diagnostics should be run when `jdtls` is installed; otherwise record `jdtls` as unavailable and rely on Gradle compile/test.
- Runtime broker QA: `./gradlew bootRun --args='--server.port=18097'`; authenticated create and update requests with private leak markers should return redirects and keep the broker edit flow working.
- Cleanup QA: stop `bootRun`, then a bounded curl to port `18097` should fail to connect.

## Phase 4 Share Card Validation

- Full verification: `./gradlew test`.
- Focused share checks: `./gradlew test --tests 'com.imjangbox.share.*' --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest`.
- Runtime QA: `./gradlew bootRun` on the default local profile.
- Manual HTTP QA:
  - authenticated GET `/broker/inspections/new` should return `200` and a CSRF token
  - authenticated POST `/broker/inspections` with public and private marker values should redirect to `/broker/inspections/{id}/edit`
  - authenticated POST `/broker/inspections/{id}/share` with CSRF should redirect to `/share/{shareId}`
  - unauthenticated GET `/share/{shareId}` should return `200` and render only public title, public address, public pricing, customer-safe verification text, and customer-visible facilities
  - the public page should not render private memo, private price note, stakeholder phone, contact-log content, internal address, internal risk memo, storage key, original attachment filename, or search-index-only fields
  - after updating the internal inspection, the same `/share/{shareId}` page should still render the original snapshot values
- Cleanup QA: stop `bootRun`, then verify no `bootRun`/`ImjangboxApplication` process remains.

## Phase 5 Share Audit Validation

- Focused audit/share checks: `./gradlew test --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest`.
- Full verification: `./gradlew test`.
- Runtime QA: start `./gradlew bootRun --args='--server.port=18101'` on the default local profile.
- Manual HTTP QA:
  - authenticated GET `/broker/inspections/new?businessType=CAFE` should return `200` and a CSRF token
  - authenticated POST `/broker/inspections` with private marker values should redirect to the broker edit page
  - authenticated POST `/broker/inspections/{inspectionId}/share` twice should redirect to two `/share/{shareId}` URLs
  - unauthenticated GET of both share URLs should return `200`, render public title/address/verification values, and exclude private marker values
  - focused service tests should prove the first snapshot audit action is `CREATED` and the second generated snapshot for the same inspection is `UPDATED`

## Phase 5 MyBatis Integration Validation

- Focused mapper integration check: `./gradlew test --tests com.imjangbox.inspection.persistence.MyBatisPersistenceIntegrationTest`.
- Privacy/share regression check: `./gradlew test --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest`.
- Full verification: `./gradlew test`.
- The mapper integration profile is `mybatis-integration`; it uses H2 in MySQL compatibility mode with test-only schema migration resources under `src/test/resources/db/mybatis-integration`.
- Keep production MySQL migration shape coverage in `PersistencePrivacyShapeTest`, because the integration profile intentionally uses a local-friendly final schema rather than requiring Docker or an external MySQL server.

## Phase 5 File Storage Validation

- Focused file/share checks: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.file.LocalFileStorageTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest`.
- Full verification: `./gradlew test`.
- Runtime QA: start `./gradlew bootRun --args='--server.port=18102 --imjangbox.file-storage.local-root=/tmp/imjangbox-phase5-file-storage-qa'` on the default local profile.
- Manual HTTP QA:
  - authenticated GET `/broker/inspections/new` should return `200` and a CSRF token
  - authenticated multipart POST `/broker/inspections` with a valid `.png` file, `Content-Type: image/png`, and PNG header bytes should redirect to `/broker/inspections/{id}/edit`
  - unsupported content types, oversized files, extension/type mismatches, and header/type mismatches should be rejected before storage in focused tests
  - authenticated POST `/broker/inspections/{id}/share` should redirect to `/share/{shareId}`
  - unauthenticated GET `/share/{shareId}` should render public values and should not render private markers, storage keys, `inspection-files`, local paths, or original filenames
  - unauthenticated GET `/share/{shareId}/images/1` should return `200` with an image content type and no original-filename `Content-Disposition` header
  - unauthenticated GET of a raw storage-like route such as `/inspection-files/{id}/file.png` should return `404`
  - stop `bootRun`, then a bounded curl to port `18102` should fail to connect

## Phase 5 Full Manual QA Validation

- Runtime QA: start with `KAKAO_MAP_JAVASCRIPT_KEY='manual qa browser key' ./gradlew bootRun --args='--server.port=18103 --imjangbox.maps.kakao.enabled=true --imjangbox.file-storage.local-root=/tmp/imjangbox-phase5-manual-qa-files'`.
- Authenticated GET `/broker/inspections/new?businessType=CAFE` should return `200`, render exactly one CSRF token, render CAFE facility labels, render the enabled Kakao map container and encoded browser-key marker, and not render a Kakao REST-key marker.
- Authenticated multipart POST `/broker/inspections` with public values, private marker values, contact-log content, customer-visible CAFE facility answers, and a valid PNG should redirect to `/broker/inspections/{inspectionId}/edit`.
- The broker edit page should render saved internal values and selected facility answers.
- Authenticated POST `/broker/inspections/{inspectionId}/share` should redirect to `/share/{shareId}`.
- Unauthenticated GET `/share/{shareId}` should return `200` and render only public title, public address, customer-safe verification text, public facility values, and share-scoped image URLs.
- The public share page should not render private markers, original filenames, raw storage keys, `inspection-files`, internal address markers, contact-log content, private price notes, or internal risk values.
- Unauthenticated GET `/share/{shareId}/images/1` should return `200`, an image content type, and no original-filename `Content-Disposition` header.
- Unauthenticated GET of a raw storage-like route such as `/inspection-files/{inspectionId}/file.png` should return `404`.
- After updating the internal inspection, the original `/share/{shareId}` should still render the original snapshot values and should not render updated/private markers.
- Unauthenticated GET `/broker/inspections/new` should return `401`.
- Authenticated GET with malformed `businessType` should return `200`, render the no-template empty state, and not echo executable markup.
- Focused regression command: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest --tests com.imjangbox.map.KakaoMapViewFactoryTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.file.LocalFileStorageTest`.
- Full verification: `./gradlew test`.
- Cleanup QA: stop `bootRun`, then a bounded curl to port `18103` should fail to connect.
