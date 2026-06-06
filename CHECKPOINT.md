# CHECKPOINT

**Checkpoint date:** 2026-06-06
**Project:** imjangbox
**Current state:** Phase 2 broker inspection ledger create/edit flow exists. A Phase 2 hardening pass retained local-profile contact logs and attachment metadata, clarified contact logs as append-only writes, and made local file storage configurable through application properties.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Start at `TASKS.md` Phase 3 only after confirming no newer Phase 2 hardening request is pending.

## Exact Next Implementation Step

Begin Phase 3 facility templates, maps, geocoding, and search-index work:

- design data-driven facility-check templates by business type
- store facility answers independently from template definitions
- implement `GeocodingGateway` boundaries before map/geocoding logic
- keep search based on a separate `search_index` structure, not internal notes

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
