package com.imjangbox.map;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imjangbox.maps.kakao")
public class KakaoMapProperties {

	private boolean enabled;
	private String javascriptApiKey;
	private String sdkBaseUrl = "https://dapi.kakao.com/v2/maps/sdk.js";
	private BigDecimal defaultLatitude = new BigDecimal("37.566500");
	private BigDecimal defaultLongitude = new BigDecimal("126.978000");
	private int defaultLevel = 4;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getJavascriptApiKey() {
		return javascriptApiKey;
	}

	public void setJavascriptApiKey(String javascriptApiKey) {
		this.javascriptApiKey = javascriptApiKey;
	}

	public String getSdkBaseUrl() {
		return sdkBaseUrl;
	}

	public void setSdkBaseUrl(String sdkBaseUrl) {
		this.sdkBaseUrl = sdkBaseUrl;
	}

	public BigDecimal getDefaultLatitude() {
		return defaultLatitude;
	}

	public void setDefaultLatitude(BigDecimal defaultLatitude) {
		this.defaultLatitude = defaultLatitude;
	}

	public BigDecimal getDefaultLongitude() {
		return defaultLongitude;
	}

	public void setDefaultLongitude(BigDecimal defaultLongitude) {
		this.defaultLongitude = defaultLongitude;
	}

	public int getDefaultLevel() {
		return defaultLevel;
	}

	public void setDefaultLevel(int defaultLevel) {
		this.defaultLevel = defaultLevel;
	}
}
