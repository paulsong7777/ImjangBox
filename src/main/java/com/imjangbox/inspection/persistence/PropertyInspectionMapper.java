package com.imjangbox.inspection.persistence;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyInspectionMapper {

	Optional<PropertyInspectionRow> findById(long inspectionId);

	void insert(PropertyInspectionWriteRow row);

	int update(PropertyInspectionWriteRow row);

	void deleteContactLogs(long inspectionId);

	void insertContactLog(ContactLogWriteRow row);

	void insertFileAttachment(FileAttachmentWriteRow row);

	List<FileAttachmentWriteRow> findFileAttachments(long inspectionId);

	void deleteFacilityAnswers(long inspectionId);

	void insertFacilityAnswer(FacilityAnswerWriteRow row);

	List<FacilityAnswerWriteRow> findFacilityAnswers(long inspectionId);

	void upsertSearchIndex(SearchIndexWriteRow row);

	Optional<SearchIndexWriteRow> findSearchIndexByInspectionId(long inspectionId);
}
