package com.imjangbox.share;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.imjangbox.common.SecurityConfig;
import com.imjangbox.property.PublicAddress;
import com.imjangbox.property.VerificationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PublicShareController.class)
@Import(SecurityConfig.class)
class PublicShareControllerTest {

	private final MockMvc mockMvc;

	@MockitoBean
	private ShareSnapshotService shareSnapshotService;

	@Autowired
	PublicShareControllerTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	void publicShareCardRendersSnapshotWithoutPrivateFields() throws Exception {
		when(shareSnapshotService.findSnapshot("share-1")).thenReturn(new PublicShareSnapshot(
				"share-1",
				"Customer-safe title",
				new PublicAddress("PUBLIC_DISTRICT_VALUE", "near station"),
				new PublicPricingSnapshot(100_000_000L, 6_000_000L, 30_000_000L),
				VerificationStatus.AGENT_CHECKED,
				"Agent checked",
				List.of(new PublicFacilitySnapshot("Customer-visible water", "OK")),
				List.of(new PublicImageSnapshot("/share/share-1/images/1", "Property image 1"))));

		mockMvc.perform(get("/share/share-1"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Bootstrap")))
				.andExpect(content().string(containsString("Customer-safe title")))
				.andExpect(content().string(containsString("PUBLIC_DISTRICT_VALUE")))
				.andExpect(content().string(containsString("Agent checked")))
				.andExpect(content().string(containsString("Customer-visible water")))
				.andExpect(content().string(containsString("/share/share-1/images/1")))
				.andExpect(content().string(not(containsString("PRIVATE_MEMO_VALUE"))))
				.andExpect(content().string(not(containsString("pricePrivateNote"))))
				.andExpect(content().string(not(containsString("contactLogs"))))
				.andExpect(content().string(not(containsString("internalRiskMemo"))))
				.andExpect(content().string(not(containsString("stakeholder"))))
				.andExpect(content().string(not(containsString("storageKey"))));
	}

	@Test
	void publicShareImageStreamsSelectedSnapshotImageWithoutStorageKeyInUrl() throws Exception {
		when(shareSnapshotService.findImageFile("share-1", 1)).thenReturn(Optional.of(new PublicImageFile(
				new ByteArrayResource("image-bytes".getBytes()),
				"image/png")));

		mockMvc.perform(get("/share/share-1/images/1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("image/png"))
				.andExpect(content().bytes("image-bytes".getBytes()));
	}
}
