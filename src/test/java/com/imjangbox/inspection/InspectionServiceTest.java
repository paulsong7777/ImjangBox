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
import com.imjangbox.inspection.persistence.PropertyInspectionMapper;
import com.imjangbox.inspection.persistence.PropertyInspectionWriteRow;
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
		return form;
	}

	private static final class CapturingPropertyInspectionMapper implements PropertyInspectionMapper {

		private PropertyInspectionWriteRow inserted;
		private PropertyInspectionWriteRow updated;
		private final List<ContactLogWriteRow> contactLogs = new ArrayList<>();
		private final List<FileAttachmentWriteRow> attachments = new ArrayList<>();
		private final List<Long> deletedContactLogInspectionIds = new ArrayList<>();
		private int updateResult = 1;

		@Override
		public Optional<com.imjangbox.inspection.persistence.PropertyInspectionRow> findById(long inspectionId) {
			return Optional.empty();
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
