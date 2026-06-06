# Search Index

## Purpose

Map and keyword search should use a separate `search_index` structure rather than direct scans over internal inspection records.

## Rationale

Internal records can contain private notes, risk memos, contact logs, and operational context. Search structures should contain only fields intentionally selected for search and map display.

## Candidate Indexed Fields

- Public or broker-safe property title.
- Public address or normalized public location tokens.
- Coordinates.
- Business type.
- Facility template tags selected for search.
- Verification status.
- Proposal readiness flags.

## Fields To Exclude

- `private_memo`
- `price_private_note`
- `stakeholder.phone`
- `contact_log.content`
- `internal_risk_memo`
- Raw internal address when not needed for search.

## Future Decisions

- Whether index refresh is synchronous on inspection save or asynchronous.
- Whether search index stores broker-team scoped data.
- How public address masking affects map clustering.

## Current Implementation

- `property_search_index` is created by Flyway V4 as a separate map/search structure.
- `InspectionService` refreshes the index synchronously after broker create/update writes.
- Indexed text is built from broker-safe fields only: title, public address summary, public landmark hint, business type, verification status, and area.
- The index excludes private memo, private price note, contact-log content, internal risk memo, and raw internal address.
- Latitude and longitude columns are nullable placeholders for later geocoding-backed map search.
