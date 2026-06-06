package com.imjangbox.map;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DisabledGeocodingGatewayTest {

	@Test
	void geocodeReturnsInvalidAddressWhenInputIsBlank() {
		GeocodingGateway gateway = new DisabledGeocodingGateway();

		GeocodingResult result = gateway.geocode("   ");

		assertThat(result).isInstanceOf(GeocodingResult.Failure.class);
		GeocodingResult.Failure failure = (GeocodingResult.Failure) result;
		assertThat(failure.reason()).isEqualTo(GeocodingFailureReason.INVALID_ADDRESS);
	}

	@Test
	void geocodeReturnsUnavailableWhenGatewayIsDisabled() {
		GeocodingGateway gateway = new DisabledGeocodingGateway();

		GeocodingResult result = gateway.geocode("Seoul Seongdong-gu Seongsu-dong 1");

		assertThat(result).isInstanceOf(GeocodingResult.Failure.class);
		GeocodingResult.Failure failure = (GeocodingResult.Failure) result;
		assertThat(failure.reason()).isEqualTo(GeocodingFailureReason.UNAVAILABLE);
	}
}
