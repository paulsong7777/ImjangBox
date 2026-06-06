package com.imjangbox.facility;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local & !local-db")
class LocalFacilityTemplateMapper implements FacilityTemplateMapper {

	private final FacilityTemplateProperties properties;

	LocalFacilityTemplateMapper(FacilityTemplateProperties properties) {
		this.properties = properties;
	}

	@Override
	public List<FacilityTemplateItem> findTemplateItemsByBusinessType(String businessType) {
		return properties.getItems().stream()
				.map(FacilityTemplateProperties.Item::toTemplateItem)
				.filter(item -> item.businessType().equals(businessType))
				.sorted(Comparator
						.comparingInt(FacilityTemplateItem::displayOrder)
						.thenComparing(FacilityTemplateItem::itemKey))
				.toList();
	}

	@Override
	public List<String> findBusinessTypes() {
		LinkedHashSet<String> businessTypes = new LinkedHashSet<>();
		for (FacilityTemplateProperties.Item item : properties.getItems()) {
			businessTypes.add(item.getBusinessType());
		}
		return List.copyOf(businessTypes);
	}
}
