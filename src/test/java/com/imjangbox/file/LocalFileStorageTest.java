package com.imjangbox.file;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class LocalFileStorageTest {

	@Test
	void storeSanitizesClientFilenameBeforeBuildingStorageKey() throws Exception {
		LocalFileStorage storage = new LocalFileStorage();

		StoredFile storedFile = storage.store(41L, new MockMultipartFile(
				"attachments", "../위험\nfloor plan.png", "image/png", "file".getBytes()));

		assertThat(storedFile.originalFilename()).isEqualTo("___floor_plan.png");
		assertThat(storedFile.storageKey())
				.startsWith("inspection-files/41/")
				.endsWith(".png")
				.doesNotContain("..")
				.doesNotContain("floor_plan")
				.doesNotContain("\n");
	}
}
