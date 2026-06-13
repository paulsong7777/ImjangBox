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

## Business Type Taxonomy

Recommended business type is a broker-entered commercial-property hint, not an automatic suitability judgment.

The MVP commercial taxonomy should stay practical and small enough to maintain:

- `CAFE_DESSERT`: 카페·디저트
- `RESTAURANT`: 음식점
- `BAR_NIGHT`: 주점·야간영업
- `DELIVERY_TAKEOUT`: 배달·포장 전문점
- `BEAUTY`: 미용·네일·뷰티
- `ACADEMY`: 학원·교습
- `CLINIC`: 병의원·클리닉
- `OFFICE`: 사무실
- `RETAIL`: 소매점·편집샵
- `STUDIO_WORKSHOP`: 공방·스튜디오
- `FITNESS`: 헬스·PT·필라테스
- `UNMANNED_STORE`: 무인점포
- `STORAGE_WORKSPACE`: 창고·작업장
- `GENERAL`: 기타/직접입력

Required implication:

- Legacy `CAFE` input should remain understandable as `CAFE_DESSERT`.
- `RESTAURANT` and `GENERAL` must remain compatible with existing records and tests.
- Public pages should present the value as `추천 업종`, preserving the broker-entered tone.
- Do not add automatic 업종 적합도, 상권분석, 매출 추정, or ranking language without a separate product phase.

## Facility Checks

Facility checks should support dynamic template-based items by business type.

Required implication:

- Business-type templates should be data-driven.
- Answers should be stored separately from template definitions.
- Historical inspections should remain understandable if templates later change.
- Customer share cards may include only answers marked customer-visible at snapshot time.
- The default MVP checklist should reflect commercial brokerage field checks such as 급배수, 덕트, 도시가스, 소방, 전기 용량, 층고, 바닥 하중, 간판 노출, 주차, 상하차, 민원 가능성, 보안, and 접근성 depending on business type.
- A customer share card may expose the recommended business type as customer-safe proposal context, but it must not expose storage keys, local paths, original filenames, internal address, private notes, or contact logs.

## Map Search

Map search should consider a separate `search_index` structure.

Required implication:

- Search should not rely on scanning private notes.
- Geocoding should be behind `GeocodingGateway`.
- Kakao Maps concerns should remain separate from core property and share-card privacy logic.
