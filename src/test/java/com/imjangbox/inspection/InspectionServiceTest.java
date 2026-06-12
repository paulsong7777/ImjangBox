package com.imjangbox.inspection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.imjangbox.inspection.AttachmentValidationException;
import com.imjangbox.file.FileStorage;
import com.imjangbox.file.StoredFile;
import com.imjangbox.inspection.persistence.ContactLogWriteRow;
import com.imjangbox.inspection.persistence.FileAttachmentWriteRow;
import com.imjangbox.inspection.persistence.FacilityAnswerWriteRow;
import com.imjangbox.inspection.persistence.PropertyInspectionMapper;
import com.imjangbox.inspection.persistence.PropertyInspectionRow;
import com.imjangbox.inspection.persistence.PropertyInspectionWriteRow;
import com.imjangbox.inspection.persistence.SearchIndexWriteRow;
import com.imjangbox.inspection.web.FacilityAnswerForm;
import com.imjangbox.inspection.web.InspectionForm;
import com.imjangbox.property.VerificationStatus;

class InspectionServiceTest {

	@Test
	void createPersistsInternalFieldsContactLogAndAttachmentMetadata() throws IOException {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		CapturingFileStorage fileStorage = new CapturingFileStorage();
		InspectionService service = new InspectionService(mapper, fileStorage);

		long inspectionId = service.create(form(), List.of(new MockMultipartFile(
				"attachments", "floor-plan.txt", "text/plain", "floor-plan".getBytes())));

		assertThat(inspectionId).isEqualTo(41L);
		assertThat(mapper.inserted.title()).isEqualTo("성수역 1층 상가");
		assertThat(mapper.inserted.businessType()).isEqualTo("CAFE");
		assertThat(mapper.inserted.internalRoadAddress()).isEqualTo("서울 성동구 내부로 1");
		assertThat(mapper.inserted.publicAddressSummary()).isEqualTo("성수역 인근");
		assertThat(mapper.inserted.businessFitMemo()).isEqualTo("카페 가능성 높음");
		assertThat(mapper.inserted.conditionMemo()).isEqualTo("급배수 확인 필요");
		assertThat(mapper.inserted.privateMemo()).isEqualTo("임대인 통화 전까지 공유 금지");
		assertThat(mapper.inserted.internalRiskMemo()).isEqualTo("권리금 조정 리스크");
		assertThat(mapper.contactLogs)
				.extracting(ContactLogWriteRow::content)
				.containsExactly("임대인과 권리금 범위 통화");
		assertThat(fileStorage.storedOriginalNames).containsExactly("floor-plan.txt");
		assertThat(mapper.attachments)
				.extracting(FileAttachmentWriteRow::storageKey)
				.containsExactly("inspection-files/41/floor-plan.txt");
		assertThat(mapper.facilityAnswers)
				.extracting(FacilityAnswerWriteRow::templateItemKey, FacilityAnswerWriteRow::answer)
				.containsExactly(org.assertj.core.groups.Tuple.tuple("water_supply", "OK"));
		assertThat(mapper.searchIndex.inspectionId()).isEqualTo(41L);
		assertThat(mapper.searchIndex.searchText())
				.contains("성수역 1층 상가", "성수역 인근", "대로변", "CAFE", "AGENT_CHECKED")
				.doesNotContain("임대인 통화 전까지 공유 금지")
				.doesNotContain("권리금 조정 리스크")
				.doesNotContain("임대인과 권리금 범위 통화");
	}

	@Test
	void updatePreservesExistingContactLogsAndPersistsNewInternalChildren() throws IOException {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		CapturingFileStorage fileStorage = new CapturingFileStorage();
		InspectionService service = new InspectionService(mapper, fileStorage);

		service.update(77L, form(), List.of(new MockMultipartFile(
				"attachments", "memo.txt", "text/plain", "memo".getBytes())));

		assertThat(mapper.updated.inspectionId()).isEqualTo(77L);
		assertThat(mapper.deletedContactLogInspectionIds).isEmpty();
		assertThat(mapper.contactLogs)
				.extracting(ContactLogWriteRow::inspectionId, ContactLogWriteRow::content)
				.containsExactly(org.assertj.core.groups.Tuple.tuple(77L, "임대인과 권리금 범위 통화"));
		assertThat(mapper.attachments)
				.extracting(FileAttachmentWriteRow::inspectionId, FileAttachmentWriteRow::originalFilename)
				.containsExactly(org.assertj.core.groups.Tuple.tuple(77L, "memo.txt"));
		assertThat(mapper.deletedFacilityAnswerInspectionIds).containsExactly(77L);
		assertThat(mapper.facilityAnswers)
				.extracting(FacilityAnswerWriteRow::inspectionId, FacilityAnswerWriteRow::templateItemKey,
						FacilityAnswerWriteRow::answer)
				.containsExactly(org.assertj.core.groups.Tuple.tuple(77L, "water_supply", "OK"));
		assertThat(mapper.searchIndex.inspectionId()).isEqualTo(77L);
		assertThat(mapper.searchIndex.publicAddressSummary()).isEqualTo("성수역 인근");
		assertThat(mapper.searchIndex.searchText())
				.contains("성수역 1층 상가", "82.50")
				.doesNotContain("권리금 협상 여지")
				.doesNotContain("권리금 조정 리스크");
	}

	@Test
	void findFormLoadsSavedFacilityAnswers() {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		mapper.foundRow = Optional.of(row(77L));
		mapper.facilityAnswers.add(new FacilityAnswerWriteRow(
				77L, "water_supply", "CAFE", "급배수 확인", "OK", true));
		InspectionService service = new InspectionService(mapper, new CapturingFileStorage());

		InspectionForm form = service.findForm(77L);

		assertThat(form.getBusinessType()).isEqualTo("CAFE");
		assertThat(form.getFacilityAnswers())
				.extracting(FacilityAnswerForm::getTemplateItemKey, FacilityAnswerForm::getAnswer)
				.containsExactly(org.assertj.core.groups.Tuple.tuple("water_supply", "OK"));
	}

	@Test
	void findDashboardItemsUsesPublicCardFieldsAndImagePresenceOnly() {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		mapper.dashboardRows.add(row(77L));
		mapper.attachments.add(new FileAttachmentWriteRow(
				77L, "private-name.png", "inspection-files/77/private-name.png", "image/png", 10L));
		InspectionService service = new InspectionService(mapper, new CapturingFileStorage());

		assertThat(service.findDashboardItems())
				.extracting(
						PropertyDashboardItem::title,
						PropertyDashboardItem::publicAddressSummary,
						PropertyDashboardItem::depositAmount,
						PropertyDashboardItem::monthlyRentAmount,
						PropertyDashboardItem::premiumAmount,
						PropertyDashboardItem::areaSquareMeters,
						PropertyDashboardItem::hasImageAttachment)
				.containsExactly(org.assertj.core.groups.Tuple.tuple(
						"성수역 1층 상가",
						"성수역 인근",
						10_000L,
						550L,
						3_000L,
						"82.50",
						true));
	}

	@Test
	void updateRejectsMissingInspectionId() {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		mapper.updateResult = 0;
		InspectionService service = new InspectionService(mapper, new CapturingFileStorage());

		assertThatThrownBy(() -> service.update(77L, form(), List.of()))
				.isInstanceOf(InspectionNotFoundException.class);
	}

	@Test
	void createRejectsUnsupportedAttachmentTypesBeforeStorage() {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		CapturingFileStorage fileStorage = new CapturingFileStorage();
		InspectionService service = new InspectionService(mapper, fileStorage);

		assertThatThrownBy(() -> service.create(form(), List.of(new MockMultipartFile(
				"attachments", "script.exe", "application/x-msdownload", "x".getBytes()))))
				.isInstanceOf(AttachmentValidationException.class);

		assertThat(fileStorage.storedOriginalNames).isEmpty();
	}

	@Test
	void createRejectsAttachmentContentThatDoesNotMatchDeclaredType() {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		CapturingFileStorage fileStorage = new CapturingFileStorage();
		InspectionService service = new InspectionService(mapper, fileStorage);

		assertThatThrownBy(() -> service.create(form(), List.of(new MockMultipartFile(
				"attachments", "fake.png", "image/png", "not a png".getBytes()))))
				.isInstanceOf(AttachmentValidationException.class);

		assertThat(fileStorage.storedOriginalNames).isEmpty();
	}

	@Test
	void createRejectsAttachmentFilenameExtensionThatDoesNotMatchDeclaredType() {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		CapturingFileStorage fileStorage = new CapturingFileStorage();
		InspectionService service = new InspectionService(mapper, fileStorage);

		assertThatThrownBy(() -> service.create(form(), List.of(new MockMultipartFile(
				"attachments",
				"renamed.txt",
				"image/png",
				new byte[] { (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A }))))
				.isInstanceOf(AttachmentValidationException.class);

		assertThat(fileStorage.storedOriginalNames).isEmpty();
	}

	@Test
	void createRejectsOversizedAttachmentsBeforeStorage() {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		CapturingFileStorage fileStorage = new CapturingFileStorage();
		InspectionService service = new InspectionService(mapper, fileStorage);

		assertThatThrownBy(() -> service.create(form(), List.of(new MockMultipartFile(
				"attachments",
				"large.txt",
				"text/plain",
				new byte[(10 * 1024 * 1024) + 1]))))
				.isInstanceOf(AttachmentValidationException.class);

		assertThat(fileStorage.storedOriginalNames).isEmpty();
	}

	@Test
	void createRejectsTooManyAttachmentsBeforeStorage() {
		CapturingPropertyInspectionMapper mapper = new CapturingPropertyInspectionMapper();
		CapturingFileStorage fileStorage = new CapturingFileStorage();
		InspectionService service = new InspectionService(mapper, fileStorage);
		List<MockMultipartFile> attachments = java.util.stream.IntStream.rangeClosed(1, 6)
				.mapToObj(index -> new MockMultipartFile(
						"attachments", "file-" + index + ".txt", "text/plain", "x".getBytes()))
				.toList();

		assertThatThrownBy(() -> service.create(form(), List.copyOf(attachments)))
				.isInstanceOf(AttachmentValidationException.class);

		assertThat(fileStorage.storedOriginalNames).isEmpty();
	}

	private InspectionForm form() {
		InspectionForm form = new InspectionForm();
		form.setTitle("성수역 1층 상가");
		form.setBusinessType("CAFE");
		form.setInternalRoadAddress("서울 성동구 내부로 1");
		form.setInternalDetailAddress("101호");
		form.setInternalGeocodeMemo("현장 좌표 재확인");
		form.setPublicAddressSummary("성수역 인근");
		form.setPublicLandmarkHint("대로변");
		form.setDepositAmount(10_000L);
		form.setMonthlyRentAmount(550L);
		form.setPremiumAmount(3_000L);
		form.setAreaSquareMeters("82.50");
		form.setBusinessFitMemo("카페 가능성 높음");
		form.setConditionMemo("급배수 확인 필요");
		form.setPricePrivateNote("권리금 협상 여지");
		form.setPrivateMemo("임대인 통화 전까지 공유 금지");
		form.setInternalRiskMemo("권리금 조정 리스크");
		form.setVerificationStatus(VerificationStatus.AGENT_CHECKED);
		form.setContactedOn("2026-06-05");
		form.setContactLogContent("임대인과 권리금 범위 통화");
		FacilityAnswerForm facilityAnswer = new FacilityAnswerForm();
		facilityAnswer.setBusinessType("CAFE");
		facilityAnswer.setTemplateItemKey("water_supply");
		facilityAnswer.setLabel("급배수 확인");
		facilityAnswer.setAnswer("OK");
		facilityAnswer.setCustomerVisible(true);
		form.setFacilityAnswers(List.of(facilityAnswer));
		return form;
	}

	private PropertyInspectionRow row(long inspectionId) {
		return new PropertyInspectionRow(
				inspectionId,
				"성수역 1층 상가",
				"CAFE",
				"AGENT_CHECKED",
				"서울 성동구 내부로 1",
				"101호",
				"현장 좌표 재확인",
				"성수역 인근",
				"대로변",
				10_000L,
				550L,
				3_000L,
				"82.50",
				"카페 가능성 높음",
				"급배수 확인 필요",
				"권리금 협상 여지",
				"임대인 통화 전까지 공유 금지",
				"권리금 조정 리스크");
	}

	private static final class CapturingPropertyInspectionMapper implements PropertyInspectionMapper {

		private PropertyInspectionWriteRow inserted;
		private PropertyInspectionWriteRow updated;
		private Optional<PropertyInspectionRow> foundRow = Optional.empty();
		private final List<PropertyInspectionRow> dashboardRows = new ArrayList<>();
		private final List<ContactLogWriteRow> contactLogs = new ArrayList<>();
		private final List<FileAttachmentWriteRow> attachments = new ArrayList<>();
		private final List<FacilityAnswerWriteRow> facilityAnswers = new ArrayList<>();
		private final List<Long> deletedContactLogInspectionIds = new ArrayList<>();
		private final List<Long> deletedFacilityAnswerInspectionIds = new ArrayList<>();
		private SearchIndexWriteRow searchIndex;
		private int updateResult = 1;

		@Override
		public List<PropertyInspectionRow> findAll() {
			return List.copyOf(dashboardRows);
		}

		@Override
		public Optional<PropertyInspectionRow> findById(long inspectionId) {
			return foundRow;
		}

		@Override
		public void insert(PropertyInspectionWriteRow row) {
			row.setInspectionId(41L);
			inserted = row;
		}

		@Override
		public int update(PropertyInspectionWriteRow row) {
			updated = row;
			return updateResult;
		}

		@Override
		public void deleteContactLogs(long inspectionId) {
			deletedContactLogInspectionIds.add(inspectionId);
		}

		@Override
		public void insertContactLog(ContactLogWriteRow row) {
			contactLogs.add(row);
		}

		@Override
		public void insertFileAttachment(FileAttachmentWriteRow row) {
			attachments.add(row);
		}

		@Override
		public List<FileAttachmentWriteRow> findFileAttachments(long inspectionId) {
			return List.copyOf(attachments);
		}

		@Override
		public void deleteFacilityAnswers(long inspectionId) {
			deletedFacilityAnswerInspectionIds.add(inspectionId);
		}

		@Override
		public void insertFacilityAnswer(FacilityAnswerWriteRow row) {
			facilityAnswers.add(row);
		}

		@Override
		public List<FacilityAnswerWriteRow> findFacilityAnswers(long inspectionId) {
			return List.copyOf(facilityAnswers);
		}

		@Override
		public void upsertSearchIndex(SearchIndexWriteRow row) {
			searchIndex = row;
		}

		@Override
		public Optional<SearchIndexWriteRow> findSearchIndexByInspectionId(long inspectionId) {
			return Optional.ofNullable(searchIndex);
		}
	}

	private static final class CapturingFileStorage implements FileStorage {

		private final List<String> storedOriginalNames = new ArrayList<>();

		@Override
		public StoredFile store(long inspectionId, org.springframework.web.multipart.MultipartFile file)
				throws IOException {
			storedOriginalNames.add(file.getOriginalFilename());
			return new StoredFile(
					"inspection-files/" + inspectionId + "/" + file.getOriginalFilename(),
					file.getOriginalFilename(),
					file.getContentType(),
					file.getSize());
		}
	}
}
