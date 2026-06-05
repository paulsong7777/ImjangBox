package com.imjangbox.inspection.persistence;

public class PropertyInspectionWriteRow {

	private Long inspectionId;
	private final String title;
	private final String verificationStatus;
	private final String internalRoadAddress;
	private final String internalDetailAddress;
	private final String internalGeocodeMemo;
	private final String publicAddressSummary;
	private final String publicLandmarkHint;
	private final long depositAmount;
	private final long monthlyRentAmount;
	private final long premiumAmount;
	private final String areaSquareMeters;
	private final String businessFitMemo;
	private final String conditionMemo;
	private final String pricePrivateNote;
	private final String privateMemo;
	private final String internalRiskMemo;

	public PropertyInspectionWriteRow(Builder builder) {
		inspectionId = builder.inspectionId;
		title = builder.title;
		verificationStatus = builder.verificationStatus;
		internalRoadAddress = builder.internalRoadAddress;
		internalDetailAddress = builder.internalDetailAddress;
		internalGeocodeMemo = builder.internalGeocodeMemo;
		publicAddressSummary = builder.publicAddressSummary;
		publicLandmarkHint = builder.publicLandmarkHint;
		depositAmount = builder.depositAmount;
		monthlyRentAmount = builder.monthlyRentAmount;
		premiumAmount = builder.premiumAmount;
		areaSquareMeters = builder.areaSquareMeters;
		businessFitMemo = builder.businessFitMemo;
		conditionMemo = builder.conditionMemo;
		pricePrivateNote = builder.pricePrivateNote;
		privateMemo = builder.privateMemo;
		internalRiskMemo = builder.internalRiskMemo;
	}

	public Long inspectionId() {
		return inspectionId;
	}

	public void setInspectionId(Long inspectionId) {
		this.inspectionId = inspectionId;
	}

	public String title() { return title; }
	public String getTitle() { return title; }
	public String verificationStatus() { return verificationStatus; }
	public String getVerificationStatus() { return verificationStatus; }
	public String internalRoadAddress() { return internalRoadAddress; }
	public String getInternalRoadAddress() { return internalRoadAddress; }
	public String internalDetailAddress() { return internalDetailAddress; }
	public String getInternalDetailAddress() { return internalDetailAddress; }
	public String internalGeocodeMemo() { return internalGeocodeMemo; }
	public String getInternalGeocodeMemo() { return internalGeocodeMemo; }
	public String publicAddressSummary() { return publicAddressSummary; }
	public String getPublicAddressSummary() { return publicAddressSummary; }
	public String publicLandmarkHint() { return publicLandmarkHint; }
	public String getPublicLandmarkHint() { return publicLandmarkHint; }
	public long depositAmount() { return depositAmount; }
	public long getDepositAmount() { return depositAmount; }
	public long monthlyRentAmount() { return monthlyRentAmount; }
	public long getMonthlyRentAmount() { return monthlyRentAmount; }
	public long premiumAmount() { return premiumAmount; }
	public long getPremiumAmount() { return premiumAmount; }
	public String areaSquareMeters() { return areaSquareMeters; }
	public String getAreaSquareMeters() { return areaSquareMeters; }
	public String businessFitMemo() { return businessFitMemo; }
	public String getBusinessFitMemo() { return businessFitMemo; }
	public String conditionMemo() { return conditionMemo; }
	public String getConditionMemo() { return conditionMemo; }
	public String pricePrivateNote() { return pricePrivateNote; }
	public String getPricePrivateNote() { return pricePrivateNote; }
	public String privateMemo() { return privateMemo; }
	public String getPrivateMemo() { return privateMemo; }
	public String internalRiskMemo() { return internalRiskMemo; }
	public String getInternalRiskMemo() { return internalRiskMemo; }

	public static class Builder {
		private Long inspectionId;
		private String title;
		private String verificationStatus;
		private String internalRoadAddress;
		private String internalDetailAddress;
		private String internalGeocodeMemo;
		private String publicAddressSummary;
		private String publicLandmarkHint;
		private long depositAmount;
		private long monthlyRentAmount;
		private long premiumAmount;
		private String areaSquareMeters;
		private String businessFitMemo;
		private String conditionMemo;
		private String pricePrivateNote;
		private String privateMemo;
		private String internalRiskMemo;

		public Builder inspectionId(Long value) { inspectionId = value; return this; }
		public Builder title(String value) { title = value; return this; }
		public Builder verificationStatus(String value) { verificationStatus = value; return this; }
		public Builder internalRoadAddress(String value) { internalRoadAddress = value; return this; }
		public Builder internalDetailAddress(String value) { internalDetailAddress = value; return this; }
		public Builder internalGeocodeMemo(String value) { internalGeocodeMemo = value; return this; }
		public Builder publicAddressSummary(String value) { publicAddressSummary = value; return this; }
		public Builder publicLandmarkHint(String value) { publicLandmarkHint = value; return this; }
		public Builder depositAmount(long value) { depositAmount = value; return this; }
		public Builder monthlyRentAmount(long value) { monthlyRentAmount = value; return this; }
		public Builder premiumAmount(long value) { premiumAmount = value; return this; }
		public Builder areaSquareMeters(String value) { areaSquareMeters = value; return this; }
		public Builder businessFitMemo(String value) { businessFitMemo = value; return this; }
		public Builder conditionMemo(String value) { conditionMemo = value; return this; }
		public Builder pricePrivateNote(String value) { pricePrivateNote = value; return this; }
		public Builder privateMemo(String value) { privateMemo = value; return this; }
		public Builder internalRiskMemo(String value) { internalRiskMemo = value; return this; }
		public PropertyInspectionWriteRow build() { return new PropertyInspectionWriteRow(this); }
	}
}
