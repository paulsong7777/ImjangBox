package com.imjangbox.inspection.persistence;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local & !local-db")
class LocalInspectionLedgerMapper implements PropertyInspectionMapper {

	private final AtomicLong sequence = new AtomicLong(40);
	private final Map<Long, PropertyInspectionWriteRow> inspections = new LinkedHashMap<>();
	private final Map<Long, List<ContactLogWriteRow>> contactLogs = new LinkedHashMap<>();
	private final Map<Long, List<FileAttachmentWriteRow>> fileAttachments = new LinkedHashMap<>();
	private final Map<Long, List<FacilityAnswerWriteRow>> facilityAnswers = new LinkedHashMap<>();
	private final Map<Long, SearchIndexWriteRow> searchIndex = new LinkedHashMap<>();

	@Override
	public Optional<PropertyInspectionRow> findById(long inspectionId) {
		return Optional.ofNullable(inspections.get(inspectionId)).map(this::toReadRow);
	}

	@Override
	public void insert(PropertyInspectionWriteRow row) {
		row.setInspectionId(sequence.incrementAndGet());
		inspections.put(row.inspectionId(), row);
	}

	@Override
	public int update(PropertyInspectionWriteRow row) {
		if (!inspections.containsKey(row.inspectionId())) {
			return 0;
		}
		inspections.put(row.inspectionId(), row);
		return 1;
	}

	@Override
	public void deleteContactLogs(long inspectionId) {
		contactLogs.remove(inspectionId);
	}

	@Override
	public void insertContactLog(ContactLogWriteRow row) {
		contactLogs.computeIfAbsent(row.inspectionId(), ignored -> new ArrayList<>()).add(row);
	}

	@Override
	public void insertFileAttachment(FileAttachmentWriteRow row) {
		fileAttachments.computeIfAbsent(row.inspectionId(), ignored -> new ArrayList<>()).add(row);
	}

	@Override
	public List<FileAttachmentWriteRow> findFileAttachments(long inspectionId) {
		return List.copyOf(fileAttachments.getOrDefault(inspectionId, List.of()));
	}

	@Override
	public void deleteFacilityAnswers(long inspectionId) {
		facilityAnswers.remove(inspectionId);
	}

	@Override
	public void insertFacilityAnswer(FacilityAnswerWriteRow row) {
		facilityAnswers.computeIfAbsent(row.inspectionId(), ignored -> new ArrayList<>()).add(row);
	}

	@Override
	public List<FacilityAnswerWriteRow> findFacilityAnswers(long inspectionId) {
		return List.copyOf(facilityAnswers.getOrDefault(inspectionId, List.of()));
	}

	@Override
	public void upsertSearchIndex(SearchIndexWriteRow row) {
		searchIndex.put(row.inspectionId(), row);
	}

	@Override
	public Optional<SearchIndexWriteRow> findSearchIndexByInspectionId(long inspectionId) {
		return Optional.ofNullable(searchIndex.get(inspectionId));
	}

	private PropertyInspectionRow toReadRow(PropertyInspectionWriteRow row) {
		return new PropertyInspectionRow(
				row.inspectionId(),
				row.title(),
				row.businessType(),
				row.verificationStatus(),
				row.internalRoadAddress(),
				row.internalDetailAddress(),
				row.internalGeocodeMemo(),
				row.publicAddressSummary(),
				row.publicLandmarkHint(),
				row.depositAmount(),
				row.monthlyRentAmount(),
				row.premiumAmount(),
				row.areaSquareMeters(),
				row.businessFitMemo(),
				row.conditionMemo(),
				row.pricePrivateNote(),
				row.privateMemo(),
				row.internalRiskMemo(),
				contactLogs.getOrDefault(row.inspectionId(), List.of()),
				fileAttachments.getOrDefault(row.inspectionId(), List.of()));
	}
}
