package com.imjangbox.property;

public enum VerificationStatus {

	UNVERIFIED("세부 조건 확인 필요"),
	OWNER_CLAIM("임대인 제공 정보"),
	TENANT_CLAIM("임차인 제공 정보"),
	CO_BROKER_CLAIM("중개 경로 확인 정보"),
	AGENT_CHECKED("현장 확인 완료"),
	DOCUMENT_CHECKED("서류 확인 완료");

	private final String customerDisplayText;

	VerificationStatus(String customerDisplayText) {
		this.customerDisplayText = customerDisplayText;
	}

	public String customerDisplayText() {
		return customerDisplayText;
	}
}
