package com.imjangbox.inspection;

import com.imjangbox.facility.BusinessTypeCatalog;
import com.imjangbox.property.VerificationStatus;

public record PropertyDashboardItem(
		long inspectionId,
		String title,
		String businessType,
		VerificationStatus verificationStatus,
		String publicAddressSummary,
		String publicLandmarkHint,
		long depositAmount,
		long monthlyRentAmount,
		long premiumAmount,
		String areaSquareMeters,
		boolean hasImageAttachment) {

	public String businessTypeLabel() {
		return BusinessTypeCatalog.label(businessType);
	}

	public String verificationStatusLabel() {
		return switch (verificationStatus) {
			case UNVERIFIED -> "미확인";
			case OWNER_CLAIM -> "임대인 주장";
			case TENANT_CLAIM -> "임차인 주장";
			case CO_BROKER_CLAIM -> "공동중개 확인";
			case AGENT_CHECKED -> "현장 확인";
			case DOCUMENT_CHECKED -> "서류 확인";
		};
	}

	public boolean hasPublicLandmarkHint() {
		return hasText(publicLandmarkHint);
	}

	public boolean hasAreaSquareMeters() {
		return hasText(areaSquareMeters);
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
