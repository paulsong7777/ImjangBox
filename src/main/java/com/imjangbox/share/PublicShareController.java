package com.imjangbox.share;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PublicShareController {

	private final ShareSnapshotService shareSnapshotService;

	public PublicShareController(ShareSnapshotService shareSnapshotService) {
		this.shareSnapshotService = shareSnapshotService;
	}

	@GetMapping("/share/{shareId}")
	String show(@PathVariable String shareId, Model model) {
		model.addAttribute("snapshot", shareSnapshotService.findSnapshot(shareId));
		return "share/public-card";
	}

	@GetMapping("/share/{shareId}/images/{displayOrder}")
	ResponseEntity<Resource> image(
			@PathVariable String shareId,
			@PathVariable int displayOrder) throws IOException {
		return shareSnapshotService.findImageFile(shareId, displayOrder)
				.map(image -> ResponseEntity.ok()
						.contentType(MediaType.parseMediaType(image.contentType()))
						.body(image.resource()))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
