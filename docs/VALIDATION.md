# Validation Notes

## Initialization Validation

This repository initialization is documentation-only. No Java product code exists yet, so no Gradle, JUnit, LSP, or runtime application validation is currently possible.

## Automated File/Content Check

Use this shell check after planning document edits:

```bash
bash -lc 'set -euo pipefail
required=(AGENTS.md TASKS.md WORK_LOG.md CHECKPOINT.md plans/2026-06-04-imjangbox-implementation-plan.md plans/0001-product-architecture-plan.md docs/PROJECT_MEMORY.md docs/DOMAIN_RULES.md docs/VALIDATION.md docs/INIT_NOTEPAD.md docs/domain-overview.md docs/privacy-and-share-cards.md docs/architecture-constraints.md docs/integrations.md docs/search-index.md)
for f in "${required[@]}"; do test -f "$f" || { echo "MISSING:$f"; exit 1; }; done
grep -q "Phase 0" TASKS.md
grep -q "Phase 5" TASKS.md
grep -q "private_memo" AGENTS.md
grep -q "UNVERIFIED" plans/2026-06-04-imjangbox-implementation-plan.md
grep -q "Do not modify product code" WORK_LOG.md'
```

## Manual QA Surface

For this planning-only initialization, the manual QA surface is the repository filesystem. A valid manual check is a terminal or tmux transcript showing:

- required files exist;
- `TASKS.md` includes Phase 0 through Phase 5;
- no Java product files were created;
- privacy rules are present in the planning documents.

## Future Validation Once Product Code Exists

- Run `./gradlew test`.
- Run MVC tests for internal broker flows and public share-card routes.
- Run template rendering tests proving denied fields are absent.
- Run browser QA for broker inspection capture and customer share-card pages.
