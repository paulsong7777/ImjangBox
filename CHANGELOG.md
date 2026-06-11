# Changelog

All notable changes to ImjangBox will be documented in this file.

ImjangBox is currently in an early MVP stage. The project is being developed in phases, with a focus on commercial-property inspection records, broker workflow clarity, privacy-safe public sharing, and AI-assisted development with Codex.

## v0.1.0-alpha - In Progress

### Added

* Spring Boot 3.5.x and Java 21 project foundation
* Thymeleaf and Bootstrap 5.3 based server-rendered UI
* MyBatis-based persistence structure
* Local development profiles
* Broker authentication foundation
* Commercial-property inspection ledger workflow
* Inspection create and edit flow
* Contact log append flow
* Attachment metadata and upload validation flow
* Facility template definitions for commercial-property inspection
* Customer-safe share card snapshot generation and public share pages
* Share-scoped public image streaming without storage-key or filename exposure
* Internal audit logging for share-card snapshot creation and regenerated versions
* SQL-backed MyBatis mapper integration tests using the `mybatis-integration` profile
* Operational deployment, configuration, backup, smoke-test, and rollback guide
* Project planning documents
* Task tracking documents
* Work log and checkpoint documents
* Security policy
* Contribution guide
* MIT license

### Security

* Added privacy boundary rules for customer-facing share pages
* Added security policy for vulnerability reporting
* Added project guidance for avoiding exposure of private broker data
* Added focus areas for attachment validation, share token handling, SQL injection, XSS, CSRF, and internal field leakage

### Documentation

* Added README setup and test instructions
* Added AGENTS.md for AI-assisted development context
* Added TASKS.md for phase-based task tracking
* Added WORK_LOG.md for implementation history
* Added CHECKPOINT.md for resuming development
* Added CONTRIBUTING.md for external contribution guidance
* Added SECURITY.md for security reporting

### Current Focus

* v0.1.0-alpha release preparation
* Public share page privacy regression coverage
* Mobile-first broker workflow improvement
* Screenshot and product documentation
