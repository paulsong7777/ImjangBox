package com.imjangbox.inspection.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class LocalInspectionLedgerMapperTest {

	@Test
	void storesContactLogsAndAttachmentMetadataForDefaultLocalProfile() {
		LocalInspectionLedgerMapper mapper = new LocalInspectionLedgerMapper();
		PropertyInspectionWriteRow row = writeRow(null, "성수역 1층 상가");

		mapper.insert(row);
		mapper.insertContactLog(new ContactLogWriteRow(row.inspectionId(), "2026-06-05", "임대인 통화"));
		mapper.insertFileAttachment(new FileAttachmentWriteRow(
				row.inspectionId(), "memo.txt", "inspection-files/41/memo.txt", "text/plain", 4L));
		mapper.insertFacilityAnswer(new FacilityAnswerWriteRow(
				row.inspectionId(), "water_supply", "CAFE", "급배수 확인", "OK", true));

		Optional<PropertyInspectionRow> found = mapper.findById(row.inspectionId());

		assertThat(found).isPresent();
		assertThat(found.orElseThrow().contactLogs())
				.extracting(ContactLogWriteRow::content)
				.containsExactly("임대인 통화");
		assertThat(found.orElseThrow().fileAttachments())
				.extracting(FileAttachmentWriteRow::originalFilename, FileAttachmentWriteRow::storageKey)
				.containsExactly(org.assertj.core.groups.Tuple.tuple(
						"memo.txt", "inspection-files/41/memo.txt"));
		assertThat(mapper.findFacilityAnswers(row.inspectionId()))
				.extracting(FacilityAnswerWriteRow::templateItemKey, FacilityAnswerWriteRow::answer)
				.containsExactly(org.assertj.core.groups.Tuple.tuple("water_supply", "OK"));
	}

	@Test
	void updateIsAppendOnlyForContactLogsUntilExplicitDeletion() {
		LocalInspectionLedgerMapper mapper = new LocalInspectionLedgerMapper();
		PropertyInspectionWriteRow row = writeRow(77L, "초기 제목");
		mapper.insert(row);
		mapper.insertContactLog(new ContactLogWriteRow(row.inspectionId(), "2026-06-05", "첫 통화"));

		mapper.update(writeRow(row.inspectionId(), "수정 제목"));
		mapper.insertContactLog(new ContactLogWriteRow(row.inspectionId(), "2026-06-06", "추가 통화"));

		PropertyInspectionRow found = mapper.findById(row.inspectionId()).orElseThrow();
		assertThat(found.title()).isEqualTo("수정 제목");
		assertThat(found.contactLogs())
				.extracting(ContactLogWriteRow::content)
				.containsExactly("첫 통화", "추가 통화");

		mapper.deleteContactLogs(row.inspectionId());

		assertThat(mapper.findById(row.inspectionId()).orElseThrow().contactLogs()).isEmpty();
	}

	@Test
	void facilityAnswerUpdateReplacesPreviousAnswersUntilExplicitlyChanged() {
		LocalInspectionLedgerMapper mapper = new LocalInspectionLedgerMapper();
		PropertyInspectionWriteRow row = writeRow(null, "성수역 1층 상가");
		mapper.insert(row);
		mapper.insertFacilityAnswer(new FacilityAnswerWriteRow(
				row.inspectionId(), "water_supply", "CAFE", "급배수 확인", "OK", true));

		mapper.deleteFacilityAnswers(row.inspectionId());
		mapper.insertFacilityAnswer(new FacilityAnswerWriteRow(
				row.inspectionId(), "electric_capacity", "CAFE", "전기 용량 확인", "NEEDS_CHECK", false));

		assertThat(mapper.findFacilityAnswers(row.inspectionId()))
				.extracting(FacilityAnswerWriteRow::templateItemKey, FacilityAnswerWriteRow::answer)
				.containsExactly(org.assertj.core.groups.Tuple.tuple("electric_capacity", "NEEDS_CHECK"));
	}

	private PropertyInspectionWriteRow writeRow(Long inspectionId, String title) {
		return new PropertyInspectionWriteRow.Builder()
				.inspectionId(inspectionId)
				.title(title)
				.businessType("CAFE")
				.verificationStatus("AGENT_CHECKED")
				.internalRoadAddress("서울 성동구 내부로 1")
				.internalDetailAddress("101호")
				.internalGeocodeMemo("좌표 확인")
				.publicAddressSummary("성수역 인근")
				.publicLandmarkHint("대로변")
				.depositAmount(10_000L)
				.monthlyRentAmount(550L)
				.premiumAmount(3_000L)
				.areaSquareMeters("82.50")
				.businessFitMemo("카페 가능")
				.conditionMemo("급배수 확인")
				.pricePrivateNote("협상 여지")
				.privateMemo("공유 금지")
				.internalRiskMemo("권리금 리스크")
				.build();
	}
}
