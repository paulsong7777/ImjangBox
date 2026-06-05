package com.imjangbox.inspection.persistence;

public record ContactLogWriteRow(
		long inspectionId,
		String contactedOn,
		String content) {
}
