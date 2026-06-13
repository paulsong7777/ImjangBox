package com.imjangbox.facility;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class FacilityTemplateServiceTest {

	private final FacilityTemplateService service = new FacilityTemplateService(new EmptyTemplateMapper());

	@Test
	void exposesCommercialBrokerBusinessTypesWithLegacyCafeCompatibility() {
		assertThat(service.findBusinessTypes())
				.containsExactly(
						"CAFE_DESSERT",
						"RESTAURANT",
						"BAR_NIGHT",
						"DELIVERY_TAKEOUT",
						"BEAUTY",
						"ACADEMY",
						"CLINIC",
						"OFFICE",
						"RETAIL",
						"STUDIO_WORKSHOP",
						"FITNESS",
						"UNMANNED_STORE",
						"STORAGE_WORKSPACE",
						"GENERAL");
		assertThat(service.normalizeBusinessType("CAFE")).isEqualTo("CAFE_DESSERT");
		assertThat(BusinessTypeCatalog.label("CAFE")).isEqualTo("카페·디저트");
	}

	@Test
	void returnsPracticalFacilityTemplatesByCommercialBusinessType() {
		assertThat(service.findItemsForBusinessType("CAFE_DESSERT"))
				.extracting(FacilityTemplateItem::label)
				.containsExactly("급배수", "전기 용량", "간판 노출", "테라스/외부공간", "화장실", "주차", "전면 가시성");
		assertThat(service.findItemsForBusinessType("RESTAURANT"))
				.extracting(FacilityTemplateItem::label)
				.containsExactly("덕트", "도시가스", "급배수", "전기 용량", "소방", "주방 공간", "민원 가능성", "원상복구 조건");
		assertThat(service.findItemsForBusinessType("CLINIC"))
				.extracting(FacilityTemplateItem::label)
				.containsExactly("엘리베이터", "주차", "전용면적", "화장실", "간판 노출", "내부 동선", "접근성");
	}

	private static final class EmptyTemplateMapper implements FacilityTemplateMapper {

		@Override
		public List<FacilityTemplateItem> findTemplateItemsByBusinessType(String businessType) {
			return List.of();
		}

		@Override
		public List<String> findBusinessTypes() {
			return List.of();
		}
	}
}
