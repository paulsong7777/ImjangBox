package com.imjangbox.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class SecurityConfigTest {

	private final SecurityConfig securityConfig = new SecurityConfig();

	@Test
	void brokerPasswordUsesLocalDefaultOnlyForLocalProfile() {
		MockEnvironment environment = new MockEnvironment();
		environment.setActiveProfiles("local");

		assertThat(securityConfig.brokerPassword("", environment)).isEqualTo("broker-password");
	}

	@Test
	void brokerPasswordIsRequiredOutsideLocalAndTestProfiles() {
		MockEnvironment environment = new MockEnvironment();
		environment.setActiveProfiles("prod");

		assertThatThrownBy(() -> securityConfig.brokerPassword("", environment))
				.isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("imjangbox.broker.password");
	}

	@Test
	void brokerPasswordUsesConfiguredPasswordOutsideLocalProfiles() {
		MockEnvironment environment = new MockEnvironment();
		environment.setActiveProfiles("prod");

		assertThat(securityConfig.brokerPassword("configured-secret", environment))
				.isEqualTo("configured-secret");
	}
}
