# CHECKPOINT

**Checkpoint date:** 2026-06-05
**Project:** imjangbox
**Current state:** Phase 0 Spring Boot foundation exists and passes tests.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Start at `TASKS.md` Phase 1 unless the user gives a newer instruction.

## Exact Next Implementation Step

Begin Phase 1 privacy-first domain and persistence design:

- model internal records separately from public share snapshots
- define verification status values
- keep internal and public addresses distinct
- draft MySQL/Flyway/MyBatis schema and mapper shapes
- add public projection tests proving denied fields cannot appear

## Do Not Start With

- Domain entities before the privacy boundary is planned in code.
- JPA.
- React, Vue, or Next.js.
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
