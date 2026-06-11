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

## Phase 5 Integration Test Approach

Production and `local-db` still use MySQL with the versioned Flyway migrations in `src/main/resources/db/migration`.

Mapper integration tests use a dedicated `mybatis-integration` profile with H2 in MySQL compatibility mode and a test-only final-schema migration under `src/test/resources/db/mybatis-integration`. This keeps `./gradlew test` deterministic on local machines without Docker or an external MySQL service while still executing the real MyBatis mapper XML against SQL tables, foreign keys, generated IDs, child rows, and search-index upserts.

Production migration text remains covered by persistence shape tests because some valid MySQL DDL forms, such as multi-column `ALTER TABLE ... ADD COLUMN`, are not accepted by H2.
