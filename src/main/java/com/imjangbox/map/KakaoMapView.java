package com.imjangbox.map;

public record KakaoMapView(
		boolean enabled,
		String sdkUrl,
		String latitude,
		String longitude,
		int level) {

	public static KakaoMapView disabled() {
		return new KakaoMapView(false, null, null, null, 0);
	}

	public static KakaoMapView enabled(String sdkUrl, String latitude, String longitude, int level) {
		return new KakaoMapView(true, sdkUrl, latitude, longitude, level);
	}
}
