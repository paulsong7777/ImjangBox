package com.imjangbox.inspection.persistence;

import com.imjangbox.file.StoredFile;

public record FileAttachmentWriteRow(
		long inspectionId,
		String originalFilename,
		String storageKey,
		String contentType,
		long sizeBytes) {

	public static FileAttachmentWriteRow from(long inspectionId, StoredFile storedFile) {
		return new FileAttachmentWriteRow(
				inspectionId,
				storedFile.originalFilename(),
				storedFile.storageKey(),
				storedFile.contentType(),
				storedFile.sizeBytes());
	}
}
