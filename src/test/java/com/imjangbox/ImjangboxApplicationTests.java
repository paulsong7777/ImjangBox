package com.imjangbox;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import com.imjangbox.facility.FacilityTemplateMapper;
import com.imjangbox.inspection.persistence.PropertyInspectionMapper;
import com.imjangbox.share.ShareSnapshotMapper;

@SpringBootTest
@ActiveProfiles("test")
class ImjangboxApplicationTests {

	@MockitoBean
	private PropertyInspectionMapper propertyInspectionMapper;

	@MockitoBean
	private FacilityTemplateMapper facilityTemplateMapper;

	@MockitoBean
	private ShareSnapshotMapper shareSnapshotMapper;

	@Test
	void contextLoads() {
	}

}
