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

## Product UI Fit Pass Criteria

The Product UI Fit Pass keeps the existing feature set but raises the current screens to early-tester product quality:

- Dashboard layout should be card-first. The map should support location context without dominating the page while coordinate persistence is still incomplete.
- Avoid developer-facing copy such as coordinate-storage, disabled integration, SDK-key, or snapshot language in user-visible areas.
- Property cards should immediately show photo readiness, title, customer-safe location, recommended business type, verification status, area, and 보증금/월세/권리금 in `만원`.
- Dashboard actions should be task-oriented: `매물 수정` and `고객 제안 카드 만들기` read better than generic edit/share labels.
- The broker form should read as `상가 매물 등록`, not only as an inspection note form.
- The first form screen should make title, address, customer-safe location, price, premium, area, recommended business type, photo upload, and short memo feel like the core save path.
- The public share card should look like a customer proposal template, not a raw public page.
- Customer-facing verification language must not use internal-sounding claim labels. Use `세부 조건 확인 필요`, `임대인 제공 정보`, `임차인 제공 정보`, `중개 경로 확인 정보`, `현장 확인 완료`, and `서류 확인 완료`.

## Commercial Practice Domain Fit

ImjangBox should not look like a cafe/restaurant demo. The recommended business type should feel broad enough for commercial-property brokerage work while staying manageable for an MVP.

- Use 12-14 high-signal business categories, not dozens of hard-to-maintain niche categories.
- Recommended business labels should sound like broker-entered context, not automatic legal approval or suitability scoring.
- Current MVP categories are `카페·디저트`, `음식점`, `주점·야간영업`, `배달·포장 전문점`, `미용·네일·뷰티`, `학원·교습`, `병의원·클리닉`, `사무실`, `소매점·편집샵`, `공방·스튜디오`, `헬스·PT·필라테스`, `무인점포`, `창고·작업장`, and `기타/직접입력`.
- Facility checks should change materially by business type. Examples: food uses duct/gas/kitchen/fire/restoration checks; clinic uses elevator/parking/area/sign/access checks; fitness uses ceiling height/floor load/noise/shower/ventilation checks.
- Public proposal cards may show `추천 업종: ...` when the broker entered it, but must not imply that the app automatically judged licensing, use approval, or market fit.

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
