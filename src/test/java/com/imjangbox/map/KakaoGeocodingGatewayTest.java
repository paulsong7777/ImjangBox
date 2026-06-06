package com.imjangbox.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class KakaoGeocodingGatewayTest {

	@Test
	void geocodeMapsSingleKakaoDocumentToCoordinates() {
		RestClient.Builder builder = RestClient.builder().baseUrl("https://dapi.kakao.com");
		MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
		server.expect(requestTo(containsString("/v2/local/search/address.json")))
				.andExpect(requestTo(containsString("query=Seoul")))
				.andExpect(header("Authorization", "KakaoAK test-key"))
				.andRespond(withSuccess("""
						{
						  "documents": [
						    {
						      "address_name": "Seoul Seongdong-gu Seongsu-dong 1",
						      "x": "127.044280",
						      "y": "37.544580",
						      "road_address": {
						        "address_name": "Seoul Seongdong-gu Test-ro 1"
						      }
						    }
						  ]
						}
						""", MediaType.APPLICATION_JSON));
		GeocodingGateway gateway = new KakaoGeocodingGateway(builder.build(), "test-key");

		GeocodingResult result = gateway.geocode("Seoul");

		assertThat(result).isInstanceOf(GeocodingResult.Success.class);
		GeocodingResult.Success success = (GeocodingResult.Success) result;
		assertThat(success.address().latitude()).isEqualByComparingTo(new BigDecimal("37.544580"));
		assertThat(success.address().longitude()).isEqualByComparingTo(new BigDecimal("127.044280"));
		assertThat(success.address().addressName()).isEqualTo("Seoul Seongdong-gu Seongsu-dong 1");
		assertThat(success.address().roadAddressName()).isEqualTo("Seoul Seongdong-gu Test-ro 1");
		server.verify();
	}

	@Test
	void geocodeReturnsInvalidAddressWhenKakaoHasNoDocuments() {
		RestClient.Builder builder = RestClient.builder().baseUrl("https://dapi.kakao.com");
		MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
		server.expect(requestTo(containsString("/v2/local/search/address.json")))
				.andRespond(withSuccess("{\"documents\":[]}", MediaType.APPLICATION_JSON));
		GeocodingGateway gateway = new KakaoGeocodingGateway(builder.build(), "test-key");

		GeocodingResult result = gateway.geocode("missing address");

		assertThat(result).isInstanceOf(GeocodingResult.Failure.class);
		GeocodingResult.Failure failure = (GeocodingResult.Failure) result;
		assertThat(failure.reason()).isEqualTo(GeocodingFailureReason.INVALID_ADDRESS);
		server.verify();
	}

	@Test
	void geocodeReturnsAmbiguousAddressWhenKakaoReturnsMultipleDocuments() {
		RestClient.Builder builder = RestClient.builder().baseUrl("https://dapi.kakao.com");
		MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
		server.expect(requestTo(containsString("/v2/local/search/address.json")))
				.andRespond(withSuccess("""
						{
						  "documents": [
						    {"address_name": "First", "x": "127.0", "y": "37.0"},
						    {"address_name": "Second", "x": "128.0", "y": "38.0"}
						  ]
						}
						""", MediaType.APPLICATION_JSON));
		GeocodingGateway gateway = new KakaoGeocodingGateway(builder.build(), "test-key");

		GeocodingResult result = gateway.geocode("Seoul");

		assertThat(result).isInstanceOf(GeocodingResult.Failure.class);
		GeocodingResult.Failure failure = (GeocodingResult.Failure) result;
		assertThat(failure.reason()).isEqualTo(GeocodingFailureReason.AMBIGUOUS_ADDRESS);
		server.verify();
	}

	@Test
	void geocodeReturnsProviderFailedWhenKakaoRequestFails() {
		RestClient.Builder builder = RestClient.builder().baseUrl("https://dapi.kakao.com");
		MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
		server.expect(requestTo(containsString("/v2/local/search/address.json")))
				.andRespond(withServerError());
		GeocodingGateway gateway = new KakaoGeocodingGateway(builder.build(), "test-key");

		GeocodingResult result = gateway.geocode("Seoul");

		assertThat(result).isInstanceOf(GeocodingResult.Failure.class);
		GeocodingResult.Failure failure = (GeocodingResult.Failure) result;
		assertThat(failure.reason()).isEqualTo(GeocodingFailureReason.PROVIDER_FAILED);
		server.verify();
	}

	@Test
	void geocodeReturnsProviderFailedWhenKakaoDocumentIsMalformed() {
		RestClient.Builder builder = RestClient.builder().baseUrl("https://dapi.kakao.com");
		MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
		server.expect(requestTo(containsString("/v2/local/search/address.json")))
				.andRespond(withSuccess("""
						{
						  "documents": [
						    {"address_name": "Seoul Seongdong-gu"}
						  ]
						}
						""", MediaType.APPLICATION_JSON));
		GeocodingGateway gateway = new KakaoGeocodingGateway(builder.build(), "test-key");

		GeocodingResult result = gateway.geocode("Seoul");

		assertThat(result).isInstanceOf(GeocodingResult.Failure.class);
		GeocodingResult.Failure failure = (GeocodingResult.Failure) result;
		assertThat(failure.reason()).isEqualTo(GeocodingFailureReason.PROVIDER_FAILED);
		server.verify();
	}
}
