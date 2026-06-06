package com.imjangbox.inspection.web;

import java.io.IOException;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.imjangbox.facility.FacilityTemplateService;
import com.imjangbox.inspection.AttachmentValidationException;
import com.imjangbox.inspection.InspectionService;
import com.imjangbox.map.KakaoMapView;
import com.imjangbox.map.KakaoMapViewFactory;
import com.imjangbox.property.VerificationStatus;

@Controller
@RequestMapping("/broker/inspections")
public class BrokerInspectionController {

	private final InspectionService inspectionService;
	private final FacilityTemplateService facilityTemplateService;
	private final KakaoMapViewFactory kakaoMapViewFactory;

	public BrokerInspectionController(
			InspectionService inspectionService,
			FacilityTemplateService facilityTemplateService,
			KakaoMapViewFactory kakaoMapViewFactory) {
		this.inspectionService = inspectionService;
		this.facilityTemplateService = facilityTemplateService;
		this.kakaoMapViewFactory = kakaoMapViewFactory;
	}

	@ModelAttribute("verificationStatuses")
	VerificationStatus[] verificationStatuses() {
		return VerificationStatus.values();
	}

	@ModelAttribute("businessTypes")
	List<String> businessTypes() {
		return facilityTemplateService.findBusinessTypes();
	}

	@ModelAttribute("kakaoMap")
	KakaoMapView kakaoMap() {
		return kakaoMapViewFactory.brokerInspectionMap();
	}

	@GetMapping("/new")
	String newForm(
			@RequestParam(name = "businessType", required = false) String businessType,
			Model model) {
		InspectionForm form = new InspectionForm();
		form.setBusinessType(facilityTemplateService.normalizeBusinessType(businessType));
		populateFacilityTemplates(form);
		model.addAttribute("inspectionForm", form);
		model.addAttribute("formAction", "/broker/inspections");
		model.addAttribute("submitLabel", "저장");
		model.addAttribute("selectedBusinessType", form.getBusinessType());
		return "inspection/form";
	}

	@PostMapping
	String create(
			@Valid @ModelAttribute("inspectionForm") InspectionForm form,
			BindingResult bindingResult,
			@RequestParam(name = "attachments", required = false) List<MultipartFile> attachments,
			Model model) throws IOException {
		populateFacilityTemplates(form);
		if (bindingResult.hasErrors()) {
			populateFormModel(model, null, "/broker/inspections", "저장");
			model.addAttribute("selectedBusinessType", form.getBusinessType());
			return "inspection/form";
		}
		try {
			long inspectionId = inspectionService.create(form, safeAttachments(attachments));
			return "redirect:/broker/inspections/" + inspectionId + "/edit";
		}
		catch (AttachmentValidationException ex) {
			model.addAttribute("attachmentError", "첨부 파일을 확인해 주세요.");
			populateFormModel(model, null, "/broker/inspections", "저장");
			model.addAttribute("selectedBusinessType", form.getBusinessType());
			return "inspection/form";
		}
	}

	@GetMapping("/{inspectionId}/edit")
	String edit(@PathVariable long inspectionId, Model model) {
		InspectionForm form = inspectionService.findForm(inspectionId);
		populateFacilityTemplates(form);
		model.addAttribute("inspectionForm", form);
		populateFormModel(model, inspectionId, "/broker/inspections/" + inspectionId, "수정");
		model.addAttribute("selectedBusinessType", form.getBusinessType());
		return "inspection/form";
	}

	@PostMapping("/{inspectionId}")
	String update(
			@PathVariable long inspectionId,
			@Valid @ModelAttribute("inspectionForm") InspectionForm form,
			BindingResult bindingResult,
			@RequestParam(name = "attachments", required = false) List<MultipartFile> attachments,
			Model model) throws IOException {
		populateFacilityTemplates(form);
		if (bindingResult.hasErrors()) {
			populateFormModel(model, inspectionId, "/broker/inspections/" + inspectionId, "수정");
			model.addAttribute("selectedBusinessType", form.getBusinessType());
			return "inspection/form";
		}
		try {
			inspectionService.update(inspectionId, form, safeAttachments(attachments));
		}
		catch (AttachmentValidationException ex) {
			model.addAttribute("attachmentError", "첨부 파일을 확인해 주세요.");
			populateFormModel(model, inspectionId, "/broker/inspections/" + inspectionId, "수정");
			model.addAttribute("selectedBusinessType", form.getBusinessType());
			return "inspection/form";
		}
		return "redirect:/broker/inspections/" + inspectionId + "/edit";
	}

	private void populateFormModel(Model model, Long inspectionId, String formAction, String submitLabel) {
		model.addAttribute("inspectionId", inspectionId);
		model.addAttribute("formAction", formAction);
		model.addAttribute("submitLabel", submitLabel);
	}

	private List<MultipartFile> safeAttachments(List<MultipartFile> attachments) {
		return attachments == null ? List.of() : attachments;
	}

	private void populateFacilityTemplates(InspectionForm form) {
		form.setBusinessType(facilityTemplateService.normalizeBusinessType(form.getBusinessType()));
		form.applyFacilityTemplates(facilityTemplateService.findItemsForBusinessType(form.getBusinessType()));
	}
}
