# Product UX Notes

## Product-Level MVP Language

ImjangBox should read as a Korean-first field ledger for commercial real estate brokers, not as a generic CRUD or test screen.

The product core is now property map/list management first, not inspection-form first. The intended promise is:

> 상가 전문 공인중개사가 내 매물을 지도와 카드로 한눈에 관리하고, 사진과 가격 조건이 담긴 고객 제안 카드로 쉽게 공유하는 도구.

- Use `임장박스`, `상가 현장 기록`, `빠른 등록`, `현장 정보`, `임대 조건`, `시설 확인`, `내부 전용`, `연락 기록`, and `고객 공유`.
- Use `내 상가 매물`, `상가 매물 등록`, `지도`, `카드`, and `고객 제안 카드` for the broker's primary management flow.
- Keep technical field names only in HTML bindings and tests; do not show names such as `businessType`, enum values, or snapshot terminology in user-facing copy.
- Render verification status and common facility answers in Korean while preserving persisted enum and answer values.
- Keep internal broker notes visibly separated from customer-facing share content.
- Keep save/update actions visually separate from customer share-card generation.

## Management-First Broker Flow

The first broker screen should be `/broker/inspections`, not the create form. It should communicate:

- registered commercial properties are managed as cards;
- the map is the central location surface, even while coordinate persistence is not yet connected;
- filters such as `전체`, `확인중`, `제안 가능`, and `공유 가능` are simple presentational chips until backend filtering is explicitly planned;
- the empty state should invite a first property registration;
- property cards should show customer-facing location summary, commercial pricing, area, business type, verification status, and clear edit/share actions.

## Mobile-First Broker Flow

The broker form should prioritize a small first-screen save path:

- required property title and business context;
- internal address and customer-safe address summary;
- deposit, monthly rent, premium, area, verification status, and a short memo;
- field photos/files.

Secondary details should stay available but visually lower:

- detail address, landmark, area, and map memo;
- facility answers;
- business and condition notes;
- internal-only price, memo, and risk notes;
- contact logs.

No product UX polish should relax validation, add draft-save persistence, change public share privacy boundaries, or introduce a frontend framework.

## Customer Proposal Card

The public share page should feel like a clean customer proposal template:

- hero image or placeholder first;
- title, customer-safe location, confirmation status, and price summary;
- `이 매물의 핵심 정보` and readable facility chips;
- photo gallery from share-scoped public image routes;
- caution text that the card is a customer-facing summary and details should be confirmed through the broker.

It must continue to render only public snapshot data. Internal road/detail addresses, private memos, contact logs, storage keys, local paths, and original filenames remain denied from public output.
