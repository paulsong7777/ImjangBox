package com.imjangbox.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/", "/share/**").permitAll()
						.requestMatchers("/broker/**").hasRole("BROKER")
						.anyRequest().permitAll())
				.httpBasic(Customizer.withDefaults())
				.build();
	}

	@Bean
	UserDetailsService brokerUsers(
			@Value("${imjangbox.broker.username:broker}") String username,
			@Value("${imjangbox.broker.password:}") String password,
			Environment environment) {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		String brokerPassword = brokerPassword(password, environment);
		return new InMemoryUserDetailsManager(User.withUsername(username)
				.password(passwordEncoder.encode(brokerPassword))
				.roles("BROKER")
				.build());
	}

	String brokerPassword(String configuredPassword, Environment environment) {
		if (StringUtils.hasText(configuredPassword)) {
			return configuredPassword;
		}
		if (environment.acceptsProfiles(Profiles.of("local", "test"))) {
			return "broker-password";
		}
		throw new IllegalStateException("imjangbox.broker.password is required outside local/test profiles");
	}
}
