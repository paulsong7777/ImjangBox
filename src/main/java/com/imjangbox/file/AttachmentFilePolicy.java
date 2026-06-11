package com.imjangbox.file;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class AttachmentFilePolicy {

	public static final String PDF = "application/pdf";
	public static final String JPEG = "image/jpeg";
	public static final String PNG = "image/png";
	public static final String PLAIN_TEXT = "text/plain";

	public static final Set<String> ALLOWED_ATTACHMENT_TYPES = Set.of(PDF, JPEG, PNG, PLAIN_TEXT);
	public static final Set<String> PUBLIC_IMAGE_TYPES = Set.of(JPEG, PNG);

	private static final Map<String, Set<String>> EXTENSIONS_BY_CONTENT_TYPE = Map.of(
			PDF, Set.of("pdf"),
			JPEG, Set.of("jpg", "jpeg"),
			PNG, Set.of("png"),
			PLAIN_TEXT, Set.of("txt"));

	private AttachmentFilePolicy() {
	}

	public static boolean isAllowedAttachmentType(String contentType) {
		return ALLOWED_ATTACHMENT_TYPES.contains(contentType);
	}

	public static boolean isPublicImageType(String contentType) {
		return PUBLIC_IMAGE_TYPES.contains(contentType);
	}

	public static boolean extensionMatchesContentType(String originalFilename, String contentType) {
		Set<String> allowedExtensions = EXTENSIONS_BY_CONTENT_TYPE.get(contentType);
		String extension = extension(originalFilename);
		return allowedExtensions != null && allowedExtensions.contains(extension);
	}

	private static String extension(String originalFilename) {
		if (originalFilename == null || originalFilename.isBlank()) {
			return "";
		}
		String normalized = originalFilename.replace('\\', '/');
		String basename = normalized.substring(normalized.lastIndexOf('/') + 1);
		int dotIndex = basename.lastIndexOf('.');
		if (dotIndex < 1 || dotIndex == basename.length() - 1) {
			return "";
		}
		return basename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
	}
}
