# Contributing to ImjangBox

Thank you for your interest in contributing to ImjangBox.

ImjangBox is an early-stage open-source reference project for building a commercial-property field inspection and proposal ledger with Java, Spring Boot, Thymeleaf, MyBatis, and AI-assisted development.

The project focuses on practical workflows for Korean commercial real estate brokers, including inspection records, contact logs, facility checks, attachments, and privacy-safe customer share cards.

## Good First Contributions

Good first contributions include:

* improving documentation
* adding or improving tests
* improving mobile-first broker form UX
* reporting bugs
* suggesting commercial-property workflow improvements
* improving privacy and security checks
* reviewing public share card exposure rules

## Before You Start

Before submitting a change, please open an issue first so the scope can be discussed.

This project values small, focused changes over large, mixed pull requests.

## Development Setup

Please check the README for setup instructions.

Common commands:

```bash
./gradlew test
```

If you are working on Windows:

```bash
gradlew.bat test
```

## Contribution Rules

When contributing, please follow these rules:

1. Keep changes small and focused.
2. Add or update tests when changing behavior.
3. Do not expose private broker data in public share DTOs, templates, or APIs.
4. Do not commit secrets, credentials, API keys, or local configuration files.
5. Do not include real customer, broker, landlord, tenant, or property data.
6. Prefer clear naming and simple domain logic over clever abstractions.

## Security and Privacy Boundaries

The following data must never be exposed in customer-facing share pages:

* private broker notes
* internal price notes
* contact logs
* landlord contact information
* tenant contact information
* attachment storage keys
* internal file paths
* negotiation notes
* commission notes

If your contribution touches public share cards, please add tests to verify that private fields are not exposed.

## Pull Request Checklist

Before opening a pull request:

* [ ] I opened or referenced an issue.
* [ ] I kept the change focused.
* [ ] I ran the test suite.
* [ ] I updated documentation if needed.
* [ ] I checked that no private broker data is exposed.
* [ ] I did not commit secrets or real personal data.

## Project Status

ImjangBox is currently in an early MVP stage. Contributions that improve documentation, tests, security boundaries, and broker workflow clarity are especially welcome.
