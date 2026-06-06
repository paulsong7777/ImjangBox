# CHECKPOINT

**Checkpoint date:** 2026-06-06
**Project:** imjangbox
**Current state:** Phase 3 is complete. Dynamic facility-check template definitions now exist by business type, with MySQL/Flyway template tables, a MyBatis mapper/service boundary, local-profile configuration-backed seed templates, broker-form rendering for selected business types, independently persisted facility answers, an explicit `GeocodingGateway` boundary with a disabled local default and Kakao REST adapter, a broker-form Kakao Maps UI boundary configured by a browser JavaScript key, and a separate `property_search_index` structure refreshed from broker-safe fields on inspection create/update.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Start the next unchecked Phase 4 customer share-card task.

## Exact Next Implementation Step

Start Phase 4 customer share-card work:

- generate customer-safe share snapshots from internal inspection records
- keep share cards backed by snapshots, not direct internal entities

Phase 2 notes to preserve:

- Default `local` profile uses an in-memory mapper and HTTP Basic broker credentials `broker` / `broker-password`.
- Broker create form is `/broker/inspections/new`.
- Contact-log writes are append-only unless `deleteContactLogs` is explicitly called.
- Local attachment storage root is `imjangbox.file-storage.local-root`, defaulting to `${java.io.tmpdir}/imjangbox-local-files`.

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
- Automatic ad-copy generation.
- Automatic business-type legal judgment.
- Automatic commercial district scoring.
- External listing platform auto-upload.

## Verification To Run

```bash
./gradlew test
```

The repository includes a Gradle wrapper. In this `/mnt/c` workspace, Gradle writes generated output to `/tmp/imjangbox-build` by default to avoid Windows-mount chmod failures.
