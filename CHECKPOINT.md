# CHECKPOINT

**Checkpoint date:** 2026-06-12
**Project:** imjangbox
**Current state:** Phase 0 through Phase 5 are complete. Release preparation for the next alpha is complete in repository docs as `v0.2.0-alpha` because `v0.1.0-alpha` is already tagged and Phase 4/5 work landed afterward. Broker authentication was completed early, share-card snapshot creation writes internal audit records, MyBatis mapper behavior has SQL-backed integration coverage through a deterministic H2 MySQL-mode test profile, file-storage validation enforces attachment count, size, allowed content type, filename extension/content-type consistency, and header/content-type consistency before storage, operational deployment/configuration/backup docs exist in `docs/operations.md`, and full manual QA across inspection capture, map UI/search-index coverage, and share-card views passed on 2026-06-11. Public share images remain share-scoped, image-only streams; raw storage keys, local paths, and original filenames are not public routes or public template output. The `v0.2.0-alpha` release notes draft exists at `docs/release-notes/v0.2.0-alpha.md`.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Read `docs/release-notes/v0.2.0-alpha.md`.
5. Do not start Phase 6 unless explicitly requested.

## Exact Next Implementation Step

Next checkpoint is final release publication work only if requested: review `docs/release-notes/v0.2.0-alpha.md`, confirm live GitHub issue/release status if needed, tag/publish `v0.2.0-alpha`, and do not start Phase 6 product features unless explicitly requested.

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

- Documentation validation command from `docs/VALIDATION.md` passed with `VALIDATION:PASS` on 2026-06-12.
- `git diff --check` passed on 2026-06-12 after release-preparation cleanup.
- `./gradlew test` passed on 2026-06-12 after release-preparation documentation updates.
- Release-preparation docs inspection on 2026-06-12 confirmed SECURITY, CONTRIBUTING, operations, validation, and privacy docs match the completed Phase 5 state. Local-only GitHub issue status check found no issue export or open-issue ledger in repository docs or `.github`; no network verification was required.
- `git diff --check` passed on 2026-06-11 after the Phase 5 documentation review. No Gradle test run was needed because only documentation files changed.
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
