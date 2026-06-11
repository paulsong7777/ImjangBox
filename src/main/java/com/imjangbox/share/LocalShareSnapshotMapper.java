package com.imjangbox.share;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local & !local-db")
class LocalShareSnapshotMapper implements ShareSnapshotMapper {

	private final Map<String, ShareSnapshotRow> snapshots = new LinkedHashMap<>();
	private final Map<String, List<ShareFacilitySnapshotRow>> facilities = new LinkedHashMap<>();
	private final Map<String, List<ShareImageSnapshotRow>> images = new LinkedHashMap<>();

	@Override
	public void insertSnapshot(ShareSnapshotWriteRow row) {
		snapshots.put(row.shareId(), new ShareSnapshotRow(
				row.shareId(),
				row.title(),
				row.verificationStatus(),
				row.verificationDisplayText(),
				row.publicAddressSummary(),
				row.publicLandmarkHint(),
				row.depositAmount(),
				row.monthlyRentAmount(),
				row.premiumAmount()));
	}

	@Override
	public void insertFacility(ShareFacilitySnapshotRow row) {
		facilities.computeIfAbsent(row.shareId(), ignored -> new ArrayList<>()).add(row);
	}

	@Override
	public void insertImage(ShareImageSnapshotRow row) {
		images.computeIfAbsent(row.shareId(), ignored -> new ArrayList<>()).add(row);
	}

	@Override
	public Optional<ShareSnapshotRow> findSnapshotByShareId(String shareId) {
		return Optional.ofNullable(snapshots.get(shareId));
	}

	@Override
	public List<ShareFacilitySnapshotRow> findFacilitiesByShareId(String shareId) {
		return sortedCopy(facilities.getOrDefault(shareId, List.of()));
	}

	@Override
	public List<ShareImageSnapshotRow> findImagesByShareId(String shareId) {
		return sortedCopy(images.getOrDefault(shareId, List.of()));
	}

	@Override
	public Optional<ShareImageSnapshotRow> findImageByShareIdAndDisplayOrder(String shareId, int displayOrder) {
		return images.getOrDefault(shareId, List.of()).stream()
				.filter(image -> image.displayOrder() == displayOrder)
				.findFirst();
	}

	private <T extends Record> List<T> sortedCopy(List<T> rows) {
		return rows.stream()
				.sorted(Comparator.comparingInt(this::displayOrder))
				.toList();
	}

	private int displayOrder(Record row) {
		if (row instanceof ShareFacilitySnapshotRow facility) {
			return facility.displayOrder();
		}
		if (row instanceof ShareImageSnapshotRow image) {
			return image.displayOrder();
		}
		return 0;
	}
}
