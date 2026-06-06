package com.imjangbox.inspection.persistence;

import java.util.List;
import java.util.Objects;

public record PropertyInspectionRow(
		long inspectionId,
		String title,
		String businessType,
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
		String internalRiskMemo,
		List<ContactLogWriteRow> contactLogs,
		List<FileAttachmentWriteRow> fileAttachments) {

	public PropertyInspectionRow(
			long inspectionId,
			String title,
			String businessType,
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
		this(
				inspectionId,
				title,
				businessType,
				verificationStatus,
				internalRoadAddress,
				internalDetailAddress,
				internalGeocodeMemo,
				publicAddressSummary,
				publicLandmarkHint,
				depositAmount,
				monthlyRentAmount,
				premiumAmount,
				areaSquareMeters,
				businessFitMemo,
				conditionMemo,
				pricePrivateNote,
				privateMemo,
				internalRiskMemo,
				List.of(),
				List.of());
	}

	public PropertyInspectionRow {
		contactLogs = List.copyOf(Objects.requireNonNull(contactLogs, "contactLogs"));
		fileAttachments = List.copyOf(Objects.requireNonNull(fileAttachments, "fileAttachments"));
	}
}
