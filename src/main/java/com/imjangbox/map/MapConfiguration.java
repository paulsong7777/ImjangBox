package com.imjangbox.map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(KakaoGeocodingProperties.class)
class MapConfiguration {

	@Bean
	GeocodingGateway geocodingGateway(KakaoGeocodingProperties properties, RestClient.Builder restClientBuilder) {
		if (!properties.isEnabled() || !hasText(properties.getRestApiKey())) {
			return new DisabledGeocodingGateway();
		}
		return new KakaoGeocodingGateway(
				restClientBuilder.baseUrl(properties.getBaseUrl()).build(),
				properties.getRestApiKey());
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
