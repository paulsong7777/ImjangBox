package com.imjangbox.facility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("imjangbox.facility-templates")
public class FacilityTemplateProperties {

	private List<Item> items = new ArrayList<>();

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public static class Item {
		private String businessType;
		private String itemKey;
		private String label;
		private int displayOrder;
		private boolean customerVisible;

		FacilityTemplateItem toTemplateItem() {
			return new FacilityTemplateItem(businessType, itemKey, label, displayOrder, customerVisible);
		}

		public String getBusinessType() { return businessType; }
		public void setBusinessType(String businessType) { this.businessType = businessType; }
		public String getItemKey() { return itemKey; }
		public void setItemKey(String itemKey) { this.itemKey = itemKey; }
		public String getLabel() { return label; }
		public void setLabel(String label) { this.label = label; }
		public int getDisplayOrder() { return displayOrder; }
		public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
		public boolean isCustomerVisible() { return customerVisible; }
		public void setCustomerVisible(boolean customerVisible) { this.customerVisible = customerVisible; }
	}
}
