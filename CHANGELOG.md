# Changelog

All notable changes to ImjangBox will be documented in this file.

ImjangBox is currently in an early MVP stage. The project is being developed in phases, with a focus on commercial-property inspection records, broker workflow clarity, privacy-safe public sharing, and AI-assisted development with Codex.

## v0.2.0-alpha - Released

Released: 2026-06-12

This released pre-release captures the completed Phase 0 through Phase 5 MVP state after the published `v0.1.0-alpha` tag.

### Added

- Customer-safe share-card snapshot generation and public share pages backed by persisted snapshot rows.
- Share-scoped public image streaming without storage-key, local-path, or original-filename exposure.
- Internal audit logging for share-card snapshot creation and regenerated versions.
- SQL-backed MyBatis mapper integration tests using the `mybatis-integration` profile.
- File upload validation for attachment count, size, allowed content type, extension/content-type consistency, and header/content-type consistency.
- Operational deployment, configuration, backup, smoke-test, and rollback guide.
- Full Phase 5 manual QA coverage across inspection capture, map UI/search-index coverage, and public share-card views.

### Security

- Public share routes render from snapshots rather than live internal inspection records.
- Public share pages continue to deny `private_memo`, `price_private_note`, `stakeholder.phone`, `contact_log.content`, `internal_risk_memo`, internal addresses, storage keys, local paths, and original filenames.
- Broker-only internal routes remain protected by HTTP Basic authentication and CSRF-protected forms.
- Public share image routes refuse non-image snapshot rows and expose only share-scoped URLs.

### Documentation

- Updated README status for the published `v0.2.0-alpha` pre-release.
- Added release notes for `v0.2.0-alpha`.
- Confirmed SECURITY, CONTRIBUTING, operations, validation, and privacy docs are consistent with the completed Phase 5 state.
- Recorded published release status in TASKS, WORK_LOG, CHECKPOINT, and this changelog.

### Validation

- Release validation is tracked in [docs/release-notes/v0.2.0-alpha.md](docs/release-notes/v0.2.0-alpha.md).

## v0.1.0-alpha - Released

Released: 2026-06-10

### Added

- Java 21 Spring Boot 3.5.x foundation with Gradle and GitHub Actions CI.
- Thymeleaf and Bootstrap 5.3 server-rendered UI foundation.
- Broker inspection create/edit flow with authentication, CSRF protection, validation, contact logs, and attachment metadata.
- Privacy-first internal/public model separation, verification status enum, Flyway migrations, and MyBatis mapper shape.
- Dynamic facility templates and independently persisted facility answers.
- `GeocodingGateway`, Kakao Maps browser UI boundary, and separate `property_search_index`.
- Project planning documents, security policy, contribution guide, changelog, and MIT license.
