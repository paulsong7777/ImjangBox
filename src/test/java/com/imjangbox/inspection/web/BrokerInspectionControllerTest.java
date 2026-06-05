package com.imjangbox.inspection.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.imjangbox.inspection.InspectionService;
import com.imjangbox.common.SecurityConfig;
import com.imjangbox.property.VerificationStatus;

@WebMvcTest(BrokerInspectionController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "BROKER")
class BrokerInspectionControllerTest {

	private final MockMvc mockMvc;

	@MockitoBean
	private InspectionService inspectionService;

	@Autowired
	BrokerInspectionControllerTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	void newFormRendersInternalBrokerFields() throws Exception {
		mockMvc.perform(get("/broker/inspections/new"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Bootstrap")))
				.andExpect(content().string(containsString("내부 주소")))
				.andExpect(content().string(containsString("내부 리스크 메모")))
				.andExpect(content().string(containsString("연락 기록")))
				.andExpect(content().string(containsString("첨부 파일")));
	}

	@Test
	@WithMockUser(roles = "CUSTOMER")
	void newFormRejectsNonBrokerUsers() throws Exception {
		mockMvc.perform(get("/broker/inspections/new"))
				.andExpect(status().isForbidden());
	}

	@Test
	void createRejectsBlankRequiredFieldsWithSafeDefaults() throws Exception {
		mockMvc.perform(multipart("/broker/inspections").with(csrf())
				.param("title", "")
				.param("depositAmount", "-1")
				.param("monthlyRentAmount", "0")
				.param("premiumAmount", "0"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors(
						"inspectionForm", "title", "internalRoadAddress", "publicAddressSummary", "depositAmount"))
				.andExpect(content().string(containsString("입력값을 확인해 주세요")));
	}

	@Test
	void createRejectsPartialContactLog() throws Exception {
		mockMvc.perform(multipart("/broker/inspections").with(csrf())
				.param("title", "성수역 1층 상가")
				.param("internalRoadAddress", "서울 성동구 내부로 1")
				.param("publicAddressSummary", "성수역 인근")
				.param("depositAmount", "10000")
				.param("monthlyRentAmount", "550")
				.param("premiumAmount", "3000")
				.param("verificationStatus", "AGENT_CHECKED")
				.param("contactLogContent", "날짜 없는 연락 기록"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("inspectionForm", "contactLogComplete"))
				.andExpect(content().string(containsString("입력값을 확인해 주세요")));
	}

	@Test
	void createSubmitsInspectionAndRedirectsToEditScreen() throws Exception {
		when(inspectionService.create(any(InspectionForm.class), anyAttachmentList())).thenReturn(41L);

		mockMvc.perform(multipart("/broker/inspections").with(csrf())
				.param("title", "성수역 1층 상가")
				.param("internalRoadAddress", "서울 성동구 내부로 1")
				.param("publicAddressSummary", "성수역 인근")
				.param("depositAmount", "10000")
				.param("monthlyRentAmount", "550")
				.param("premiumAmount", "3000")
				.param("verificationStatus", "AGENT_CHECKED")
				.param("contactedOn", "2026-06-05")
				.param("contactLogContent", "임대인 통화 내용")
				.param("internalRiskMemo", "공유 금지 리스크"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/broker/inspections/41/edit"));

		ArgumentCaptor<InspectionForm> form = ArgumentCaptor.forClass(InspectionForm.class);
		verify(inspectionService).create(form.capture(), anyAttachmentList());
		org.assertj.core.api.Assertions.assertThat(form.getValue().getInternalRiskMemo())
				.isEqualTo("공유 금지 리스크");
	}

	@Test
	void createRendersAttachmentErrorWhenServiceRejectsUploadedFile() throws Exception {
		when(inspectionService.create(any(InspectionForm.class), anyAttachmentList()))
				.thenThrow(new com.imjangbox.inspection.AttachmentValidationException("bad file"));

		mockMvc.perform(multipart("/broker/inspections")
				.file("attachments", "payload".getBytes())
				.with(csrf())
				.param("title", "성수역 1층 상가")
				.param("internalRoadAddress", "서울 성동구 내부로 1")
				.param("publicAddressSummary", "성수역 인근")
				.param("depositAmount", "10000")
				.param("monthlyRentAmount", "550")
				.param("premiumAmount", "3000")
				.param("verificationStatus", "AGENT_CHECKED"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("첨부 파일을 확인해 주세요.")));
	}

	@Test
	void editFormLoadsExistingInspection() throws Exception {
		InspectionForm form = new InspectionForm();
		form.setTitle("기존 상가");
		form.setVerificationStatus(VerificationStatus.UNVERIFIED);
		when(inspectionService.findForm(41L)).thenReturn(form);

		mockMvc.perform(get("/broker/inspections/41/edit"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("inspectionId", 41L))
				.andExpect(content().string(containsString("기존 상가")));
	}

	@Test
	void updateSubmitsInspectionAndRedirectsBackToEditScreen() throws Exception {
		mockMvc.perform(multipart("/broker/inspections/41").with(csrf())
				.param("_method", "POST")
				.param("title", "수정 상가")
				.param("internalRoadAddress", "서울 성동구 내부로 2")
				.param("publicAddressSummary", "성수역 4번 출구")
				.param("depositAmount", "15000")
				.param("monthlyRentAmount", "650")
				.param("premiumAmount", "2500")
				.param("verificationStatus", "DOCUMENT_CHECKED"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/broker/inspections/41/edit"));

		verify(inspectionService).update(eq(41L), any(InspectionForm.class), anyAttachmentList());
	}

	private List<MultipartFile> anyAttachmentList() {
		return org.mockito.ArgumentMatchers.anyList();
	}
}
