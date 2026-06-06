# Integrations

## Kakao Maps

Kakao Maps should be used for map display and property-location workflows. Keep browser map behavior separate from domain privacy decisions.

## GeocodingGateway

`GeocodingGateway` should be the backend abstraction for geocoding.

Expected behavior:

- Convert address input into coordinates when possible.
- Return explicit failure results for invalid, ambiguous, unavailable, or provider-failed geocoding.
- Avoid corrupting existing inspection records when geocoding fails.
- Support test doubles for controller/service tests.

Current implementation:

- `GeocodingGateway` returns explicit success or failure results.
- `DisabledGeocodingGateway` is the default because `imjangbox.geocoding.kakao.enabled=false`.
- `KakaoGeocodingGateway` is enabled only when configuration opts in and `KAKAO_REST_API_KEY` is present.
- Kakao Local API `x` is stored as longitude and `y` as latitude.

## FileStorage

`FileStorage` should be the backend abstraction for file persistence.

Expected behavior:

- Store inspection attachments.
- Return metadata safe for internal use.
- Avoid exposing private storage paths or unrestricted file URLs in public share cards.
- Support validation for file size, type, and access control.

## Test Direction

- Unit test gateway failure mapping.
- MVC test flows that depend on geocoding with fake gateway results.
- Test file attachment metadata separately from public share-card rendering.
