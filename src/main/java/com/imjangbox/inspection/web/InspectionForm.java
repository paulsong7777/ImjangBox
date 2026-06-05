package com.imjangbox.inspection.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.AssertTrue;

import com.imjangbox.inspection.persistence.PropertyInspectionWriteRow;
import com.imjangbox.property.VerificationStatus;

public class InspectionForm {

	@NotBlank
	private String title;
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

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
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
}
