package com.imjangbox.map;

import java.util.Objects;

public sealed interface GeocodingResult permits GeocodingResult.Success, GeocodingResult.Failure {

	static GeocodingResult success(GeocodedAddress address) {
		return new Success(address);
	}

	static GeocodingResult failure(GeocodingFailureReason reason, String message) {
		return new Failure(reason, message);
	}

	record Success(GeocodedAddress address) implements GeocodingResult {
		public Success {
			Objects.requireNonNull(address, "address must not be null");
		}
	}

	record Failure(GeocodingFailureReason reason, String message) implements GeocodingResult {
		public Failure {
			Objects.requireNonNull(reason, "reason must not be null");
			Objects.requireNonNull(message, "message must not be null");
		}
	}
}
