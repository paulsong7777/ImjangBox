package com.imjangbox.inspection;

public class InspectionNotFoundException extends RuntimeException {

	public InspectionNotFoundException(long inspectionId) {
		super("Inspection not found: " + inspectionId);
	}
}
