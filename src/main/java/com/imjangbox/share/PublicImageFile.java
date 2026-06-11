package com.imjangbox.share;

import org.springframework.core.io.Resource;

public record PublicImageFile(
		Resource resource,
		String contentType) {
}
