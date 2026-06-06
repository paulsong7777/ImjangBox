package com.imjangbox.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class LocalFileStorageTest {

	@Test
	void storeSanitizesClientFilenameBeforeBuildingStorageKey(@TempDir Path localRoot) throws Exception {
		LocalFileStorage storage = new LocalFileStorage(localRoot);

		StoredFile storedFile = storage.store(41L, new MockMultipartFile(
				"attachments", "../위험\nfloor plan.png", "image/png", "file".getBytes()));

		assertThat(storedFile.originalFilename()).isEqualTo("___floor_plan.png");
		assertThat(storedFile.storageKey())
				.startsWith("inspection-files/41/")
				.endsWith(".png")
				.doesNotContain("..")
				.doesNotContain("floor_plan")
				.doesNotContain("\n");
		assertThat(Files.readString(localRoot.resolve(storedFile.storageKey()))).isEqualTo("file");
	}
}
