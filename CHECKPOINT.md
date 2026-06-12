# CHECKPOINT

**Checkpoint date:** 2026-06-12
**Project:** imjangbox
**Current state:** Phase 0 through Phase 5 are complete, `v0.2.0-alpha` is published as the latest GitHub pre-release, and post-publication GitHub issue cleanup is complete. Issues #1 Phase 4 share-card snapshot generation, #2 public share-card privacy/security verification, and #4 mobile-first broker inspection form UX are closed as completed. The product core has been re-centered from inspection-form-first to property map/list management first: `/broker` now redirects to `/broker/inspections`, `/broker/inspections` is the broker's core `내 상가 매물` dashboard with map placeholder, property cards, empty state, and registration/share actions, and the home page points to property management and registration. The create/edit form remains server-rendered Thymeleaf/Bootstrap and keeps existing routes, field names, validation, CSRF, attachments, facility answers, Kakao map boundary, contact logs, and customer share POST behavior intact. The public share card now reads as a customer proposal template backed only by public snapshot data. Broker authentication was completed early, share-card snapshot creation writes internal audit records, MyBatis mapper behavior has SQL-backed integration coverage through a deterministic H2 MySQL-mode test profile, file-storage validation enforces attachment count, size, allowed content type, filename extension/content-type consistency, and header/content-type consistency before storage, operational deployment/configuration/backup docs exist in `docs/operations.md`, and full manual QA across inspection capture, map UI/search-index coverage, share-card views, Issue #4, the product UX elevation pass, and the product core reset has passed. Public share images remain share-scoped, image-only streams; raw storage keys, local paths, and original filenames are not public routes or public template output. The `v0.2.0-alpha` release notes are at `docs/release-notes/v0.2.0-alpha.md`.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `plans/2026-06-12-issue-4-mobile-first-broker-form-ux-plan.md`.
4. Read `TASKS.md`.
5. Read `docs/release-notes/v0.2.0-alpha.md`.
6. Do not start Phase 6 unless explicitly requested.

## Exact Next Step

Next checkpoint is an explicit next-product planning request after reviewing the property management dashboard reset:

- Wait for an explicit next-product planning or implementation request.
- Do not start Phase 6 product features unless explicitly requested.

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

- `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest` passed on 2026-06-12 after the Issue #4 broker form UX restructuring.
- Product Core Reset validation passed on 2026-06-13 KST: focused home, broker dashboard, public share/privacy, local mapper, and MyBatis integration tests passed; full `./gradlew test` passed; docs validation passed with `VALIDATION:PASS`; `git diff --check` passed; manual QA on port `18108` verified home, dashboard empty state, authenticated create with valid PNG, list/card rendering after create, edit page, share-card generation, public proposal card privacy, and mobile-width readability.
- `./gradlew test --tests com.imjangbox.web.HomeControllerTest --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest` passed on 2026-06-12 after the product UX elevation pass.
- `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.inspection.InspectionServiceTest --tests com.imjangbox.file.LocalFileStorageTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.share.PublicShareSnapshotPrivacyTest --tests com.imjangbox.share.ShareSnapshotServiceTest` passed on 2026-06-12 after the product UX elevation pass.
- `./gradlew test` passed on 2026-06-12 after the product UX elevation pass.
- Manual product UX QA passed on 2026-06-12 at `http://localhost:8080/` and `/broker/inspections/new`: home rendered Korean product overview; broker form rendered Korean product copy with preserved field bindings and disabled map state; invalid create preserved facility answer selection; valid PNG create redirected to edit; edit kept save/update separate from customer share generation; generated public share page kept private markers, internal address marker, contact-log content, raw storage path text, and original filename out of public output.
- `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest --tests com.imjangbox.map.KakaoMapViewFactoryTest --tests com.imjangbox.file.LocalFileStorageTest --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.share.ShareSnapshotServiceTest` passed on 2026-06-12 after the Issue #4 broker form UX restructuring.
- `./gradlew test` passed on 2026-06-12 after the Issue #4 broker form UX restructuring.
- Issue #4 mobile-first broker inspection form UX was pushed to `main`, verified by CI, and closed as completed on 2026-06-12. Post-release GitHub issues #1, #2, and #4 are now all closed.
- Manual Issue #4 mobile-width QA passed on 2026-06-12. Disabled-map run on port `18104` confirmed unauthenticated broker form access returned `401`, authenticated create rendered one CSRF token and quick-save/mobile layout first, invalid create preserved facility answer selection, valid create with a PNG attachment redirected to edit, edit rendered saved values and the separate share action, and the generated public share page excluded private marker values, internal address marker, contact-log content, storage path text, and original filename. Enabled-map run on port `18105` confirmed `data-kakao-map`, encoded browser SDK URL, default coordinates, and no REST-key marker text.
- Pre-commit Issue #4 validation rerun passed on 2026-06-12, including focused broker controller tests, focused broker/map/file/share tests, full `./gradlew test`, disabled-map manual mobile QA on port `18106`, enabled-map manual QA on port `18107`, documentation validation, and `git diff --check`.
- Documentation validation command from `docs/VALIDATION.md` passed with `VALIDATION:PASS` on 2026-06-12 after the Issue #4 broker form UX implementation.
- `git diff --check` passed on 2026-06-12 after the Issue #4 broker form UX implementation.
- Documentation validation command from `docs/VALIDATION.md` passed with `VALIDATION:PASS` on 2026-06-12 after the published-release documentation sync.
- `git diff --check` passed on 2026-06-12 after the published-release documentation sync.
- `./gradlew test` passed on 2026-06-12 after the published-release documentation sync.
- GitHub issue cleanup after `v0.2.0-alpha` publication completed on 2026-06-12: Issues #1 and #2 are closed, and Issue #4 remains open as the next mobile-first broker form UX planning checkpoint.
- Documentation validation command from `docs/VALIDATION.md` passed with `VALIDATION:PASS` on 2026-06-12.
- `git diff --check` passed on 2026-06-12 after release-preparation cleanup.
- `./gradlew test` passed on 2026-06-12 after release-preparation documentation updates.
- Release-preparation docs inspection on 2026-06-12 confirmed SECURITY, CONTRIBUTING, operations, validation, and privacy docs match the completed Phase 5 state. Release publication is complete, and the next checkpoint is GitHub issue cleanup.
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
