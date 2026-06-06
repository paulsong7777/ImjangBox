package com.imjangbox.map;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

@Component
public class KakaoMapViewFactory {

	private final KakaoMapProperties properties;

	public KakaoMapViewFactory(KakaoMapProperties properties) {
		this.properties = properties;
	}

	public KakaoMapView brokerInspectionMap() {
		if (!properties.isEnabled() || !hasText(properties.getJavascriptApiKey())) {
			return KakaoMapView.disabled();
		}
		String sdkUrl = sdkUrl(properties.getJavascriptApiKey().trim());
		return KakaoMapView.enabled(
				sdkUrl,
				properties.getDefaultLatitude().toPlainString(),
				properties.getDefaultLongitude().toPlainString(),
				properties.getDefaultLevel());
	}

	private String sdkUrl(String javascriptApiKey) {
		String separator = properties.getSdkBaseUrl().contains("?") ? "&" : "?";
		return properties.getSdkBaseUrl()
				+ separator
				+ "appkey="
				+ encodeQueryValue(javascriptApiKey)
				+ "&autoload=false";
	}

	private String encodeQueryValue(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
	}

	private boolean hasText(String value) {
		return value != null && !value.isBlank();
	}
}
