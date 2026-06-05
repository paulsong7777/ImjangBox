package com.imjangbox.file;

public record StoredFile(
		String storageKey,
		String originalFilename,
		String contentType,
		long sizeBytes) {
}
