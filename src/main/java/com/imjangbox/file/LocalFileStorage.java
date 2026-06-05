package com.imjangbox.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalFileStorage implements FileStorage {

	private static final Pattern UNSAFE_FILENAME_CHARACTER = Pattern.compile("[^A-Za-z0-9._-]");
	private static final int MAX_FILENAME_LENGTH = 120;

	@Override
	public StoredFile store(long inspectionId, MultipartFile file) throws IOException {
		String originalFilename = safeFilename(file.getOriginalFilename());
		String storageKey = "inspection-files/" + inspectionId + "/" + UUID.randomUUID() + extension(originalFilename);
		Path target = localRoot().resolve(storageKey);
		Files.createDirectories(target.getParent());
		try (InputStream input = file.getInputStream()) {
			Files.copy(input, target);
		}
		return new StoredFile(storageKey, originalFilename, file.getContentType(), file.getSize());
	}

	private Path localRoot() {
		return Path.of(System.getProperty("java.io.tmpdir"), "imjangbox-local-files");
	}

	private String safeFilename(String originalFilename) {
		if (originalFilename == null || originalFilename.isBlank()) {
			return "attachment";
		}
		String normalized = Normalizer.normalize(originalFilename, Normalizer.Form.NFKC);
		String basename = normalized.replace('\\', '/');
		basename = basename.substring(basename.lastIndexOf('/') + 1);
		String sanitized = UNSAFE_FILENAME_CHARACTER.matcher(basename).replaceAll("_");
		if (sanitized.isBlank() || sanitized.equals(".") || sanitized.equals("..")) {
			return "attachment";
		}
		return sanitized.length() > MAX_FILENAME_LENGTH ? sanitized.substring(0, MAX_FILENAME_LENGTH) : sanitized;
	}

	private String extension(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex < 1 || dotIndex == filename.length() - 1) {
			return "";
		}
		return filename.substring(dotIndex);
	}
}
