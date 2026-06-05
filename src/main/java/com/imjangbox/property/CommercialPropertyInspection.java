package com.imjangbox.property;

import java.util.List;
import java.util.Objects;

import com.imjangbox.facility.FacilityCheckAnswer;

public record CommercialPropertyInspection(
		long inspectionId,
		String title,
		InternalAddress internalAddress,
		PublicAddress publicAddress,
		Pricing pricing,
		List<FacilityCheckAnswer> facilityChecks,
		List<Stakeholder> stakeholders,
		List<ContactLog> contactLogs,
		String privateMemo,
		String internalRiskMemo,
		VerificationStatus verificationStatus) {

	public CommercialPropertyInspection {
		facilityChecks = List.copyOf(Objects.requireNonNull(facilityChecks, "facilityChecks"));
		stakeholders = List.copyOf(Objects.requireNonNull(stakeholders, "stakeholders"));
		contactLogs = List.copyOf(Objects.requireNonNull(contactLogs, "contactLogs"));
	}
}
