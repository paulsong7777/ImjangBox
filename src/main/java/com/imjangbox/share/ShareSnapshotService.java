package com.imjangbox.share;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imjangbox.file.FileStorage;
import com.imjangbox.inspection.InspectionNotFoundException;
import com.imjangbox.inspection.persistence.FacilityAnswerWriteRow;
import com.imjangbox.inspection.persistence.FileAttachmentWriteRow;
import com.imjangbox.inspection.persistence.PropertyInspectionMapper;
import com.imjangbox.inspection.persistence.PropertyInspectionRow;
import com.imjangbox.property.PublicAddress;
import com.imjangbox.property.VerificationStatus;

@Service
public class ShareSnapshotService {

	private static final int MAX_PUBLIC_IMAGES = 5;

	private final PropertyInspectionMapper inspectionMapper;
	private final ShareSnapshotMapper shareMapper;
	private final FileStorage fileStorage;
	private final Supplier<String> shareIdGenerator;

	@Autowired
	public ShareSnapshotService(
			PropertyInspectionMapper inspectionMapper,
			ShareSnapshotMapper shareMapper,
			FileStorage fileStorage) {
		this(inspectionMapper, shareMapper, fileStorage, () -> java.util.UUID.randomUUID().toString());
	}

	ShareSnapshotService(
			PropertyInspectionMapper inspectionMapper,
			ShareSnapshotMapper shareMapper,
			FileStorage fileStorage,
			Supplier<String> shareIdGenerator) {
		this.inspectionMapper = inspectionMapper;
		this.shareMapper = shareMapper;
		this.fileStorage = fileStorage;
		this.shareIdGenerator = shareIdGenerator;
	}

	@Transactional
	public PublicShareSnapshot createSnapshot(long inspectionId) {
		PropertyInspectionRow inspection = inspectionMapper.findById(inspectionId)
				.orElseThrow(() -> new InspectionNotFoundException(inspectionId));
		List<FacilityAnswerWriteRow> facilityAnswers = inspectionMapper.findFacilityAnswers(inspectionId);
		List<FileAttachmentWriteRow> publicImages = inspectionMapper.findFileAttachments(inspectionId).stream()
				.filter(this::isPublicImage)
				.limit(MAX_PUBLIC_IMAGES)
				.toList();
		String shareId = shareIdGenerator.get();
		PublicShareSnapshot snapshot = PublicShareSnapshotFactory.from(
				shareId,
				inspection,
				facilityAnswers,
				publicImages);

		shareMapper.insertSnapshot(ShareSnapshotWriteRow.from(inspectionId, snapshot));
		IntStream.range(0, snapshot.facilities().size())
				.mapToObj(index -> new ShareFacilitySnapshotRow(
						shareId,
						index + 1,
						snapshot.facilities().get(index).label(),
						snapshot.facilities().get(index).answer()))
				.forEach(shareMapper::insertFacility);
		IntStream.range(0, publicImages.size())
				.mapToObj(index -> new ShareImageSnapshotRow(
						shareId,
						index + 1,
						publicImages.get(index).contentType(),
						publicImages.get(index).storageKey()))
				.forEach(shareMapper::insertImage);

		return snapshot;
	}

	@Transactional(readOnly = true)
	public PublicShareSnapshot findSnapshot(String shareId) {
		ShareSnapshotRow row = shareMapper.findSnapshotByShareId(shareId)
				.orElseThrow(() -> new ShareSnapshotNotFoundException(shareId));
		VerificationStatus verificationStatus = VerificationStatus.valueOf(row.verificationStatus());
		return new PublicShareSnapshot(
				row.shareId(),
				row.title(),
				new PublicAddress(row.publicAddressSummary(), row.publicLandmarkHint()),
				new PublicPricingSnapshot(row.depositAmount(), row.monthlyRentAmount(), row.premiumAmount()),
				verificationStatus,
				row.verificationDisplayText(),
				shareMapper.findFacilitiesByShareId(shareId).stream()
						.map(facility -> new PublicFacilitySnapshot(facility.label(), facility.answer()))
						.toList(),
				shareMapper.findImagesByShareId(shareId).stream()
						.map(image -> new PublicImageSnapshot(
								"/share/" + shareId + "/images/" + image.displayOrder(),
								"Property image " + image.displayOrder()))
						.toList());
	}

	@Transactional(readOnly = true)
	public Optional<PublicImageFile> findImageFile(String shareId, int displayOrder) throws java.io.IOException {
		Optional<ShareImageSnapshotRow> row = shareMapper.findImageByShareIdAndDisplayOrder(shareId, displayOrder);
		if (row.isEmpty()) {
			return Optional.empty();
		}
		Optional<Resource> resource = fileStorage.load(row.orElseThrow().sourceStorageKey());
		return resource.map(value -> new PublicImageFile(value, row.orElseThrow().contentType()));
	}

	private boolean isPublicImage(FileAttachmentWriteRow attachment) {
		return "image/jpeg".equals(attachment.contentType()) || "image/png".equals(attachment.contentType());
	}
}
