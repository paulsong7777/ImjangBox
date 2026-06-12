# Issue #4 Mobile-First Broker Inspection Form UX Plan

**Created:** 2026-06-12
**Status:** Planning complete; implementation not started
**Scope:** Improve the existing broker inspection create/edit form UX for mobile field capture without starting Phase 6.

## Current Form Findings

The current broker form is a single Thymeleaf/Bootstrap page shared by create and edit:

- Create route: `/broker/inspections/new` and `POST /broker/inspections`.
- Edit route: `/broker/inspections/{inspectionId}/edit` and `POST /broker/inspections/{inspectionId}`.
- Existing sections: basic property info, address, Kakao map boundary, pricing/status, facility template answers, internal-only records, contact log, attachments, and edit-only share-card creation.
- Required save fields are `title`, `businessType`, `internalRoadAddress`, `publicAddressSummary`, `depositAmount`, `monthlyRentAmount`, `premiumAmount`, and `verificationStatus`.
- Contact log fields must be entered together when used.
- Facility answers are dynamic by business type and are persisted separately from template definitions.
- Attachments use the existing `attachments` multipart field and `FileStorage` validation boundary.
- The map uses the existing `kakaoMap` view model and must keep browser JavaScript keys separate from backend REST keys.
- Share-card creation is available only from the broker edit page and must continue to use persisted public snapshots.

## UX Problem

The form is functional but too flat for field use on a phone. Brokers need to save a viable inspection quickly while standing on site, then fill in richer notes, contact history, facility details, and share-card readiness afterward. The UX should reduce first-save friction without adding a draft model or weakening current validation.

## Field Placement

| Placement | Fields | Reason |
| --- | --- | --- |
| Quick-save | `title` | Broker needs a recognizable record name immediately. |
| Quick-save | `businessType` | Drives facility template context and search/index meaning. |
| Quick-save | `internalRoadAddress` | Required exact broker location for internal operations. |
| Quick-save | `publicAddressSummary` | Required customer-safe location summary and share/search input. |
| Quick-save | `depositAmount`, `monthlyRentAmount`, `premiumAmount` | Required commercial pricing facts; defaults may remain `0`. |
| Quick-save | `verificationStatus` | Required factual confidence state; default remains `UNVERIFIED`. |
| Quick-save | `attachments` | Field photo/file capture is most useful while on site; validation stays unchanged. |
| Detailed follow-up | `internalDetailAddress` | Useful but not required for first save. |
| Detailed follow-up | `publicLandmarkHint` | Helpful for proposals, but can be refined later. |
| Detailed follow-up | `areaSquareMeters` | Often known after documents or follow-up. |
| Detailed follow-up | `businessFitMemo`, `conditionMemo` | Narrative notes are important but should not block first capture. |
| Detailed follow-up | `facilityAnswers[...]` | Existing dynamic checklist remains available after business type selection; not all items are known immediately. |
| Detailed follow-up | `internalGeocodeMemo` and Kakao map display | Supports location review but should not block save or change map boundaries. |
| Detailed follow-up | `pricePrivateNote`, `privateMemo`, `internalRiskMemo` | Internal-only context belongs away from the first-save path and must remain clearly marked. |
| Detailed follow-up | `contactedOn`, `contactLogContent` | Contact records are append-only internal follow-up and must keep paired validation. |
| Edit-only action | Customer share-card generation | Keep separate from create and from the quick-save submit path. |

## Proposed Mobile-First Structure

1. Header
   - Keep a compact page title.
   - Add create/edit context through existing model data only; do not introduce new backend state.

2. Quick-save section
   - Put all required save fields first.
   - Use stacked full-width controls on mobile and Bootstrap grid only at wider breakpoints.
   - Move attachments into this early section under a label such as `현장 사진/파일`.
   - Add concise helper text only where it prevents privacy mistakes, especially public address vs internal address.
   - Keep one primary submit button visually available near the top and repeat or make the final submit area sticky for mobile.

3. Follow-up sections
   - Group optional fields below quick-save as scannable sections:
     - `위치 보강`: internal detail address, public landmark hint, map, internal geocode memo.
     - `시설 확인`: dynamic facility template answers.
     - `메모`: business fit and condition notes.
     - `내부 전용`: private price note, private memo, internal risk memo.
     - `연락 기록`: contact date and content pair.
   - Prefer native `<details>` or simple Bootstrap sections so the page does not require adding Bootstrap JavaScript.
   - Keep private/internal section headings explicit.

4. Edit-only share action
   - Keep share-card generation outside the main save form.
   - Label it as a separate customer-facing snapshot action so brokers do not confuse save with sharing.
   - Preserve the current CSRF-protected POST behavior.

## Implementation Steps

1. Add characterization tests for the current mobile-first target shape before changing the template:
   - quick-save heading renders before detailed follow-up headings;
   - required fields and `attachments` appear in the quick-save section;
   - internal-only fields render only in the detailed internal section;
   - contact-log paired validation and facility answer preservation still work;
   - edit pages still render the separate share-card form.

2. Restructure `src/main/resources/templates/inspection/form.html` only:
   - keep the same form action, model object, field names, multipart encoding, CSRF behavior, and submit labels;
   - move existing controls into the quick-save and follow-up groups;
   - keep `attachments` named exactly `attachments`;
   - keep facility hidden fields and answer binding unchanged;
   - keep `data-kakao-map` attributes and disabled-map fallback unchanged;
   - keep the edit-only share form outside the save form.

3. Apply mobile-first Bootstrap refinements:
   - use tighter spacing on small screens and `row g-3` layouts for wider screens;
   - make the primary save action easy to reach on mobile;
   - use input modes or existing numeric types where appropriate;
   - avoid adding React, Vue, Next.js, custom build tooling, or new frontend dependencies.

4. Run focused verification:
   - `./gradlew test --tests com.imjangbox.inspection.web.BrokerInspectionControllerTest`
   - `./gradlew test --tests com.imjangbox.map.KakaoMapViewFactoryTest`
   - `./gradlew test --tests com.imjangbox.file.LocalFileStorageTest`
   - `./gradlew test --tests com.imjangbox.share.PublicShareControllerTest --tests com.imjangbox.share.ShareSnapshotServiceTest`

5. Run final verification:
   - `./gradlew test`
   - `git diff --check`
   - Manual broker form QA on a narrow/mobile viewport:
     - unauthenticated broker form access returns `401`;
     - authenticated create page shows quick-save first with one CSRF token;
     - valid create with required fields and an allowed image redirects to edit;
     - invalid create preserves facility answers and shows validation errors;
     - edit page shows saved values and the separate share-card action;
     - enabled and disabled Kakao map states still render without REST-key leakage;
     - generated public share card still excludes private markers, contact-log content, internal address, storage keys, local paths, and original filenames.

## Acceptance Criteria

- No schema, mapper, service, share snapshot, search-index, file-storage, or authentication behavior changes are required for Issue #4.
- The broker can still create and update inspections through the existing routes.
- Existing validation remains intact, including required fields, non-negative pricing, paired contact-log entry, facility answer preservation, and attachment validation.
- The first mobile screen prioritizes a quick save path: title, business type, internal address, public address summary, pricing, verification, attachments, and save action.
- Optional follow-up fields are grouped below the quick-save path and remain available on both create and edit.
- Internal-only fields remain clearly marked and never move into public share DTOs, templates, snapshots, or image routes.
- Facility template rendering remains dynamic by business type and answers remain independently persisted.
- Kakao map rendering remains controlled only through the existing `kakaoMap` view model and does not expose `KAKAO_REST_API_KEY`.
- Edit-only share-card generation remains a separate CSRF-protected action backed by persisted snapshots.
- Full regression tests and manual mobile broker/share QA pass before closing Issue #4.

## Out Of Scope

- Starting Phase 6.
- Adding draft-save persistence or relaxing required-field validation.
- Adding new database tables or migrations.
- Replacing Thymeleaf/Bootstrap with a client-side frontend framework.
- Automatic ad-copy generation, business-type legal judgment, commercial district scoring, or listing-platform upload.
