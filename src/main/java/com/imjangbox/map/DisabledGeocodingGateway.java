package com.imjangbox.map;

public class DisabledGeocodingGateway implements GeocodingGateway {

	@Override
	public GeocodingResult geocode(String address) {
		if (!hasText(address)) {
			return GeocodingResult.failure(
					GeocodingFailureReason.INVALID_ADDRESS,
					"Address is required for geocoding.");
		}
		return GeocodingResult.failure(
				GeocodingFailureReason.UNAVAILABLE,
				"Geocoding provider is not configured.");
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
