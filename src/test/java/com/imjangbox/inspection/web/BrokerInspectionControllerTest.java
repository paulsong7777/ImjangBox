package com.imjangbox.inspection.web;

import static org.hamcrest.Matchers.containsString;
import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.imjangbox.inspection.InspectionService;
import com.imjangbox.common.SecurityConfig;
import com.imjangbox.facility.FacilityTemplateItem;
import com.imjangbox.facility.FacilityTemplateService;
import com.imjangbox.map.KakaoMapView;
import com.imjangbox.map.KakaoMapViewFactory;
import com.imjangbox.property.VerificationStatus;
import com.imjangbox.share.PublicPricingSnapshot;
import com.imjangbox.share.PublicShareSnapshot;
import com.imjangbox.share.ShareSnapshotService;

@WebMvcTest(BrokerInspectionController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "BROKER")
class BrokerInspectionControllerTest {

	private final MockMvc mockMvc;

	@MockitoBean
	private InspectionService inspectionService;

	@MockitoBean
	private FacilityTemplateService facilityTemplateService;

	@MockitoBean
	private KakaoMapViewFactory kakaoMapViewFactory;

	@MockitoBean
	private ShareSnapshotService shareSnapshotService;

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
		when(kakaoMapViewFactory.brokerInspectionMap()).thenReturn(KakaoMapView.disabled());
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
	void newFormPrioritizesQuickSaveFieldsBeforeFollowUpSections() throws Exception {
		String html = mockMvc.perform(get("/broker/inspections/new"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		assertThat(html).contains("빠른 등록", "현장 사진/파일", "나중에 보강", "시설 확인", "내부 전용", "연락 기록");
		assertThat(html).contains("name=\"attachments\"");
		assertThat(html.indexOf("빠른 등록")).isLessThan(html.indexOf("나중에 보강"));
		assertThat(html.indexOf("나중에 보강")).isLessThan(html.indexOf("시설 확인"));
		assertThat(html.indexOf("시설 확인")).isLessThan(html.indexOf("내부 전용"));
		assertThat(html.indexOf("내부 전용")).isLessThan(html.indexOf("연락 기록"));
		assertThat(html.indexOf("name=\"attachments\"")).isLessThan(html.indexOf("나중에 보강"));
		assertThat(html.indexOf("name=\"internalDetailAddress\"")).isGreaterThan(html.indexOf("나중에 보강"));
		assertThat(html.indexOf("name=\"pricePrivateNote\"")).isGreaterThan(html.indexOf("내부 전용"));
		assertThat(html.indexOf("name=\"privateMemo\"")).isGreaterThan(html.indexOf("내부 전용"));
		assertThat(html.indexOf("name=\"internalRiskMemo\"")).isGreaterThan(html.indexOf("내부 전용"));
	}

	@Test
	void newFormUsesKoreanProductLanguageWhileKeepingBindings() throws Exception {
		String html = mockMvc.perform(get("/broker/inspections/new"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		assertThat(html)
				.contains("필수 입력", "빠른 등록", "현장 정보", "임대 조건", "고객 공유용 주소", "확인 상태", "내부 메모")
				.contains("name=\"businessType\"", "name=\"verificationStatus\"", "name=\"attachments\"")
				.contains("카페", "음식점", "미확인", "현장 확인", "서류 확인");
		assertThat(html)
				.doesNotContain(">CAFE<", ">RESTAURANT<", ">UNVERIFIED<", ">AGENT_CHECKED<", ">DOCUMENT_CHECKED<")
				.doesNotContain("빠른 저장", "브로커 매물 입력", "지오코딩", "스냅샷");
	}

	@Test
	void editFormKeepsShareSnapshotActionSeparateFromInspectionSaveForm() throws Exception {
		InspectionForm form = new InspectionForm();
		form.setTitle("공유 준비 상가");
		form.setVerificationStatus(VerificationStatus.UNVERIFIED);
		when(inspectionService.findForm(41L)).thenReturn(form);

		String html = mockMvc.perform(get("/broker/inspections/41/edit"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		assertThat(html).contains("고객 공유 카드 만들기", "/broker/inspections/41/share");
		assertThat(html.indexOf("action=\"/broker/inspections/41\""))
				.isLessThan(html.indexOf("action=\"/broker/inspections/41/share\""));
		assertThat(html.indexOf("type=\"submit\">수정</button>"))
				.isLessThan(html.indexOf("고객 공유 카드 만들기"));
	}

	@Test
	void newFormRendersDisabledKakaoMapBoundaryWithoutSdkSecret() throws Exception {
		mockMvc.perform(get("/broker/inspections/new"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("위치 지도")))
				.andExpect(content().string(containsString("지도 연동은 비활성화되어 있습니다")))
				.andExpect(content().string(org.hamcrest.Matchers.not(containsString("data-kakao-sdk-src"))))
				.andExpect(content().string(org.hamcrest.Matchers.not(containsString("KAKAO_REST_API_KEY"))));
	}

	@Test
	void newFormRendersConfiguredKakaoMapSdkThroughViewBoundary() throws Exception {
		when(kakaoMapViewFactory.brokerInspectionMap()).thenReturn(KakaoMapView.enabled(
				"https://dapi.kakao.com/v2/maps/sdk.js?appkey=browser-key&autoload=false",
				"37.566500",
				"126.978000",
				4));

		mockMvc.perform(get("/broker/inspections/new"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("data-kakao-map")))
				.andExpect(content().string(containsString(
						"data-kakao-sdk-src=\"https://dapi.kakao.com/v2/maps/sdk.js?appkey=browser-key&amp;autoload=false\"")))
				.andExpect(content().string(containsString("data-latitude=\"37.566500\"")))
				.andExpect(content().string(containsString("data-longitude=\"126.978000\"")))
				.andExpect(content().string(containsString("data-level=\"4\"")))
				.andExpect(content().string(org.hamcrest.Matchers.not(containsString("rest-api-key"))));
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
				.andExpect(content().string(containsString("시설 확인")))
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
				.andExpect(content().string(containsString("필수 입력과 숫자 값을 확인해 주세요")))
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
				.andExpect(content().string(containsString("필수 입력과 숫자 값을 확인해 주세요")));
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
	@WithAnonymousUser
	void attachmentUploadRequiresBrokerAuthentication() throws Exception {
		mockMvc.perform(multipart("/broker/inspections")
				.file("attachments", "payload".getBytes())
				.with(csrf()))
				.andExpect(status().isUnauthorized());
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

	@Test
	void createShareSnapshotRedirectsToPublicShareCard() throws Exception {
		when(shareSnapshotService.createSnapshot(41L, "user")).thenReturn(new PublicShareSnapshot(
				"share-41",
				"성수역 1층 상가",
				new com.imjangbox.property.PublicAddress("성수역 인근", "대로변"),
				new PublicPricingSnapshot(10_000L, 550L, 3_000L),
				VerificationStatus.AGENT_CHECKED,
				"Agent checked",
				List.of(),
				List.of()));

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
				.post("/broker/inspections/41/share").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/share/share-41"));

		verify(shareSnapshotService).createSnapshot(41L, "user");
	}

	private List<MultipartFile> anyAttachmentList() {
		return org.mockito.ArgumentMatchers.anyList();
	}
}
