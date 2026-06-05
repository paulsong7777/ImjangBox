package com.imjangbox.property;

public record Pricing(
		long depositAmount,
		long monthlyRentAmount,
		long premiumAmount,
		String pricePrivateNote) {
}
