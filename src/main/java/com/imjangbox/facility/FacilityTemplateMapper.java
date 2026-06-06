package com.imjangbox.facility;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FacilityTemplateMapper {

	List<FacilityTemplateItem> findTemplateItemsByBusinessType(String businessType);

	List<String> findBusinessTypes();
}
