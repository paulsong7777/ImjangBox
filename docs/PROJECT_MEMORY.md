# Project Memory

## Name

imjangbox

## Purpose

imjangbox is a commercial-property field inspection and proposal ledger for Korean commercial real estate brokers. It supports on-site property capture, business-fit organization, facility-condition tracking, premium/key-money details, verification status, and safe customer proposal cards.

## Primary Users

- Commercial real estate brokers recording field inspections.
- Broker teams reviewing internal notes, verification status, and proposal readiness.
- Customers receiving sanitized share cards.

## Product Boundary

The system is a broker workflow and proposal ledger. It is not a listing-platform uploader, legal-decision engine, commercial district scoring engine, or automatic advertising-copy generator.

## Required Technical Direction

- Java 21
- Spring Boot 3.x
- Thymeleaf
- Bootstrap 5.3
- MyBatis
- MySQL
- JUnit 5
- Gradle
- Kakao Maps
- `GeocodingGateway`
- `FileStorage`

## Repository State

As of 2026-06-04, the repository had no product code, build files, or docs before initialization. Planning/memory documents were added first so implementation can start from explicit constraints.

As of 2026-06-05, Phase 1 privacy-first domain and persistence shapes exist. Internal records are modeled separately from public share snapshots; the first Flyway migration and MyBatis mapper XML define internal inspection persistence without exposing public snapshot DTOs.

## Key Assumptions

- Public customer share cards should be generated from snapshot data.
- Internal broker records may contain sensitive notes, contact details, risk memos, and operational context.
- Public proposal content should be curated and factual, not automatically generated promotional copy.
- Facility-check templates should be data-driven by business type.
- Search and maps should use a dedicated search/index structure rather than private internal notes.
