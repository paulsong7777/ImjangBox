# CHECKPOINT

**Checkpoint date:** 2026-06-11
**Project:** imjangbox
**Current state:** Phase 5 hardening is in progress. Broker authentication was already completed early, share-card snapshot creation writes internal audit records, MyBatis mapper behavior has SQL-backed integration coverage through a deterministic H2 MySQL-mode test profile, file-storage validation now enforces attachment count, size, allowed content type, filename extension/content-type consistency, and header/content-type consistency before storage, operational deployment/configuration/backup docs exist in `docs/operations.md`, and full manual QA across inspection capture, map UI/search-index coverage, and share-card views passed on 2026-06-11. Public share images remain share-scoped, image-only streams; raw storage keys, local paths, and original filenames are not public routes or public template output.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Start the next unchecked Phase 5 hardening task only when explicitly asked.

## Exact Next Implementation Step

Continue Phase 5 hardening work:

- revisit `AGENTS.md`, `TASKS.md`, `WORK_LOG.md`, `CHECKPOINT.md`, `plans/`, and `docs/` after first product code lands
- keep Phase 4 snapshot privacy guarantees, Phase 5 share audit logging, and SQL-backed mapper integration tests intact

Phase 2 notes to preserve:

- Default `local` profile uses an in-memory mapper and HTTP Basic broker credentials `broker` / `broker-password`.
- Broker create form is `/broker/inspections/new`.
- Contact-log writes are append-only unless `deleteContactLogs` is explicitly called.
- Local attachment storage root is `imjangbox.file-storage.local-root`, defaulting to `${java.io.tmpdir}/imjangbox-local-files`.
- Operational docs for the current profiles, required secrets, Kakao key separation, file/database backups, small-server deployment, smoke tests, and rollback are in `docs/operations.md`.

## Do Not Start With

- Public share-card routes backed directly by internal records.
- JPA.
- React, Vue, or Next.js.
- Hard-coded facility template items in Java or Thymeleaf.
- Facility answers stored inside template-definition rows.
- Geocoding logic embedded directly into inspection controllers or Thymeleaf templates.
- Kakao Maps JavaScript keys mixed with backend Kakao REST API keys.
- Search scans over private internal notes.
- Customer share cards backed directly by internal records.
- Mutating existing public share snapshots when internal records change.
- Duplicating Phase 5 broker authentication work that was completed early.
- Automatic ad-copy generation.
- Automatic business-type legal judgment.
- Automatic commercial district scoring.
- External listing platform auto-upload.

## Verification To Run

```bash
./gradlew test
```

The repository includes a Gradle wrapper. In this `/mnt/c` workspace, Gradle writes generated output to `/tmp/imjangbox-build` by default to avoid Windows-mount chmod failures.

Most recent verification:

- `./gradlew test` passed on 2026-06-11 after the operational documentation update.
- `./gradlew test --tests com.imjangbox.inspection.persistence.MyBatisPersistenceIntegrationTest` passed on 2026-06-11.
- `./gradlew test --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.inspection.persistence.PersistencePrivacyShapeTest` passed on 2026-06-11.
- `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.file.LocalFileStorageTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest` passed on 2026-06-11.
- `./gradlew test` passed on 2026-06-11.
- Manual QA on port `18101` previously created an inspection, generated two broker share snapshots for the same inspection, and confirmed both public pages rendered public values without private marker leaks.
- Manual QA on port `18102` created an inspection with a PNG attachment, generated a public share snapshot, confirmed unauthenticated `/share/{shareId}` rendered `PUBLIC_FILE_QA` without `PRIVATE_` markers, original filename, or `inspection-files` text, confirmed `/share/{shareId}/images/1` returned `Content-Type: image/png`, and confirmed `/inspection-files/41/qa-photo.png` returned `404`.
- Manual QA on port `18103` passed on 2026-06-11 with Kakao map rendering enabled using a harmless browser-key marker and isolated local file storage. Authenticated broker form GET rendered one CSRF token, CAFE facility templates, and an enabled Kakao map container without REST-key leakage. Authenticated create with facility answers, private markers, contact log, and PNG attachment redirected to inspection `41`; edit rendered saved internal values and selected facility answers. Authenticated share generation redirected to `/share/9127e990-1c42-410e-a124-6c58f1671e15`; unauthenticated public GET rendered public title/address/verification/facility/image URL with zero private marker, original filename, or `inspection-files` leaks. Public `/images/1` returned `Content-Type: image/png` and no filename disposition; raw `/inspection-files/41/manual-qa.png` returned `404`. Updating the internal inspection afterward left the original share snapshot unchanged and free of updated/private markers. Unauthenticated broker form access returned `401`; malformed business type rendered the safe no-template state without executable markup.
- Focused manual-QA regression command passed on 2026-06-11: `./gradlew test --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.persistence.LocalInspectionLedgerMapperTest --tests com.imjangbox.map.KakaoMapViewFactoryTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.share.ShareSnapshotServiceTest --tests com.imjangbox.file.LocalFileStorageTest`.
- `./gradlew test` passed on 2026-06-11 after the full manual QA pass.
