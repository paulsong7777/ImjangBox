package com.imjangbox.property;

public enum VerificationStatus {

	UNVERIFIED("Unverified"),
	OWNER_CLAIM("Owner claim"),
	TENANT_CLAIM("Tenant claim"),
	CO_BROKER_CLAIM("Co-broker claim"),
	AGENT_CHECKED("Agent checked"),
	DOCUMENT_CHECKED("Document checked");

	private final String customerDisplayText;

	VerificationStatus(String customerDisplayText) {
		this.customerDisplayText = customerDisplayText;
	}

	public String customerDisplayText() {
		return customerDisplayText;
	}
}
