package com.imjangbox.share;

import java.util.List;
import java.util.stream.IntStream;

import com.imjangbox.inspection.persistence.FacilityAnswerWriteRow;
import com.imjangbox.inspection.persistence.FileAttachmentWriteRow;
import com.imjangbox.inspection.persistence.PropertyInspectionRow;
import com.imjangbox.property.CommercialPropertyInspection;
import com.imjangbox.property.PublicAddress;
import com.imjangbox.property.VerificationStatus;

public final class PublicShareSnapshotFactory {

	private static final int MAX_PUBLIC_IMAGES = 5;

	private PublicShareSnapshotFactory() {
	}

	public static PublicShareSnapshot from(String shareId, CommercialPropertyInspection inspection) {
		return new PublicShareSnapshot(
				shareId,
				inspection.title(),
				inspection.publicAddress(),
				new PublicPricingSnapshot(
						inspection.pricing().depositAmount(),
						inspection.pricing().monthlyRentAmount(),
						inspection.pricing().premiumAmount()),
				inspection.verificationStatus(),
				inspection.verificationStatus().customerDisplayText(),
				inspection.facilityChecks().stream()
						.filter(answer -> answer.customerVisible() && hasText(answer.answer()))
						.map(answer -> new PublicFacilitySnapshot(answer.label(), answer.answer()))
						.toList(),
				List.of());
	}

	public static PublicShareSnapshot from(
			String shareId,
			PropertyInspectionRow inspection,
			List<FacilityAnswerWriteRow> facilityAnswers,
			List<FileAttachmentWriteRow> fileAttachments) {
		VerificationStatus verificationStatus = VerificationStatus.valueOf(inspection.verificationStatus());
		return new PublicShareSnapshot(
				shareId,
				inspection.title(),
				new PublicAddress(inspection.publicAddressSummary(), inspection.publicLandmarkHint()),
				new PublicPricingSnapshot(
						inspection.depositAmount(),
						inspection.monthlyRentAmount(),
						inspection.premiumAmount()),
				verificationStatus,
				verificationStatus.customerDisplayText(),
				facilityAnswers.stream()
						.filter(answer -> answer.customerVisible() && hasText(answer.answer()))
						.map(answer -> new PublicFacilitySnapshot(answer.label(), answer.answer()))
						.toList(),
				publicImages(shareId, fileAttachments));
	}

	static List<PublicImageSnapshot> publicImages(String shareId, List<FileAttachmentWriteRow> fileAttachments) {
		List<FileAttachmentWriteRow> images = fileAttachments.stream()
				.filter(PublicShareSnapshotFactory::isPublicImage)
				.limit(MAX_PUBLIC_IMAGES)
				.toList();
		return IntStream.range(0, images.size())
				.mapToObj(index -> new PublicImageSnapshot(
						"/share/" + shareId + "/images/" + (index + 1),
						"Property image " + (index + 1)))
				.toList();
	}

	private static boolean isPublicImage(FileAttachmentWriteRow attachment) {
		return "image/jpeg".equals(attachment.contentType()) || "image/png".equals(attachment.contentType());
	}

	private static boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
