package com.imjangbox.share;

import java.util.List;
import java.util.Objects;

import com.imjangbox.property.PublicAddress;
import com.imjangbox.property.VerificationStatus;

public record PublicShareSnapshot(
		String shareId,
		String title,
		PublicAddress publicAddress,
		PublicPricingSnapshot pricing,
		VerificationStatus verificationStatus,
		String verificationDisplayText,
		List<PublicFacilitySnapshot> facilities,
		List<PublicImageSnapshot> images) {

	public static final String RECOMMENDED_BUSINESS_TYPE_LABEL = "추천 업종";

	public PublicShareSnapshot {
		facilities = List.copyOf(Objects.requireNonNull(facilities, "facilities"));
		images = List.copyOf(Objects.requireNonNull(images, "images"));
	}

	public boolean hasRecommendedBusinessTypeLabel() {
		return !recommendedBusinessTypeLabel().isBlank();
	}

	public String recommendedBusinessTypeLabel() {
		return facilities.stream()
				.filter(facility -> RECOMMENDED_BUSINESS_TYPE_LABEL.equals(facility.label()))
				.map(PublicFacilitySnapshot::answer)
				.filter(answer -> answer != null && !answer.isBlank())
				.findFirst()
				.orElse("");
	}

	public List<PublicFacilitySnapshot> displayFacilities() {
		return facilities.stream()
				.filter(facility -> !RECOMMENDED_BUSINESS_TYPE_LABEL.equals(facility.label()))
				.toList();
	}
}
