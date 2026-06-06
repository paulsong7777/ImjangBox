package com.imjangbox.inspection.web;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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

import org.junit.jupiter.api.BeforeEach;
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
import com.imjangbox.facility.FacilityTemplateItem;
import com.imjangbox.facility.FacilityTemplateService;
import com.imjangbox.property.VerificationStatus;

@WebMvcTest(BrokerInspectionController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "BROKER")
class BrokerInspectionControllerTest {

	private final MockMvc mockMvc;

	@MockitoBean
	private InspectionService inspectionService;

	@MockitoBean
	private FacilityTemplateService facilityTemplateService;

	@Autowired
	BrokerInspectionControllerTest(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@BeforeEach
	void setUpFacilityTemplates() {
		when(facilityTemplateService.findBusinessTypes()).thenReturn(List.of("CAFE", "RESTAURANT"));
		when(facilityTemplateService.defaultBusinessType()).thenReturn("CAFE");
		when(facilityTemplateService.normalizeBusinessType(isNull())).thenReturn("CAFE");
		when(facilityTemplateService.normalizeBusinessType(anyString())).thenAnswer(invocation ->
				invocation.getArgument(0, String.class).trim().toUpperCase());
		when(facilityTemplateService.findItemsForBusinessType(anyString())).thenReturn(List.of());
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
	void newFormKeepsFreeformConditionMemo() throws Exception {
		mockMvc.perform(get("/broker/inspections/new"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("시설/상태 메모")))
				.andExpect(content().string(containsString("conditionMemo")));
	}

	@Test
	void newFormRendersFacilityTemplatesForSelectedBusinessType() throws Exception {
		when(facilityTemplateService.findItemsForBusinessType("CAFE")).thenReturn(List.of(
				new FacilityTemplateItem("CAFE", "water_supply", "급배수 확인", 10, true),
				new FacilityTemplateItem("CAFE", "electric_capacity", "전기 용량 확인", 20, false)));

		mockMvc.perform(get("/broker/inspections/new").param("businessType", "CAFE"))
				.andExpect(status().isOk())
				.andExpect(model().attribute("selectedBusinessType", "CAFE"))
				.andExpect(content().string(containsString("시설 체크 템플릿")))
				.andExpect(content().string(containsString("급배수 확인")))
				.andExpect(content().string(containsString("전기 용량 확인")))
				.andExpect(content().string(containsString("facilityAnswers[0].templateItemKey")))
				.andExpect(content().string(containsString("facilityAnswers[1].templateItemKey")))
				.andExpect(content().string(containsString("facilityAnswers[0].answer")));
	}

	@Test
	@WithMockUser(roles = "CUSTOMER")
	void newFormRejectsNonBrokerUsers() throws Exception {
		mockMvc.perform(get("/broker/inspections/new"))
				.andExpect(status().isForbidden());
	}

	@Test
	void createRejectsBlankRequiredFieldsWithSafeDefaults() throws Exception {
		when(facilityTemplateService.findItemsForBusinessType("CAFE")).thenReturn(List.of(
				new FacilityTemplateItem("CAFE", "water_supply", "급배수 확인", 10, true)));

		mockMvc.perform(multipart("/broker/inspections").with(csrf())
				.param("title", "")
				.param("businessType", "CAFE")
				.param("depositAmount", "-1")
				.param("monthlyRentAmount", "0")
				.param("premiumAmount", "0")
				.param("facilityAnswers[0].templateItemKey", "water_supply")
				.param("facilityAnswers[0].businessType", "CAFE")
				.param("facilityAnswers[0].label", "급배수 확인")
				.param("facilityAnswers[0].answer", "OK")
				.param("facilityAnswers[0].customerVisible", "true"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors(
						"inspectionForm", "title", "internalRoadAddress", "publicAddressSummary", "depositAmount"))
				.andExpect(content().string(containsString("입력값을 확인해 주세요")))
				.andExpect(content().string(containsString("facilityAnswers[0].templateItemKey")))
				.andExpect(content().string(containsString("<option value=\"OK\" selected=\"selected\">양호</option>")));
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
		when(facilityTemplateService.findItemsForBusinessType("CAFE")).thenReturn(List.of(
				new FacilityTemplateItem("CAFE", "water_supply", "급배수 확인", 10, true)));

		mockMvc.perform(multipart("/broker/inspections").with(csrf())
				.param("title", "성수역 1층 상가")
				.param("businessType", "CAFE")
				.param("internalRoadAddress", "서울 성동구 내부로 1")
				.param("publicAddressSummary", "성수역 인근")
				.param("depositAmount", "10000")
				.param("monthlyRentAmount", "550")
				.param("premiumAmount", "3000")
				.param("verificationStatus", "AGENT_CHECKED")
				.param("contactedOn", "2026-06-05")
				.param("contactLogContent", "임대인 통화 내용")
				.param("internalRiskMemo", "공유 금지 리스크")
				.param("facilityAnswers[0].templateItemKey", "water_supply")
				.param("facilityAnswers[0].businessType", "CAFE")
				.param("facilityAnswers[0].label", "조작된 시설 항목")
				.param("facilityAnswers[0].answer", "OK")
				.param("facilityAnswers[0].customerVisible", "false"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/broker/inspections/41/edit"));

		ArgumentCaptor<InspectionForm> form = ArgumentCaptor.forClass(InspectionForm.class);
		verify(inspectionService).create(form.capture(), anyAttachmentList());
		org.assertj.core.api.Assertions.assertThat(form.getValue().getInternalRiskMemo())
				.isEqualTo("공유 금지 리스크");
		org.assertj.core.api.Assertions.assertThat(form.getValue().getFacilityAnswers())
				.extracting(FacilityAnswerForm::getLabel, FacilityAnswerForm::getAnswer, FacilityAnswerForm::isCustomerVisible)
				.containsExactly(org.assertj.core.groups.Tuple.tuple("급배수 확인", "OK", true));
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
