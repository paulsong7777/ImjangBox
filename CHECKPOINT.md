# CHECKPOINT

**Checkpoint date:** 2026-06-11
**Project:** imjangbox
**Current state:** Phase 4 is complete. Customer share cards are generated as stored snapshots from internal inspection records, with separate public snapshot rows for the card, customer-visible facility answers, and share-scoped public image metadata. Broker edit pages can generate a new share link, public `/share/{shareId}` pages render from snapshots with Thymeleaf/Bootstrap, and regression tests prove denied internal fields stay out of DTOs, JSON, templates, snapshot mapper shapes, and stable public output after internal record updates.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Start the next unchecked Phase 5 hardening task only when explicitly asked.

## Exact Next Implementation Step

Start Phase 5 hardening work:

- add audit logging for share-card creation and updates
- keep Phase 4 snapshot privacy guarantees intact

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
- Automatic ad-copy generation.
- Automatic business-type legal judgment.
- Automatic commercial district scoring.
- External listing platform auto-upload.

## Verification To Run

```bash
./gradlew test
```

The repository includes a Gradle wrapper. In this `/mnt/c` workspace, Gradle writes generated output to `/tmp/imjangbox-build` by default to avoid Windows-mount chmod failures.
