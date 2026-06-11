package com.imjangbox.share;

public record ShareSnapshotAuditRow(
		String shareId,
		long inspectionId,
		String action,
		String actorUsername) {
}
