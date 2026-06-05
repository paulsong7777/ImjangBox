# CHECKPOINT

**Checkpoint date:** 2026-06-05
**Project:** imjangbox
**Current state:** Phase 1 privacy-first domain, public snapshot, schema, and mapper shapes exist and pass tests.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Start at `TASKS.md` Phase 2 unless the user gives a newer instruction.

## Exact Next Implementation Step

Begin Phase 2 broker inspection ledger work:

- build broker-facing create/edit screens with Thymeleaf and Bootstrap 5.3
- capture commercial property basics, pricing, area, condition, internal notes, and verification status
- keep contact logs and internal risk memo internal-only
- add controller/service/mapper tests around inspection create/update flows

## Do Not Start With

- Public share-card routes backed directly by internal records.
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
