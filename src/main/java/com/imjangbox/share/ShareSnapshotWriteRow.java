package com.imjangbox.share;

public record ShareSnapshotWriteRow(
		String shareId,
		long inspectionId,
		String title,
		String verificationStatus,
		String verificationDisplayText,
		String publicAddressSummary,
		String publicLandmarkHint,
		long depositAmount,
		long monthlyRentAmount,
		long premiumAmount) {

	public static ShareSnapshotWriteRow from(long inspectionId, PublicShareSnapshot snapshot) {
		return new ShareSnapshotWriteRow(
				snapshot.shareId(),
				inspectionId,
				snapshot.title(),
				snapshot.verificationStatus().name(),
				snapshot.verificationDisplayText(),
				snapshot.publicAddress().summary(),
				snapshot.publicAddress().landmarkHint(),
				snapshot.pricing().depositAmount(),
				snapshot.pricing().monthlyRentAmount(),
				snapshot.pricing().premiumAmount());
	}
}
