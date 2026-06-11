# CHECKPOINT

**Checkpoint date:** 2026-06-11
**Project:** imjangbox
**Current state:** Phase 5 hardening has started. Broker authentication was already completed early, and share-card snapshot creation now writes internal audit records. The first generated snapshot for an inspection records `CREATED`; later broker-generated snapshot versions for the same inspection record `UPDATED`. Public share cards still render only stored snapshot rows, with separate public snapshot rows for the card, customer-visible facility answers, and share-scoped public image metadata.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Start the next unchecked Phase 5 hardening task only when explicitly asked.

## Exact Next Implementation Step

Continue Phase 5 hardening work:

- add integration tests for MyBatis/MySQL behavior using the chosen test database approach
- keep Phase 4 snapshot privacy guarantees and Phase 5 share audit logging intact

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

- `./gradlew test` passed on 2026-06-11.
- Manual QA on port `18101` created an inspection, generated two broker share snapshots for the same inspection, and confirmed both public pages rendered public values without private marker leaks.
