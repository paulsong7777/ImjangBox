package com.imjangbox.share;

import com.imjangbox.property.CommercialPropertyInspection;

public final class PublicShareSnapshotFactory {

	private PublicShareSnapshotFactory() {
	}

	public static PublicShareSnapshot from(String shareId, CommercialPropertyInspection inspection) {
		return new PublicShareSnapshot(
				shareId,
				inspection.title(),
				inspection.publicAddress(),
				new PublicPricingSnapshot(
						inspection.pricing().depositAmount(),
						inspection.pricing().monthlyRentAmount(),
						inspection.pricing().premiumAmount()),
				inspection.verificationStatus(),
				inspection.verificationStatus().customerDisplayText());
	}
}
