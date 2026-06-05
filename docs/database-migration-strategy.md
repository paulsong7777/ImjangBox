# Database Migration Strategy

Schema work will use versioned SQL migrations and MyBatis mapper XML, not JPA-generated schema.

## Decision

- Use Flyway for MySQL schema migrations once Phase 1 introduces tables.
- Keep migration files under `src/main/resources/db/migration`.
- Keep MyBatis mapper XML under `src/main/resources/mappers`.
- Do not add JPA or automatic schema generation.

## Phase 1 Status

Flyway dependencies and the first migration now exist. `src/main/resources/db/migration/V1__create_property_inspection_privacy_tables.sql` defines internal inspection tables separately from `public_share_snapshots`, and `src/main/resources/mappers/PropertyInspectionMapper.xml` defines the initial MyBatis mapper shape.

The local database profile still uses environment-variable placeholders so secrets stay outside source control.
