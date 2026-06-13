package com.imjangbox.facility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class BusinessTypeCatalog {

	public static final String DEFAULT_BUSINESS_TYPE = "CAFE_DESSERT";
	private static final String LEGACY_CAFE = "CAFE";

	private static final List<BusinessTypeOption> OPTIONS = List.of(
			new BusinessTypeOption("CAFE_DESSERT", "카페·디저트"),
			new BusinessTypeOption("RESTAURANT", "음식점"),
			new BusinessTypeOption("BAR_NIGHT", "주점·야간영업"),
			new BusinessTypeOption("DELIVERY_TAKEOUT", "배달·포장 전문점"),
			new BusinessTypeOption("BEAUTY", "미용·네일·뷰티"),
			new BusinessTypeOption("ACADEMY", "학원·교습"),
			new BusinessTypeOption("CLINIC", "병의원·클리닉"),
			new BusinessTypeOption("OFFICE", "사무실"),
			new BusinessTypeOption("RETAIL", "소매점·편집샵"),
			new BusinessTypeOption("STUDIO_WORKSHOP", "공방·스튜디오"),
			new BusinessTypeOption("FITNESS", "헬스·PT·필라테스"),
			new BusinessTypeOption("UNMANNED_STORE", "무인점포"),
			new BusinessTypeOption("STORAGE_WORKSPACE", "창고·작업장"),
			new BusinessTypeOption("GENERAL", "기타/직접입력"));

	private static final Map<String, List<FacilityTemplateItem>> DEFAULT_TEMPLATES = defaultTemplates();

	private BusinessTypeCatalog() {
	}

	public static List<String> supportedTypes() {
		return OPTIONS.stream()
				.map(BusinessTypeOption::value)
				.toList();
	}

	public static List<BusinessTypeOption> options() {
		return OPTIONS;
	}

	public static String normalize(String businessType) {
		if (businessType == null || businessType.isBlank()) {
			return DEFAULT_BUSINESS_TYPE;
		}
		String normalized = businessType.trim().toUpperCase(Locale.ROOT);
		if (LEGACY_CAFE.equals(normalized)) {
			return DEFAULT_BUSINESS_TYPE;
		}
		return normalized;
	}

	public static String label(String businessType) {
		String normalized = normalize(businessType);
		return OPTIONS.stream()
				.filter(option -> option.value().equals(normalized))
				.map(BusinessTypeOption::label)
				.findFirst()
				.orElse(normalized);
	}

	public static boolean supports(String businessType) {
		String normalized = normalize(businessType);
		return OPTIONS.stream().anyMatch(option -> option.value().equals(normalized));
	}

	public static List<FacilityTemplateItem> defaultFacilityTemplates(String businessType) {
		return DEFAULT_TEMPLATES.getOrDefault(normalize(businessType), List.of());
	}

	private static Map<String, List<FacilityTemplateItem>> defaultTemplates() {
		Map<String, List<FacilityTemplateItem>> templates = new LinkedHashMap<>();
		put(templates, "CAFE_DESSERT", List.of(
				item("water_supply", "급배수", 10),
				item("electric_capacity", "전기 용량", 20),
				item("sign_visibility", "간판 노출", 30),
				item("terrace_outdoor_space", "테라스/외부공간", 40),
				item("restroom", "화장실", 50),
				item("parking", "주차", 60),
				item("front_visibility", "전면 가시성", 70)));
		put(templates, "RESTAURANT", List.of(
				item("exhaust_duct", "덕트", 10),
				item("city_gas", "도시가스", 20),
				item("water_supply", "급배수", 30),
				item("electric_capacity", "전기 용량", 40),
				item("fire_safety", "소방", 50),
				item("kitchen_space", "주방 공간", 60),
				item("complaint_risk", "민원 가능성", 70),
				item("restoration_terms", "원상복구 조건", 80)));
		put(templates, "BAR_NIGHT", List.of(
				item("noise_complaint_risk", "소음 민원 가능성", 10),
				item("business_hour_limit", "영업시간 제한", 20),
				item("restroom", "화장실", 30),
				item("fire_safety", "소방", 40),
				item("ventilation_exhaust", "환기/배기", 50),
				item("nearby_residential", "주변 주거지 여부", 60)));
		put(templates, "DELIVERY_TAKEOUT", List.of(
				item("kitchen_flow", "주방 동선", 10),
				item("exhaust", "배기", 20),
				item("motorcycle_access", "오토바이 접근", 30),
				item("pickup_waiting_space", "포장 대기 공간", 40),
				item("loading_convenience", "상하차 편의", 50),
				item("complaint_risk", "민원 가능성", 60)));
		put(templates, "BEAUTY", List.of(
				item("water_supply", "급배수", 10),
				item("electric_capacity", "전기 용량", 20),
				item("ventilation", "환기", 30),
				item("internal_restroom", "내부 화장실", 40),
				item("floor_elevator", "층수/엘리베이터", 50),
				item("female_customer_access", "여성 고객 접근성", 60)));
		put(templates, "ACADEMY", List.of(
				item("use_confirmation", "용도 확인", 10),
				item("floor", "층수", 20),
				item("fire_safety", "소방", 30),
				item("restroom", "화장실", 40),
				item("elevator", "엘리베이터", 50),
				item("nearby_school_apartment", "주변 학교/아파트", 60),
				item("noise_risk", "소음 가능성", 70)));
		put(templates, "CLINIC", List.of(
				item("elevator", "엘리베이터", 10),
				item("parking", "주차", 20),
				item("exclusive_area", "전용면적", 30),
				item("restroom", "화장실", 40),
				item("sign_visibility", "간판 노출", 50),
				item("internal_flow", "내부 동선", 60),
				item("accessibility", "접근성", 70)));
		put(templates, "OFFICE", List.of(
				item("heating_cooling", "냉난방", 10),
				item("internet_telecom", "인터넷/통신", 20),
				item("parking", "주차", 30),
				item("maintenance_fee", "관리비", 40),
				item("daylight", "채광", 50),
				item("meeting_space", "회의공간 가능성", 60),
				item("security_access", "보안/출입", 70)));
		put(templates, "RETAIL", List.of(
				item("front_exposure", "전면 노출", 10),
				item("sign", "간판", 20),
				item("foot_traffic", "유동인구", 30),
				item("display_space", "진열 공간", 40),
				item("storage_space", "창고 공간", 50),
				item("entrance", "출입구", 60)));
		put(templates, "STUDIO_WORKSHOP", List.of(
				item("ceiling_height", "층고", 10),
				item("noise_risk", "소음 가능성", 20),
				item("electric_capacity", "전기 용량", 30),
				item("ventilation", "환기", 40),
				item("loading", "상하차", 50),
				item("workspace", "작업공간", 60),
				item("complaint_risk", "민원 가능성", 70)));
		put(templates, "FITNESS", List.of(
				item("ceiling_height", "층고", 10),
				item("floor_load", "바닥 하중", 20),
				item("noise_vibration", "소음/진동", 30),
				item("shower_possible", "샤워실 가능성", 40),
				item("ventilation", "환기", 50),
				item("parking", "주차", 60),
				item("elevator", "엘리베이터", 70)));
		put(templates, "UNMANNED_STORE", List.of(
				item("front_exposure", "전면 노출", 10),
				item("cctv", "CCTV", 20),
				item("electricity", "전기", 30),
				item("entry_flow", "출입 동선", 40),
				item("night_access", "야간 접근성", 50),
				item("security", "보안", 60),
				item("management_convenience", "관리 편의", 70)));
		put(templates, "STORAGE_WORKSPACE", List.of(
				item("loading", "상하차", 10),
				item("vehicle_access", "차량 접근", 20),
				item("ceiling_height", "층고", 30),
				item("electric_capacity", "전기 용량", 40),
				item("ventilation", "환기", 50),
				item("noise_risk", "소음 가능성", 60),
				item("security", "보안", 70)));
		put(templates, "GENERAL", List.of(
				item("front_exposure", "전면 노출", 10),
				item("water_supply", "급배수", 20),
				item("electricity", "전기", 30),
				item("restroom", "화장실", 40),
				item("parking", "주차", 50),
				item("sign", "간판", 60),
				item("special_notes", "특이사항", 70)));
		return Map.copyOf(templates);
	}

	private static void put(
			Map<String, List<FacilityTemplateItem>> templates,
			String businessType,
			List<TemplateDefinition> definitions) {
		List<FacilityTemplateItem> items = new ArrayList<>();
		for (TemplateDefinition definition : definitions) {
			items.add(new FacilityTemplateItem(
					businessType,
					definition.itemKey(),
					definition.label(),
					definition.displayOrder(),
					true));
		}
		templates.put(businessType, List.copyOf(items));
	}

	private static TemplateDefinition item(String itemKey, String label, int displayOrder) {
		return new TemplateDefinition(itemKey, label, displayOrder);
	}

	private record TemplateDefinition(String itemKey, String label, int displayOrder) {
	}
}
