# Security Policy

ImjangBox is an early-stage open-source project for building a commercial-property field inspection and proposal ledger for Korean commercial real estate brokers.

Because the project handles broker-side workflow data such as contact logs, attachments, private notes, internal price notes, and customer-facing share cards, security and privacy boundaries are important from the beginning.

## Reporting a Vulnerability

If you discover a security issue, please do not publicly disclose it in an issue.

Instead, report it by contacting:

[hello@scope-works.net](mailto:hello@scope-works.net)

Please include:

* a clear description of the vulnerability
* steps to reproduce the issue
* the affected page, feature, or file
* potential impact
* suggested fix, if available

## Security-Sensitive Areas

The following areas require special attention:

* authentication and authorization
* public share card data exposure
* private broker notes and contact log leakage
* attachment upload validation
* file path and storage key exposure
* SQL injection
* XSS in Thymeleaf templates
* CSRF protection
* customer-facing share token handling

## Current Status

ImjangBox is currently in an early MVP stage. Phase 5 hardening added broker-only internal routes, CSRF-protected broker forms, upload validation, share-scoped public image routes, internal share audit logs, SQL-backed mapper integration tests, and operational guidance. Security review should continue as the project moves toward release, and privacy-safe public sharing remains one of the core project goals.
