package com.imjangbox.facility;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FacilityTemplateService {

	private static final String FALLBACK_BUSINESS_TYPE = "GENERAL";

	private final FacilityTemplateMapper mapper;

	public FacilityTemplateService(FacilityTemplateMapper mapper) {
		this.mapper = mapper;
	}

	public List<FacilityTemplateItem> findItemsForBusinessType(String businessType) {
		return mapper.findTemplateItemsByBusinessType(normalizeBusinessType(businessType));
	}

	public List<String> findBusinessTypes() {
		return mapper.findBusinessTypes();
	}

	public String defaultBusinessType() {
		return findBusinessTypes().stream().findFirst().orElse(FALLBACK_BUSINESS_TYPE);
	}

	public String normalizeBusinessType(String businessType) {
		if (businessType == null || businessType.isBlank()) {
			return defaultBusinessType();
		}
		return businessType.trim().toUpperCase();
	}
}
