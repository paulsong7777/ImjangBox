package com.imjangbox.facility;

public record FacilityCheckAnswer(
		String templateItemKey,
		String businessType,
		String label,
		String answer,
		boolean customerVisible) {
}
