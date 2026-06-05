# Database Migration Strategy

Schema work will use versioned SQL migrations and MyBatis mapper XML, not JPA-generated schema.

## Decision

- Use Flyway for MySQL schema migrations once Phase 1 introduces tables.
- Keep migration files under `src/main/resources/db/migration`.
- Keep MyBatis mapper XML under `src/main/resources/mappers`.
- Do not add JPA or automatic schema generation.

## Phase 0 Status

No migration dependency or schema file is added yet because the repository has no domain tables. The local database profile is defined with environment-variable placeholders so secrets stay outside source control.
