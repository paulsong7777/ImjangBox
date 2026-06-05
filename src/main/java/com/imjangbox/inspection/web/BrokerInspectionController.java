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

import com.imjangbox.inspection.AttachmentValidationException;
import com.imjangbox.inspection.InspectionService;
import com.imjangbox.property.VerificationStatus;

@Controller
@RequestMapping("/broker/inspections")
public class BrokerInspectionController {

	private final InspectionService inspectionService;

	public BrokerInspectionController(InspectionService inspectionService) {
		this.inspectionService = inspectionService;
	}

	@ModelAttribute("verificationStatuses")
	VerificationStatus[] verificationStatuses() {
		return VerificationStatus.values();
	}

	@GetMapping("/new")
	String newForm(Model model) {
		model.addAttribute("inspectionForm", new InspectionForm());
		model.addAttribute("formAction", "/broker/inspections");
		model.addAttribute("submitLabel", "저장");
		return "inspection/form";
	}

	@PostMapping
	String create(
			@Valid @ModelAttribute("inspectionForm") InspectionForm form,
			BindingResult bindingResult,
			@RequestParam(name = "attachments", required = false) List<MultipartFile> attachments,
			Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			populateFormModel(model, null, "/broker/inspections", "저장");
			return "inspection/form";
		}
		try {
			long inspectionId = inspectionService.create(form, safeAttachments(attachments));
			return "redirect:/broker/inspections/" + inspectionId + "/edit";
		}
		catch (AttachmentValidationException ex) {
			model.addAttribute("attachmentError", "첨부 파일을 확인해 주세요.");
			populateFormModel(model, null, "/broker/inspections", "저장");
			return "inspection/form";
		}
	}

	@GetMapping("/{inspectionId}/edit")
	String edit(@PathVariable long inspectionId, Model model) {
		model.addAttribute("inspectionForm", inspectionService.findForm(inspectionId));
		populateFormModel(model, inspectionId, "/broker/inspections/" + inspectionId, "수정");
		return "inspection/form";
	}

	@PostMapping("/{inspectionId}")
	String update(
			@PathVariable long inspectionId,
			@Valid @ModelAttribute("inspectionForm") InspectionForm form,
			BindingResult bindingResult,
			@RequestParam(name = "attachments", required = false) List<MultipartFile> attachments,
			Model model) throws IOException {
		if (bindingResult.hasErrors()) {
			populateFormModel(model, inspectionId, "/broker/inspections/" + inspectionId, "수정");
			return "inspection/form";
		}
		try {
			inspectionService.update(inspectionId, form, safeAttachments(attachments));
		}
		catch (AttachmentValidationException ex) {
			model.addAttribute("attachmentError", "첨부 파일을 확인해 주세요.");
			populateFormModel(model, inspectionId, "/broker/inspections/" + inspectionId, "수정");
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
}
