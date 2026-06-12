package com.imjangbox.inspection.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.imjangbox.share.ShareFacilitySnapshotRow;
import com.imjangbox.share.ShareImageSnapshotRow;
import com.imjangbox.share.ShareSnapshotAuditAction;
import com.imjangbox.share.ShareSnapshotAuditRow;
import com.imjangbox.share.ShareSnapshotAuditWriteRow;
import com.imjangbox.share.ShareSnapshotMapper;
import com.imjangbox.share.ShareSnapshotWriteRow;

@MybatisTest
@ActiveProfiles("mybatis-integration")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyBatisPersistenceIntegrationTest {

	@Autowired
	private PropertyInspectionMapper inspectionMapper;

	@Autowired
	private ShareSnapshotMapper shareMapper;

	@Test
	void inspectionMapperWritesReadsFacilityAnswersAttachmentsAndSearchIndex() {
		PropertyInspectionWriteRow row = inspectionRow(null, "성수역 1층 상가", "성수역 인근");

		inspectionMapper.insert(row);

		assertThat(row.inspectionId()).isPositive();
		assertThat(inspectionMapper.findById(row.inspectionId()))
				.get()
				.satisfies(found -> {
					assertThat(found.title()).isEqualTo("성수역 1층 상가");
					assertThat(found.businessType()).isEqualTo("CAFE");
					assertThat(found.internalRoadAddress()).isEqualTo("서울 성동구 내부로 1");
					assertThat(found.publicAddressSummary()).isEqualTo("성수역 인근");
					assertThat(found.privateMemo()).isEqualTo("공유 금지 내부 메모");
					assertThat(found.internalRiskMemo()).isEqualTo("권리금 리스크");
				});

		inspectionMapper.insertContactLog(new ContactLogWriteRow(
				row.inspectionId(), "2026-06-11", "임대인 통화 내용"));
		inspectionMapper.insertFileAttachment(new FileAttachmentWriteRow(
				row.inspectionId(), "floor.png", "inspection-files/1/floor.png", "image/png", 10L));
		inspectionMapper.insertFacilityAnswer(new FacilityAnswerWriteRow(
				row.inspectionId(), "water_supply", "CAFE", "급배수 확인", "OK", true));
		inspectionMapper.insertFacilityAnswer(new FacilityAnswerWriteRow(
				row.inspectionId(), "electric_capacity", "CAFE", "전기 용량 확인", "NEEDS_CHECK", false));

		assertThat(inspectionMapper.findFileAttachments(row.inspectionId()))
				.extracting(FileAttachmentWriteRow::originalFilename, FileAttachmentWriteRow::storageKey)
				.containsExactly(org.assertj.core.groups.Tuple.tuple(
						"floor.png", "inspection-files/1/floor.png"));
		assertThat(inspectionMapper.findAll())
				.extracting(PropertyInspectionRow::inspectionId, PropertyInspectionRow::title, PropertyInspectionRow::publicAddressSummary)
				.contains(org.assertj.core.groups.Tuple.tuple(row.inspectionId(), "성수역 1층 상가", "성수역 인근"));
		assertThat(inspectionMapper.findFacilityAnswers(row.inspectionId()))
				.extracting(
						FacilityAnswerWriteRow::templateItemKey,
						FacilityAnswerWriteRow::answer,
						FacilityAnswerWriteRow::customerVisible)
				.containsExactly(
						org.assertj.core.groups.Tuple.tuple("water_supply", "OK", true),
						org.assertj.core.groups.Tuple.tuple("electric_capacity", "NEEDS_CHECK", false));

		inspectionMapper.upsertSearchIndex(new SearchIndexWriteRow(
				row.inspectionId(),
				"성수역 1층 상가",
				"성수역 인근",
				"대로변",
				"CAFE",
				"AGENT_CHECKED",
				"82.50",
				"성수역 1층 상가 성수역 인근 대로변 CAFE AGENT_CHECKED 82.50"));
		inspectionMapper.upsertSearchIndex(new SearchIndexWriteRow(
				row.inspectionId(),
				"성수역 수정 상가",
				"뚝섬역 인근",
				"코너",
				"CAFE",
				"DOCUMENT_CHECKED",
				"83.00",
				"성수역 수정 상가 뚝섬역 인근 코너 CAFE DOCUMENT_CHECKED 83.00"));

		assertThat(inspectionMapper.findSearchIndexByInspectionId(row.inspectionId()))
				.get()
				.satisfies(index -> {
					assertThat(index.title()).isEqualTo("성수역 수정 상가");
					assertThat(index.publicAddressSummary()).isEqualTo("뚝섬역 인근");
					assertThat(index.verificationStatus()).isEqualTo("DOCUMENT_CHECKED");
					assertThat(index.searchText())
							.contains("뚝섬역 인근", "DOCUMENT_CHECKED")
							.doesNotContain("공유 금지 내부 메모", "권리금 리스크", "임대인 통화 내용");
				});

		inspectionMapper.deleteFacilityAnswers(row.inspectionId());
		assertThat(inspectionMapper.findFacilityAnswers(row.inspectionId())).isEmpty();
	}

	@Test
	void shareMapperWritesSnapshotsChildrenAndAuditLogsAgainstInspectionForeignKey() {
		PropertyInspectionWriteRow inspection = inspectionRow(null, "공유용 상가", "공개 주소");
		inspectionMapper.insert(inspection);

		shareMapper.insertSnapshot(snapshot("share-v1", inspection.inspectionId(), "공유용 상가"));
		shareMapper.insertFacility(new ShareFacilitySnapshotRow("share-v1", 2, "전기", "확인 필요"));
		shareMapper.insertFacility(new ShareFacilitySnapshotRow("share-v1", 1, "급배수", "OK"));
		shareMapper.insertImage(new ShareImageSnapshotRow(
				"share-v1", 1, "image/png", "inspection-files/1/image.png"));
		shareMapper.insertAuditLog(ShareSnapshotAuditWriteRow.of(
				"share-v1",
				inspection.inspectionId(),
				ShareSnapshotAuditAction.CREATED,
				"broker-user"));
		shareMapper.insertSnapshot(snapshot("share-v2", inspection.inspectionId(), "공유용 상가 v2"));
		shareMapper.insertAuditLog(ShareSnapshotAuditWriteRow.of(
				"share-v2",
				inspection.inspectionId(),
				ShareSnapshotAuditAction.UPDATED,
				"broker-user"));

		assertThat(shareMapper.countSnapshotsByInspectionId(inspection.inspectionId())).isEqualTo(2);
		assertThat(shareMapper.findSnapshotByShareId("share-v1"))
				.get()
				.satisfies(snapshot -> {
					assertThat(snapshot.title()).isEqualTo("공유용 상가");
					assertThat(snapshot.publicAddressSummary()).isEqualTo("공개 주소");
					assertThat(snapshot.verificationDisplayText()).isEqualTo("Agent checked");
				});
		assertThat(shareMapper.findFacilitiesByShareId("share-v1"))
				.extracting(ShareFacilitySnapshotRow::displayOrder, ShareFacilitySnapshotRow::label)
				.containsExactly(
						org.assertj.core.groups.Tuple.tuple(1, "급배수"),
						org.assertj.core.groups.Tuple.tuple(2, "전기"));
		assertThat(shareMapper.findImageByShareIdAndDisplayOrder("share-v1", 1))
				.get()
				.satisfies(image -> {
					assertThat(image.contentType()).isEqualTo("image/png");
					assertThat(image.sourceStorageKey()).isEqualTo("inspection-files/1/image.png");
				});
		assertThat(shareMapper.findAuditLogsByShareId("share-v1"))
				.extracting(
						ShareSnapshotAuditRow::inspectionId,
						ShareSnapshotAuditRow::action,
						ShareSnapshotAuditRow::actorUsername)
				.containsExactly(org.assertj.core.groups.Tuple.tuple(
						inspection.inspectionId(),
						ShareSnapshotAuditAction.CREATED.name(),
						"broker-user"));
		assertThat(shareMapper.findAuditLogsByShareId("share-v2"))
				.extracting(ShareSnapshotAuditRow::action)
				.containsExactly(ShareSnapshotAuditAction.UPDATED.name());
	}

	private PropertyInspectionWriteRow inspectionRow(Long inspectionId, String title, String publicAddressSummary) {
		return new PropertyInspectionWriteRow.Builder()
				.inspectionId(inspectionId)
				.title(title)
				.businessType("CAFE")
				.verificationStatus("AGENT_CHECKED")
				.internalRoadAddress("서울 성동구 내부로 1")
				.internalDetailAddress("101호")
				.internalGeocodeMemo("좌표 확인")
				.publicAddressSummary(publicAddressSummary)
				.publicLandmarkHint("대로변")
				.depositAmount(10_000L)
				.monthlyRentAmount(550L)
				.premiumAmount(3_000L)
				.areaSquareMeters("82.50")
				.businessFitMemo("카페 가능")
				.conditionMemo("급배수 확인")
				.pricePrivateNote("협상 여지")
				.privateMemo("공유 금지 내부 메모")
				.internalRiskMemo("권리금 리스크")
				.build();
	}

	private ShareSnapshotWriteRow snapshot(String shareId, long inspectionId, String title) {
		return new ShareSnapshotWriteRow(
				shareId,
				inspectionId,
				title,
				"AGENT_CHECKED",
				"Agent checked",
				"공개 주소",
				"대로변",
				10_000L,
				550L,
				3_000L);
	}
}
