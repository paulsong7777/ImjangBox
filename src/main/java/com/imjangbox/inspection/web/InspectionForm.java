package com.imjangbox.inspection.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.AssertTrue;

import com.imjangbox.facility.FacilityTemplateItem;
import com.imjangbox.inspection.persistence.FacilityAnswerWriteRow;
import com.imjangbox.inspection.persistence.PropertyInspectionWriteRow;
import com.imjangbox.inspection.persistence.SearchIndexWriteRow;
import com.imjangbox.property.VerificationStatus;

public class InspectionForm {

	@NotBlank
	private String title;
	@NotBlank
	private String businessType = "GENERAL";
	@NotBlank
	private String internalRoadAddress;
	private String internalDetailAddress;
	private String internalGeocodeMemo;
	@NotBlank
	private String publicAddressSummary;
	private String publicLandmarkHint;
	@NotNull
	@PositiveOrZero
	private Long depositAmount = 0L;
	@NotNull
	@PositiveOrZero
	private Long monthlyRentAmount = 0L;
	@NotNull
	@PositiveOrZero
	private Long premiumAmount = 0L;
	private String areaSquareMeters;
	private String businessFitMemo;
	private String conditionMemo;
	private String pricePrivateNote;
	private String privateMemo;
	private String internalRiskMemo;
	@NotNull
	private VerificationStatus verificationStatus = VerificationStatus.UNVERIFIED;
	@Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}")
	private String contactedOn;
	private String contactLogContent;
	private List<FacilityAnswerForm> facilityAnswers = new ArrayList<>();

	public boolean hasContactLog() {
		return hasText(contactedOn) && hasText(contactLogContent);
	}

	@AssertTrue(message = "Contact log date and content must be entered together")
	public boolean isContactLogComplete() {
		return hasText(contactedOn) == hasText(contactLogContent);
	}

	public PropertyInspectionWriteRow toWriteRow(Long inspectionId) {
		return new PropertyInspectionWriteRow.Builder()
				.inspectionId(inspectionId)
				.title(title)
				.businessType(businessType)
				.verificationStatus(verificationStatus.name())
				.internalRoadAddress(internalRoadAddress)
				.internalDetailAddress(internalDetailAddress)
				.internalGeocodeMemo(internalGeocodeMemo)
				.publicAddressSummary(publicAddressSummary)
				.publicLandmarkHint(publicLandmarkHint)
				.depositAmount(depositAmount)
				.monthlyRentAmount(monthlyRentAmount)
				.premiumAmount(premiumAmount)
				.areaSquareMeters(areaSquareMeters)
				.businessFitMemo(businessFitMemo)
				.conditionMemo(conditionMemo)
				.pricePrivateNote(pricePrivateNote)
				.privateMemo(privateMemo)
				.internalRiskMemo(internalRiskMemo)
				.build();
	}

	public void applyFacilityTemplates(List<FacilityTemplateItem> templateItems) {
		Map<String, FacilityAnswerForm> existingAnswers = new LinkedHashMap<>();
		for (FacilityAnswerForm existingAnswer : facilityAnswers) {
			existingAnswers.put(existingAnswer.getTemplateItemKey(), existingAnswer);
		}
		List<FacilityAnswerForm> mergedAnswers = new ArrayList<>();
		for (FacilityTemplateItem templateItem : templateItems) {
			FacilityAnswerForm answer = FacilityAnswerForm.fromTemplateItem(templateItem);
			FacilityAnswerForm existingAnswer = existingAnswers.remove(templateItem.itemKey());
			if (existingAnswer != null) {
				answer.setAnswer(existingAnswer.getAnswer());
			}
			mergedAnswers.add(answer);
		}
		facilityAnswers = mergedAnswers;
	}

	public List<FacilityAnswerWriteRow> toFacilityAnswerWriteRows(long inspectionId) {
		return facilityAnswers.stream()
				.filter(FacilityAnswerForm::hasAnswer)
				.map(answer -> answer.toWriteRow(inspectionId))
				.toList();
	}

	public SearchIndexWriteRow toSearchIndexWriteRow(long inspectionId) {
		return new SearchIndexWriteRow(
				inspectionId,
				title,
				publicAddressSummary,
				publicLandmarkHint,
				businessType,
				verificationStatus.name(),
				areaSquareMeters,
				searchText());
	}

	private String searchText() {
		StringBuilder builder = new StringBuilder();
		appendSearchTerm(builder, title);
		appendSearchTerm(builder, publicAddressSummary);
		appendSearchTerm(builder, publicLandmarkHint);
		appendSearchTerm(builder, businessType);
		appendSearchTerm(builder, verificationStatus.name());
		appendSearchTerm(builder, areaSquareMeters);
		return builder.toString();
	}

	private void appendSearchTerm(StringBuilder builder, String value) {
		if (!hasText(value)) {
			return;
		}
		if (!builder.isEmpty()) {
			builder.append(' ');
		}
		builder.append(value.strip());
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getBusinessType() { return businessType; }
	public void setBusinessType(String businessType) { this.businessType = businessType; }
	public String getInternalRoadAddress() { return internalRoadAddress; }
	public void setInternalRoadAddress(String internalRoadAddress) { this.internalRoadAddress = internalRoadAddress; }
	public String getInternalDetailAddress() { return internalDetailAddress; }
	public void setInternalDetailAddress(String internalDetailAddress) { this.internalDetailAddress = internalDetailAddress; }
	public String getInternalGeocodeMemo() { return internalGeocodeMemo; }
	public void setInternalGeocodeMemo(String internalGeocodeMemo) { this.internalGeocodeMemo = internalGeocodeMemo; }
	public String getPublicAddressSummary() { return publicAddressSummary; }
	public void setPublicAddressSummary(String publicAddressSummary) { this.publicAddressSummary = publicAddressSummary; }
	public String getPublicLandmarkHint() { return publicLandmarkHint; }
	public void setPublicLandmarkHint(String publicLandmarkHint) { this.publicLandmarkHint = publicLandmarkHint; }
	public Long getDepositAmount() { return depositAmount; }
	public void setDepositAmount(Long depositAmount) { this.depositAmount = depositAmount; }
	public Long getMonthlyRentAmount() { return monthlyRentAmount; }
	public void setMonthlyRentAmount(Long monthlyRentAmount) { this.monthlyRentAmount = monthlyRentAmount; }
	public Long getPremiumAmount() { return premiumAmount; }
	public void setPremiumAmount(Long premiumAmount) { this.premiumAmount = premiumAmount; }
	public String getAreaSquareMeters() { return areaSquareMeters; }
	public void setAreaSquareMeters(String areaSquareMeters) { this.areaSquareMeters = areaSquareMeters; }
	public String getBusinessFitMemo() { return businessFitMemo; }
	public void setBusinessFitMemo(String businessFitMemo) { this.businessFitMemo = businessFitMemo; }
	public String getConditionMemo() { return conditionMemo; }
	public void setConditionMemo(String conditionMemo) { this.conditionMemo = conditionMemo; }
	public String getPricePrivateNote() { return pricePrivateNote; }
	public void setPricePrivateNote(String pricePrivateNote) { this.pricePrivateNote = pricePrivateNote; }
	public String getPrivateMemo() { return privateMemo; }
	public void setPrivateMemo(String privateMemo) { this.privateMemo = privateMemo; }
	public String getInternalRiskMemo() { return internalRiskMemo; }
	public void setInternalRiskMemo(String internalRiskMemo) { this.internalRiskMemo = internalRiskMemo; }
	public VerificationStatus getVerificationStatus() { return verificationStatus; }
	public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
	public String getContactedOn() { return contactedOn; }
	public void setContactedOn(String contactedOn) { this.contactedOn = contactedOn; }
	public String getContactLogContent() { return contactLogContent; }
	public void setContactLogContent(String contactLogContent) { this.contactLogContent = contactLogContent; }
	public List<FacilityAnswerForm> getFacilityAnswers() { return facilityAnswers; }
	public void setFacilityAnswers(List<FacilityAnswerForm> facilityAnswers) {
		this.facilityAnswers = facilityAnswers == null ? new ArrayList<>() : facilityAnswers;
	}
}
