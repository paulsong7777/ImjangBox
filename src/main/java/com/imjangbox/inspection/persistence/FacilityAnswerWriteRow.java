package com.imjangbox.inspection.persistence;

public record FacilityAnswerWriteRow(
		long inspectionId,
		String templateItemKey,
		String businessType,
		String label,
		String answer,
		boolean customerVisible) {
}
