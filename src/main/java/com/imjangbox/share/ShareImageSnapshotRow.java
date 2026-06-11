package com.imjangbox.share;

public record ShareImageSnapshotRow(
		String shareId,
		int displayOrder,
		String contentType,
		String sourceStorageKey) {
}
