# Privacy And Share Cards

## Public Output Rule

Public share cards are allowlist-first. Internal records are not public response models.

The following fields are always denied in public output:

- `private_memo`
- `price_private_note`
- `stakeholder.phone`
- `contact_log.content`
- `internal_risk_memo`

## Snapshot Requirement

Share cards must be generated from snapshot structures.

Snapshots should contain only customer-safe fields selected at share time. They should not expose direct internal entity objects, internal mapper results, or internal-only identifiers unless a public identifier is explicitly designed.

Current implementation stores the top-level card in `public_share_snapshots`, customer-visible facility answers in `public_share_snapshot_facilities`, and share-scoped image metadata in `public_share_snapshot_images`. Public reads reconstruct `PublicShareSnapshot` from those snapshot tables only. Image binaries are served through share-scoped URLs, not by exposing file-storage keys or original filenames.

Snapshot stability is required: changing an internal inspection after share-card generation must not change an existing public card. A broker should create a new share snapshot when a new customer-facing version is needed.

## Address Separation

Internal address and public address must be distinct.

Implementation should use explicit names such as `internalAddress` and `publicAddress` rather than a single ambiguous `address` field.

## Template Safety

Public Thymeleaf templates should receive public snapshot DTOs only. Passing internal records into public templates is a design error even if the template currently displays only safe fields.

## Regression Tests

- Public DTO serialization does not contain denied field names.
- Public template rendering does not contain denied field names or sample private values.
- Share-card generation copies only allowlisted fields.
- Internal address values do not appear where public address values are expected.
- Share-card pages render from snapshots and remain stable after internal record updates.
- Every verification status has customer-safe display text.
