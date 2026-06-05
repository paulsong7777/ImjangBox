package com.imjangbox.file;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

	StoredFile store(long inspectionId, MultipartFile file) throws IOException;
}
