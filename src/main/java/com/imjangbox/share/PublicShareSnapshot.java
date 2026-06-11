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

	public PublicShareSnapshot {
		facilities = List.copyOf(Objects.requireNonNull(facilities, "facilities"));
		images = List.copyOf(Objects.requireNonNull(images, "images"));
	}
}
