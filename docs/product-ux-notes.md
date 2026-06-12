# Product UX Notes

## Product-Level MVP Language

ImjangBox should read as a Korean-first field ledger for commercial real estate brokers, not as a generic CRUD or test screen.

- Use `임장박스`, `상가 현장 기록`, `빠른 등록`, `현장 정보`, `임대 조건`, `시설 확인`, `내부 전용`, `연락 기록`, and `고객 공유`.
- Keep technical field names only in HTML bindings and tests; do not show names such as `businessType`, enum values, or snapshot terminology in user-facing copy.
- Render verification status and common facility answers in Korean while preserving persisted enum and answer values.
- Keep internal broker notes visibly separated from customer-facing share content.
- Keep save/update actions visually separate from customer share-card generation.

## Mobile-First Broker Flow

The broker form should prioritize a small first-screen save path:

- required property title and business context;
- internal address and customer-safe address summary;
- deposit, monthly rent, premium, and verification status;
- field photos/files.

Secondary details should stay available but visually lower:

- detail address, landmark, area, and map memo;
- facility answers;
- business and condition notes;
- internal-only price, memo, and risk notes;
- contact logs.

No product UX polish should relax validation, add draft-save persistence, change public share privacy boundaries, or introduce a frontend framework.
