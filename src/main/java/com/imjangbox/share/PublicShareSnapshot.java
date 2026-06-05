package com.imjangbox.share;

import com.imjangbox.property.PublicAddress;
import com.imjangbox.property.VerificationStatus;

public record PublicShareSnapshot(
		String shareId,
		String title,
		PublicAddress publicAddress,
		PublicPricingSnapshot pricing,
		VerificationStatus verificationStatus,
		String verificationDisplayText) {
}
