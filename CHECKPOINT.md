# CHECKPOINT

**Checkpoint date:** 2026-06-04
**Project:** imjangbox
**Current state:** Planning and memory initialization complete; product code has not been created.

## Resume Here Next Time

1. Read `AGENTS.md`.
2. Read `plans/2026-06-04-imjangbox-implementation-plan.md`.
3. Read `TASKS.md`.
4. Start at `TASKS.md` Phase 0 unless the user gives a newer instruction.

## Exact Next Implementation Step

Create the Spring Boot 3.x Gradle skeleton for Java 21 without adding domain behavior yet:

- `settings.gradle` or `settings.gradle.kts`
- root Gradle build file
- application entry point
- test configuration
- first context-load test
- README run/test commands

## Do Not Start With

- Domain entities before the privacy boundary is planned in code.
- JPA.
- React, Vue, or Next.js.
- Customer share cards backed directly by internal records.
- Automatic ad-copy generation.
- Automatic business-type legal judgment.
- Automatic commercial district scoring.
- External listing platform auto-upload.

## Verification To Run After Phase 0 Skeleton

```bash
./gradlew test
```

If the Gradle wrapper is not present after Phase 0, add it or document the local Gradle version required.
