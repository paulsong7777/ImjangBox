package com.imjangbox.map;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

class KakaoGeocodingGateway implements GeocodingGateway {

	private final RestClient restClient;
	private final String restApiKey;

	KakaoGeocodingGateway(RestClient restClient, String restApiKey) {
		this.restClient = restClient;
		this.restApiKey = restApiKey;
	}

	@Override
	public GeocodingResult geocode(String address) {
		if (!hasText(address)) {
			return GeocodingResult.failure(
					GeocodingFailureReason.INVALID_ADDRESS,
					"Address is required for geocoding.");
		}
		try {
			KakaoAddressSearchResponse response = restClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/v2/local/search/address.json")
							.queryParam("query", address.trim())
							.build())
					.header("Authorization", "KakaoAK " + restApiKey)
					.retrieve()
					.body(KakaoAddressSearchResponse.class);
			return mapResponse(response);
		}
		catch (RestClientException | NumberFormatException ex) {
			return GeocodingResult.failure(
					GeocodingFailureReason.PROVIDER_FAILED,
					"Geocoding provider request failed.");
		}
	}

	private GeocodingResult mapResponse(KakaoAddressSearchResponse response) {
		if (response == null || response.documents() == null || response.documents().isEmpty()) {
			return GeocodingResult.failure(
					GeocodingFailureReason.INVALID_ADDRESS,
					"Address could not be geocoded.");
		}
		if (response.documents().size() > 1) {
			return GeocodingResult.failure(
					GeocodingFailureReason.AMBIGUOUS_ADDRESS,
					"Address matched multiple geocoding results.");
		}
		KakaoAddressDocument document = response.documents().getFirst();
		if (!hasText(document.x()) || !hasText(document.y()) || !hasText(document.addressName())) {
			return GeocodingResult.failure(
					GeocodingFailureReason.PROVIDER_FAILED,
					"Geocoding provider returned an incomplete result.");
		}
		String roadAddressName = document.roadAddress() == null ? null : document.roadAddress().addressName();
		return GeocodingResult.success(new GeocodedAddress(
				new BigDecimal(document.y()),
				new BigDecimal(document.x()),
				document.addressName(),
				roadAddressName));
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}

	private record KakaoAddressSearchResponse(List<KakaoAddressDocument> documents) {
	}

	private record KakaoAddressDocument(
			@com.fasterxml.jackson.annotation.JsonProperty("address_name") String addressName,
			String x,
			String y,
			@com.fasterxml.jackson.annotation.JsonProperty("road_address") KakaoRoadAddress roadAddress) {
	}

	private record KakaoRoadAddress(
			@com.fasterxml.jackson.annotation.JsonProperty("address_name") String addressName) {
	}
}
