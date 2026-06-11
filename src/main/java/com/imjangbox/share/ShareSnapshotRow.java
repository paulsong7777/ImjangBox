package com.imjangbox.share;

public record ShareSnapshotRow(
		String shareId,
		String title,
		String verificationStatus,
		String verificationDisplayText,
		String publicAddressSummary,
		String publicLandmarkHint,
		long depositAmount,
		long monthlyRentAmount,
		long premiumAmount) {
}
