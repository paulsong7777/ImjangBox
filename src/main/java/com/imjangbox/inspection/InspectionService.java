package com.imjangbox.inspection;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.imjangbox.file.FileStorage;
import com.imjangbox.file.StoredFile;
import com.imjangbox.inspection.persistence.ContactLogWriteRow;
import com.imjangbox.inspection.persistence.FileAttachmentWriteRow;
import com.imjangbox.inspection.persistence.PropertyInspectionMapper;
import com.imjangbox.inspection.persistence.PropertyInspectionRow;
import com.imjangbox.inspection.persistence.PropertyInspectionWriteRow;
import com.imjangbox.inspection.web.InspectionForm;
import com.imjangbox.property.VerificationStatus;

@Service
public class InspectionService {

	private static final int MAX_ATTACHMENT_COUNT = 5;
	private static final long MAX_ATTACHMENT_BYTES = 10L * 1024L * 1024L;
	private static final Set<String> ALLOWED_ATTACHMENT_TYPES = Set.of(
			"application/pdf",
			"image/jpeg",
			"image/png",
			"text/plain");

	private final PropertyInspectionMapper mapper;
	private final FileStorage fileStorage;

	public InspectionService(PropertyInspectionMapper mapper, FileStorage fileStorage) {
		this.mapper = mapper;
		this.fileStorage = fileStorage;
	}

	public InspectionForm findForm(long inspectionId) {
		PropertyInspectionRow row = mapper.findById(inspectionId)
				.orElseThrow(() -> new InspectionNotFoundException(inspectionId));
		InspectionForm form = new InspectionForm();
		form.setTitle(row.title());
		form.setInternalRoadAddress(row.internalRoadAddress());
		form.setInternalDetailAddress(row.internalDetailAddress());
		form.setInternalGeocodeMemo(row.internalGeocodeMemo());
		form.setPublicAddressSummary(row.publicAddressSummary());
		form.setPublicLandmarkHint(row.publicLandmarkHint());
		form.setDepositAmount(row.depositAmount());
		form.setMonthlyRentAmount(row.monthlyRentAmount());
		form.setPremiumAmount(row.premiumAmount());
		form.setAreaSquareMeters(row.areaSquareMeters());
		form.setBusinessFitMemo(row.businessFitMemo());
		form.setConditionMemo(row.conditionMemo());
		form.setPricePrivateNote(row.pricePrivateNote());
		form.setPrivateMemo(row.privateMemo());
		form.setInternalRiskMemo(row.internalRiskMemo());
		form.setVerificationStatus(VerificationStatus.valueOf(row.verificationStatus()));
		return form;
	}

	@Transactional(rollbackFor = IOException.class)
	public long create(InspectionForm form, List<MultipartFile> attachments) throws IOException {
		validateAttachments(attachments);
		PropertyInspectionWriteRow row = form.toWriteRow(null);
		mapper.insert(row);
		persistInternalChildren(row.inspectionId(), form, attachments);
		return row.inspectionId();
	}

	@Transactional(rollbackFor = IOException.class)
	public void update(long inspectionId, InspectionForm form, List<MultipartFile> attachments) throws IOException {
		validateAttachments(attachments);
		if (mapper.update(form.toWriteRow(inspectionId)) == 0) {
			throw new InspectionNotFoundException(inspectionId);
		}
		persistInternalChildren(inspectionId, form, attachments);
	}

	private void validateAttachments(List<MultipartFile> attachments) throws IOException {
		List<MultipartFile> nonEmptyAttachments = attachments.stream()
				.filter(attachment -> !attachment.isEmpty())
				.toList();
		if (nonEmptyAttachments.size() > MAX_ATTACHMENT_COUNT) {
			throw new AttachmentValidationException("Too many attachments");
		}
		for (MultipartFile attachment : nonEmptyAttachments) {
			if (attachment.getSize() > MAX_ATTACHMENT_BYTES) {
				throw new AttachmentValidationException("Attachment is too large");
			}
			String contentType = attachment.getContentType();
			if (contentType == null || !ALLOWED_ATTACHMENT_TYPES.contains(contentType)) {
				throw new AttachmentValidationException("Unsupported attachment content type");
			}
			if (!contentMatches(contentType, attachment)) {
				throw new AttachmentValidationException("Attachment content does not match content type");
			}
		}
	}

	private boolean contentMatches(String contentType, MultipartFile attachment) throws IOException {
		byte[] header = readHeader(attachment);
		return switch (contentType) {
			case "application/pdf" -> startsWith(header, "%PDF-".getBytes());
			case "image/jpeg" -> header.length >= 3
					&& (header[0] & 0xFF) == 0xFF
					&& (header[1] & 0xFF) == 0xD8
					&& (header[2] & 0xFF) == 0xFF;
			case "image/png" -> startsWith(header, new byte[] {
					(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A });
			case "text/plain" -> isPlainText(header);
			default -> false;
		};
	}

	private byte[] readHeader(MultipartFile attachment) throws IOException {
		try (InputStream input = attachment.getInputStream()) {
			return input.readNBytes(512);
		}
	}

	private boolean startsWith(byte[] value, byte[] prefix) {
		if (value.length < prefix.length) {
			return false;
		}
		for (int index = 0; index < prefix.length; index++) {
			if (value[index] != prefix[index]) {
				return false;
			}
		}
		return true;
	}

	private boolean isPlainText(byte[] value) {
		for (byte current : value) {
			int unsigned = current & 0xFF;
			if (unsigned == 0 || (unsigned < 32 && unsigned != '\n' && unsigned != '\r' && unsigned != '\t')) {
				return false;
			}
		}
		return true;
	}

	private void persistInternalChildren(long inspectionId, InspectionForm form, List<MultipartFile> attachments)
			throws IOException {
		if (form.hasContactLog()) {
			mapper.insertContactLog(new ContactLogWriteRow(
					inspectionId, form.getContactedOn(), form.getContactLogContent()));
		}
		for (MultipartFile attachment : attachments) {
			if (!attachment.isEmpty()) {
				StoredFile storedFile = fileStorage.store(inspectionId, attachment);
				mapper.insertFileAttachment(FileAttachmentWriteRow.from(inspectionId, storedFile));
			}
		}
	}
}
