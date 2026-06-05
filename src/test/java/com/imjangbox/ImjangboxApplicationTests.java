package com.imjangbox;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import com.imjangbox.inspection.persistence.PropertyInspectionMapper;

@SpringBootTest
@ActiveProfiles("test")
class ImjangboxApplicationTests {

	@MockitoBean
	private PropertyInspectionMapper propertyInspectionMapper;

	@Test
	void contextLoads() {
	}

}
