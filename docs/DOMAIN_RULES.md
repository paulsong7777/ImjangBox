# Domain Rules

## Public Share Privacy

Customer share cards must never expose internal information. Treat public output as deny-by-default.

The following fields must never appear in public share responses, rendered public templates, public JSON, or customer share snapshots:

- `private_memo`
- `price_private_note`
- `stakeholder.phone`
- `contact_log.content`
- `internal_risk_memo`

## Snapshot Rule

Share cards should use snapshot structures, not direct internal entity exposure.

Required implication:

- Internal records can change after sharing.
- Public share cards should remain stable unless a new snapshot/version is created.
- Public DTOs should not embed internal entities or mapper result objects.
- Public image URLs should use share-scoped public references, not file-storage keys or original internal filenames.
- Share-card snapshot creation and regenerated share-card versions should leave internal audit records without adding audit data to public output.

## Address Separation

Internal address and public address must be separate fields or structures.

Required implication:

- Internal address can support broker operations and exact field work.
- Public address can be rounded, masked, simplified, or otherwise customer-safe.
- Code and schema names should make the distinction explicit.

## Verification Status

Verification status must include exactly these initial states:

- `UNVERIFIED`
- `OWNER_CLAIM`
- `TENANT_CLAIM`
- `CO_BROKER_CLAIM`
- `AGENT_CHECKED`
- `DOCUMENT_CHECKED`

Customer-facing wording must not overstate verification strength.

## Facility Checks

Facility checks should support dynamic template-based items by business type.

Required implication:

- Business-type templates should be data-driven.
- Answers should be stored separately from template definitions.
- Historical inspections should remain understandable if templates later change.
- Customer share cards may include only answers marked customer-visible at snapshot time.

## Map Search

Map search should consider a separate `search_index` structure.

Required implication:

- Search should not rely on scanning private notes.
- Geocoding should be behind `GeocodingGateway`.
- Kakao Maps concerns should remain separate from core property and share-card privacy logic.
