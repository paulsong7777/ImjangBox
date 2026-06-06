package com.imjangbox.map;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class KakaoMapViewFactoryTest {

	@Test
	void returnsDisabledViewWhenBrowserKeyIsMissing() {
		KakaoMapProperties properties = new KakaoMapProperties();
		properties.setEnabled(true);
		properties.setJavascriptApiKey(" ");

		KakaoMapView view = new KakaoMapViewFactory(properties).brokerInspectionMap();

		assertThat(view.enabled()).isFalse();
		assertThat(view.sdkUrl()).isNull();
	}

	@Test
	void buildsOfficialSdkUrlWithEncodedBrowserKeyOnly() {
		KakaoMapProperties properties = new KakaoMapProperties();
		properties.setEnabled(true);
		properties.setJavascriptApiKey("browser key+only");

		KakaoMapView view = new KakaoMapViewFactory(properties).brokerInspectionMap();

		assertThat(view.enabled()).isTrue();
		assertThat(view.sdkUrl())
				.startsWith("https://dapi.kakao.com/v2/maps/sdk.js?")
				.contains("appkey=browser%20key%2Bonly")
				.contains("autoload=false")
				.doesNotContain("rest-api-key")
				.doesNotContain("KAKAO_REST_API_KEY");
		assertThat(view.latitude()).isEqualTo("37.566500");
		assertThat(view.longitude()).isEqualTo("126.978000");
		assertThat(view.level()).isEqualTo(4);
	}
}
