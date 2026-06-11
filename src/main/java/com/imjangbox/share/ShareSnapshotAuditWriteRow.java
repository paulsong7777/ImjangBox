package com.imjangbox.share;

public record ShareSnapshotAuditWriteRow(
		String shareId,
		long inspectionId,
		String action,
		String actorUsername) {

	public static ShareSnapshotAuditWriteRow of(
			String shareId,
			long inspectionId,
			ShareSnapshotAuditAction action,
			String actorUsername) {
		return new ShareSnapshotAuditWriteRow(
				shareId,
				inspectionId,
				action.name(),
				actorUsername == null || actorUsername.isBlank() ? "system" : actorUsername);
	}
}
