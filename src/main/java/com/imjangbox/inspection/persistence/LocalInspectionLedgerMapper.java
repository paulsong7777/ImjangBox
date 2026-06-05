package com.imjangbox.inspection.persistence;

import java.util.LinkedHashMap;
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
	}

	@Override
	public void insertContactLog(ContactLogWriteRow row) {
	}

	@Override
	public void insertFileAttachment(FileAttachmentWriteRow row) {
	}

	private PropertyInspectionRow toReadRow(PropertyInspectionWriteRow row) {
		return new PropertyInspectionRow(
				row.inspectionId(),
				row.title(),
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
				row.internalRiskMemo());
	}
}
