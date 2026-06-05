package com.imjangbox.inspection.persistence;

public record PropertyInspectionRow(
		long inspectionId,
		String title,
		String verificationStatus,
		String internalRoadAddress,
		String internalDetailAddress,
		String internalGeocodeMemo,
		String publicAddressSummary,
		String publicLandmarkHint,
		long depositAmount,
		long monthlyRentAmount,
		long premiumAmount,
		String areaSquareMeters,
		String businessFitMemo,
		String conditionMemo,
		String pricePrivateNote,
		String privateMemo,
		String internalRiskMemo) {
}
