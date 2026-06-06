package com.imjangbox.map;

import java.math.BigDecimal;
import java.util.Objects;

public record GeocodedAddress(
		BigDecimal latitude,
		BigDecimal longitude,
		String addressName,
		String roadAddressName) {

	public GeocodedAddress {
		Objects.requireNonNull(latitude, "latitude must not be null");
		Objects.requireNonNull(longitude, "longitude must not be null");
		Objects.requireNonNull(addressName, "addressName must not be null");
	}
}
