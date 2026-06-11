package com.imjangbox.file;

import java.io.IOException;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

	StoredFile store(long inspectionId, MultipartFile file) throws IOException;

	default Optional<Resource> load(String storageKey) throws IOException {
		return Optional.empty();
	}
}
