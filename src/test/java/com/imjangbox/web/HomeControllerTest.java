package com.imjangbox.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.imjangbox.common.SecurityConfig;

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTest {

	private final MockMvc mockMvc;

	@Autowired
	HomeControllerTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	void indexRendersProductOverviewPage() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("임장박스")))
				.andExpect(content().string(containsString("상가 임장 기록")))
				.andExpect(content().string(containsString("내부 기록과 고객 공유를 분리")))
				.andExpect(content().string(containsString("브로커 화면 열기")))
				.andExpect(content().string(org.hamcrest.Matchers.not(containsString("foundation"))))
				.andExpect(content().string(org.hamcrest.Matchers.not(containsString("Commercial property"))));
	}

	@Test
	void unmatchedRoutesUseNormalNotFoundHandling() throws Exception {
		mockMvc.perform(get("/internal/unmatched"))
				.andExpect(status().isNotFound());
	}
}
