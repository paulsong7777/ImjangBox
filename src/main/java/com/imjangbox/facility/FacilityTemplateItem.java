package com.imjangbox.facility;

public record FacilityTemplateItem(
		String businessType,
		String itemKey,
		String label,
		int displayOrder,
		boolean customerVisible) {
}
