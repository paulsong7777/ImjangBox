package com.imjangbox.inspection.persistence;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PropertyInspectionMapper {

	Optional<PropertyInspectionRow> findById(long inspectionId);
}
