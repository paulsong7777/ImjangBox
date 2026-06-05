package com.imjangbox.inspection.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;

class PersistencePrivacyShapeTest {

	@Test
	void migrationSeparatesInternalRecordsFromPublicShareSnapshots() throws Exception {
		String sql = Files.readString(
				Path.of("src/main/resources/db/migration/V1__create_property_inspection_privacy_tables.sql"),
				StandardCharsets.UTF_8);

		assertThat(sql)
				.contains("property_inspections")
				.contains("internal_road_address")
				.contains("public_address_summary")
				.contains("private_memo")
				.contains("price_private_note")
				.contains("internal_risk_memo")
				.contains("property_stakeholders")
				.contains("phone")
				.contains("property_contact_logs")
				.contains("content")
				.contains("property_facility_check_answers")
				.contains("template_item_key")
				.contains("public_share_snapshots");
		assertThat(sql).containsOnlyOnce("CREATE TABLE property_facility_check_answers");

		String publicSnapshotSection = sql.substring(sql.indexOf("CREATE TABLE public_share_snapshots"));
		assertThat(publicSnapshotSection)
				.contains("public_address_summary")
				.doesNotContain("private_memo")
				.doesNotContain("price_private_note")
				.doesNotContain("stakeholder_phone")
				.doesNotContain("contact_log_content")
				.doesNotContain("internal_risk_memo")
				.doesNotContain("internal_road_address");
	}

	@Test
	void mapperShapeReturnsInternalRowsNotPublicSnapshots() throws Exception {
		String mapper = Files.readString(
				Path.of("src/main/resources/mappers/PropertyInspectionMapper.xml"),
				StandardCharsets.UTF_8);

		assertThat(mapper)
				.contains("com.imjangbox.inspection.persistence.PropertyInspectionMapper")
				.contains("PropertyInspectionRow")
				.contains("internal_road_address")
				.contains("public_address_summary")
				.contains("private_memo")
				.contains("<insert id=\"insert\"")
				.contains("<update id=\"update\"")
				.contains("insertContactLog")
				.contains("insertFileAttachment")
				.doesNotContain("PublicShareSnapshot");
	}

	@Test
	void phaseTwoMigrationAddsInspectionLedgerAndInternalAttachmentStorageShape() throws Exception {
		String sql = Files.readString(
				Path.of("src/main/resources/db/migration/V2__add_broker_inspection_ledger_fields.sql"),
				StandardCharsets.UTF_8);

		assertThat(sql)
				.contains("area_square_meters")
				.contains("business_fit_memo")
				.contains("condition_memo")
				.contains("property_file_attachments")
				.contains("storage_key")
				.contains("original_filename");
	}

	@Test
	void mapperInterfaceIsRegisteredForMyBatisBootScanning() {
		assertThat(PropertyInspectionMapper.class).hasAnnotation(Mapper.class);
	}
}
