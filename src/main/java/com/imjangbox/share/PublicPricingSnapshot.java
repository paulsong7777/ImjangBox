package com.imjangbox.share;

public record PublicPricingSnapshot(
		long depositAmount,
		long monthlyRentAmount,
		long premiumAmount) {
}
