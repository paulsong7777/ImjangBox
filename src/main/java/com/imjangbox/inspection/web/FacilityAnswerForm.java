package com.imjangbox.inspection.web;

import com.imjangbox.facility.FacilityTemplateItem;
import com.imjangbox.inspection.persistence.FacilityAnswerWriteRow;

public class FacilityAnswerForm {

	private String templateItemKey;
	private String businessType;
	private String label;
	private String answer;
	private boolean customerVisible;

	public static FacilityAnswerForm fromTemplateItem(FacilityTemplateItem item) {
		FacilityAnswerForm form = new FacilityAnswerForm();
		form.setTemplateItemKey(item.itemKey());
		form.setBusinessType(item.businessType());
		form.setLabel(item.label());
		form.setCustomerVisible(item.customerVisible());
		return form;
	}

	public static FacilityAnswerForm fromWriteRow(FacilityAnswerWriteRow row) {
		FacilityAnswerForm form = new FacilityAnswerForm();
		form.setTemplateItemKey(row.templateItemKey());
		form.setBusinessType(row.businessType());
		form.setLabel(row.label());
		form.setAnswer(row.answer());
		form.setCustomerVisible(row.customerVisible());
		return form;
	}

	public FacilityAnswerWriteRow toWriteRow(long inspectionId) {
		return new FacilityAnswerWriteRow(
				inspectionId,
				templateItemKey,
				businessType,
				label,
				answer,
				customerVisible);
	}

	public boolean hasAnswer() {
		return answer != null && !answer.isBlank();
	}

	public String getTemplateItemKey() { return templateItemKey; }
	public void setTemplateItemKey(String templateItemKey) { this.templateItemKey = templateItemKey; }
	public String getBusinessType() { return businessType; }
	public void setBusinessType(String businessType) { this.businessType = businessType; }
	public String getLabel() { return label; }
	public void setLabel(String label) { this.label = label; }
	public String getAnswer() { return answer; }
	public void setAnswer(String answer) { this.answer = answer; }
	public boolean isCustomerVisible() { return customerVisible; }
	public void setCustomerVisible(boolean customerVisible) { this.customerVisible = customerVisible; }
}
