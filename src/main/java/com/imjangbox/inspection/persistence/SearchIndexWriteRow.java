package com.imjangbox.inspection.persistence;

public record SearchIndexWriteRow(
		long inspectionId,
		String title,
		String publicAddressSummary,
		String publicLandmarkHint,
		String businessType,
		String verificationStatus,
		String areaSquareMeters,
		String searchText) {
}
