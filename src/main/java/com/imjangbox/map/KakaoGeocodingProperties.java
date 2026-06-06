package com.imjangbox.map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imjangbox.geocoding.kakao")
public class KakaoGeocodingProperties {

	private boolean enabled;
	private String restApiKey;
	private String baseUrl = "https://dapi.kakao.com";

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getRestApiKey() {
		return restApiKey;
	}

	public void setRestApiKey(String restApiKey) {
		this.restApiKey = restApiKey;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
