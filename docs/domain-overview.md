# Domain Overview

## Product Domain

imjangbox supports commercial real estate brokers who inspect candidate properties, record operational facts, and prepare customer-safe proposal cards.

## Core Concepts

- **Inspection record:** Internal broker record created during or after a site visit.
- **Proposal ledger:** Organized history of properties that may be proposed to customers.
- **Business fit:** Broker assessment of whether a property fits a target business type.
- **Premium/key-money:** Commercial-property pricing context that may include public figures and private broker notes.
- **Verification status:** Explicit claim/check state for facts about the property.
- **Share card:** Customer-facing snapshot generated from internal records.

## Verification Status Definitions

- `UNVERIFIED`: No claim or check has been recorded.
- `OWNER_CLAIM`: Information came from the owner.
- `TENANT_CLAIM`: Information came from the tenant.
- `CO_BROKER_CLAIM`: Information came from a cooperating broker.
- `AGENT_CHECKED`: Broker checked the information directly.
- `DOCUMENT_CHECKED`: Supporting document was checked.

## Product Position

The product records broker workflow and proposal readiness. It must not become an automated legal judgment system, district scoring engine, advertising-copy generator, or listing-platform uploader.
