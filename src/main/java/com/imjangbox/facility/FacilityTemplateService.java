package com.imjangbox.facility;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class FacilityTemplateService {

	private final FacilityTemplateMapper mapper;

	public FacilityTemplateService(FacilityTemplateMapper mapper) {
		this.mapper = mapper;
	}

	public List<FacilityTemplateItem> findItemsForBusinessType(String businessType) {
		String normalizedBusinessType = normalizeBusinessType(businessType);
		if (BusinessTypeCatalog.supports(normalizedBusinessType)) {
			return BusinessTypeCatalog.defaultFacilityTemplates(normalizedBusinessType);
		}
		return mapper.findTemplateItemsByBusinessType(normalizedBusinessType);
	}

	public List<String> findBusinessTypes() {
		List<String> configuredTypes = mapper.findBusinessTypes().stream()
				.map(BusinessTypeCatalog::normalize)
				.filter(type -> !BusinessTypeCatalog.supports(type))
				.toList();
		if (configuredTypes.isEmpty()) {
			return BusinessTypeCatalog.supportedTypes();
		}
		return java.util.stream.Stream
				.concat(BusinessTypeCatalog.supportedTypes().stream(), configuredTypes.stream())
				.distinct()
				.toList();
	}

	public List<BusinessTypeOption> findBusinessTypeOptions() {
		return findBusinessTypes().stream()
				.map(type -> new BusinessTypeOption(type, BusinessTypeCatalog.label(type)))
				.toList();
	}

	public String defaultBusinessType() {
		return findBusinessTypes().stream().findFirst().orElse(BusinessTypeCatalog.DEFAULT_BUSINESS_TYPE);
	}

	public String normalizeBusinessType(String businessType) {
		if (businessType == null || businessType.isBlank()) {
			return defaultBusinessType();
		}
		return BusinessTypeCatalog.normalize(businessType);
	}
}
