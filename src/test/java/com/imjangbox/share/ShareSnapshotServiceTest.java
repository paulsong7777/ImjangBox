package com.imjangbox.share;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjangbox.file.FileStorage;
import com.imjangbox.inspection.persistence.ContactLogWriteRow;
import com.imjangbox.inspection.persistence.FacilityAnswerWriteRow;
import com.imjangbox.inspection.persistence.FileAttachmentWriteRow;
import com.imjangbox.inspection.persistence.PropertyInspectionMapper;
import com.imjangbox.inspection.persistence.PropertyInspectionRow;
import com.imjangbox.inspection.persistence.PropertyInspectionWriteRow;
import com.imjangbox.inspection.persistence.SearchIndexWriteRow;
import com.imjangbox.property.VerificationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ShareSnapshotServiceTest {

	private static final List<String> DENIED_VALUES = List.of(
			"UPDATED_PUBLIC_TITLE",
			"UPDATED_PUBLIC_ADDRESS",
			"PRIVATE_MEMO_VALUE",
			"PRICE_PRIVATE_NOTE_VALUE",
			"CONTACT_LOG_CONTENT_VALUE",
			"INTERNAL_RISK_MEMO_VALUE",
			"INTERNAL_EXACT_ADDRESS_VALUE",
			"PRIVATE_FACILITY_ANSWER_VALUE",
			"PRIVATE_STORAGE_KEY",
			"PRIVATE_FILE_NAME.png");

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void createsSnapshotFromAllowlistedFieldsAndKeepsItStableAfterInternalUpdate() throws Exception {
		CapturingInspectionMapper inspectionMapper = new CapturingInspectionMapper();
		LocalShareSnapshotMapper shareMapper = new LocalShareSnapshotMapper();
		ShareSnapshotService service = new ShareSnapshotService(
				inspectionMapper,
				shareMapper,
				noOpFileStorage(),
				() -> "share-stable");

		PublicShareSnapshot created = service.createSnapshot(41L);
		inspectionMapper.row = row("UPDATED_PUBLIC_TITLE", "UPDATED_PUBLIC_ADDRESS", VerificationStatus.DOCUMENT_CHECKED);
		PublicShareSnapshot found = service.findSnapshot(created.shareId());

		assertThat(found.title()).isEqualTo("Customer-safe title");
		assertThat(found.publicAddress().summary()).isEqualTo("PUBLIC_DISTRICT_VALUE");
		assertThat(found.verificationDisplayText()).isEqualTo("Agent checked");
		assertThat(found.facilities())
				.extracting(PublicFacilitySnapshot::label, PublicFacilitySnapshot::answer)
				.containsExactly(org.assertj.core.groups.Tuple.tuple("Customer-visible water", "OK"));
		assertThat(found.images())
				.extracting(PublicImageSnapshot::imageUrl, PublicImageSnapshot::altText)
				.containsExactly(org.assertj.core.groups.Tuple.tuple(
						"/share/share-stable/images/1", "Property image 1"));

		String json = objectMapper.writeValueAsString(found);
		assertThat(json)
				.contains("Customer-safe title", "PUBLIC_DISTRICT_VALUE", "OK", "/share/share-stable/images/1")
				.doesNotContain(DENIED_VALUES.toArray(String[]::new))
				.doesNotContain("internalRoadAddress")
				.doesNotContain("privateMemo")
				.doesNotContain("pricePrivateNote")
				.doesNotContain("internalRiskMemo")
				.doesNotContain("contactLogs")
				.doesNotContain("storageKey")
				.doesNotContain("inspectionId");
	}

	@ParameterizedTest
	@EnumSource(VerificationStatus.class)
	void snapshotsUseCustomerSafeVerificationLabelForEveryStatus(VerificationStatus status) {
		CapturingInspectionMapper inspectionMapper = new CapturingInspectionMapper();
		inspectionMapper.row = row("Customer-safe title", "PUBLIC_DISTRICT_VALUE", status);
			ShareSnapshotService service = new ShareSnapshotService(
					inspectionMapper,
					new LocalShareSnapshotMapper(),
					noOpFileStorage(),
					() -> "share-" + status.name().toLowerCase());

		PublicShareSnapshot snapshot = service.createSnapshot(41L);

		assertThat(snapshot.verificationStatus()).isEqualTo(status);
		assertThat(snapshot.verificationDisplayText()).isEqualTo(status.customerDisplayText());
	}

	private static PropertyInspectionRow row(String title, String publicAddress, VerificationStatus status) {
		return new PropertyInspectionRow(
				41L,
				title,
				"CAFE",
				status.name(),
				"INTERNAL_EXACT_ADDRESS_VALUE",
				"101",
				"private geocode memo",
				publicAddress,
				"near station",
				100_000_000L,
				6_000_000L,
				30_000_000L,
				"82.50",
				"safe fit memo",
				"safe condition memo",
				"PRICE_PRIVATE_NOTE_VALUE",
				"PRIVATE_MEMO_VALUE",
				"INTERNAL_RISK_MEMO_VALUE",
				List.of(new ContactLogWriteRow(41L, "2026-06-11", "CONTACT_LOG_CONTENT_VALUE")),
				List.of(new FileAttachmentWriteRow(
						41L,
						"PRIVATE_FILE_NAME.png",
						"PRIVATE_STORAGE_KEY",
						"image/png",
						10L)));
	}

	private static FileStorage noOpFileStorage() {
		return (inspectionId, file) -> null;
	}

	private static final class CapturingInspectionMapper implements PropertyInspectionMapper {

		private PropertyInspectionRow row = row(
				"Customer-safe title",
				"PUBLIC_DISTRICT_VALUE",
				VerificationStatus.AGENT_CHECKED);
		private final List<FacilityAnswerWriteRow> facilityAnswers = new ArrayList<>(List.of(
				new FacilityAnswerWriteRow(41L, "water_supply", "CAFE", "Customer-visible water", "OK", true),
				new FacilityAnswerWriteRow(
						41L,
						"private_power",
						"CAFE",
						"Private power note",
						"PRIVATE_FACILITY_ANSWER_VALUE",
						false)));
		private final List<FileAttachmentWriteRow> attachments = new ArrayList<>(List.of(
				new FileAttachmentWriteRow(41L, "PRIVATE_FILE_NAME.png", "PRIVATE_STORAGE_KEY", "image/png", 10L),
				new FileAttachmentWriteRow(41L, "memo.txt", "inspection-files/41/memo.txt", "text/plain", 4L)));

		@Override
		public Optional<PropertyInspectionRow> findById(long inspectionId) {
			return Optional.of(row);
		}

		@Override
		public void insert(PropertyInspectionWriteRow row) {
		}

		@Override
		public int update(PropertyInspectionWriteRow row) {
			return 1;
		}

		@Override
		public void deleteContactLogs(long inspectionId) {
		}

		@Override
		public void insertContactLog(ContactLogWriteRow row) {
		}

		@Override
		public void insertFileAttachment(FileAttachmentWriteRow row) {
		}

		@Override
		public List<FileAttachmentWriteRow> findFileAttachments(long inspectionId) {
			return List.copyOf(attachments);
		}

		@Override
		public void deleteFacilityAnswers(long inspectionId) {
		}

		@Override
		public void insertFacilityAnswer(FacilityAnswerWriteRow row) {
		}

		@Override
		public List<FacilityAnswerWriteRow> findFacilityAnswers(long inspectionId) {
			return List.copyOf(facilityAnswers);
		}

		@Override
		public void upsertSearchIndex(SearchIndexWriteRow row) {
		}

		@Override
		public Optional<SearchIndexWriteRow> findSearchIndexByInspectionId(long inspectionId) {
			return Optional.empty();
		}
	}
}
