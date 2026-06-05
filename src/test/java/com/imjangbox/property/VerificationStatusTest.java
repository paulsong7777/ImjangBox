package com.imjangbox.property;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class VerificationStatusTest {

	@Test
	void containsOnlySupportedInitialStatuses() {
		assertThat(Arrays.stream(VerificationStatus.values()).map(Enum::name))
				.containsExactly(
						"UNVERIFIED",
						"OWNER_CLAIM",
						"TENANT_CLAIM",
						"CO_BROKER_CLAIM",
						"AGENT_CHECKED",
						"DOCUMENT_CHECKED");
	}

	@Test
	void everyStatusHasCustomerSafeDisplayText() {
		assertThat(VerificationStatus.values())
				.allSatisfy(status -> assertThat(status.customerDisplayText())
						.isNotBlank()
						.doesNotContainIgnoringCase("guaranteed")
						.doesNotContain("확정")
						.doesNotContain("보장"));
	}
}
