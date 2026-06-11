package com.imjangbox.inspection.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;

import com.imjangbox.share.ShareSnapshotMapper;

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
				.contains("insertFacilityAnswer")
				.contains("findFacilityAnswers")
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
	void phaseThreeMigrationAddsFacilityTemplateDefinitionsSeparateFromAnswers() throws Exception {
		String sql = Files.readString(
				Path.of("src/main/resources/db/migration/V3__create_facility_check_templates.sql"),
				StandardCharsets.UTF_8);

		assertThat(sql)
				.contains("CREATE TABLE facility_check_templates")
				.contains("business_type")
				.contains("item_key")
				.contains("display_order")
				.contains("customer_visible")
				.contains("UNIQUE KEY uq_facility_check_templates_business_item");
		assertThat(sql)
				.doesNotContain("answer VARCHAR")
				.doesNotContain("inspection_id BIGINT NOT NULL");
	}

	@Test
	void phaseThreeSearchIndexMigrationKeepsIndexSeparateFromInternalNotes() throws Exception {
		String sql = Files.readString(
				Path.of("src/main/resources/db/migration/V4__create_property_search_index.sql"),
				StandardCharsets.UTF_8);

		assertThat(sql)
				.contains("CREATE TABLE property_search_index")
				.contains("inspection_id")
				.contains("public_address_summary")
				.contains("business_type")
				.contains("verification_status")
				.contains("search_text")
				.doesNotContain("private_memo")
				.doesNotContain("price_private_note")
				.doesNotContain("contact_log")
				.doesNotContain("internal_risk_memo");
	}

	@Test
	void mapperHasDedicatedSearchIndexStatementsWithoutPrivateFields() throws Exception {
		String mapper = Files.readString(
				Path.of("src/main/resources/mappers/PropertyInspectionMapper.xml"),
				StandardCharsets.UTF_8);

		assertThat(mapper)
				.contains("upsertSearchIndex")
				.contains("findSearchIndexByInspectionId")
				.contains("property_search_index");

		String searchIndexSection = mapper.substring(mapper.indexOf("upsertSearchIndex"));
		assertThat(searchIndexSection)
				.doesNotContain("private_memo")
				.doesNotContain("price_private_note")
				.doesNotContain("contact_log")
				.doesNotContain("internal_risk_memo");
	}

	@Test
	void mapperInterfaceIsRegisteredForMyBatisBootScanning() {
		assertThat(PropertyInspectionMapper.class).hasAnnotation(Mapper.class);
	}

	@Test
	void phaseFourMigrationAddsSnapshotChildrenWithoutInternalAttachmentFilenames() throws Exception {
		String sql = Files.readString(
				Path.of("src/main/resources/db/migration/V5__create_share_snapshot_children.sql"),
				StandardCharsets.UTF_8);

		assertThat(sql)
				.contains("CREATE TABLE public_share_snapshot_facilities")
				.contains("CREATE TABLE public_share_snapshot_images")
				.contains("share_id")
				.contains("display_order")
				.contains("content_type")
				.contains("source_storage_key")
				.doesNotContain("original_filename")
				.doesNotContain("private_memo")
				.doesNotContain("internal_risk_memo")
				.doesNotContain("contact_log");
	}

	@Test
	void phaseFiveMigrationAddsShareSnapshotAuditLogsWithoutPrivateFields() throws Exception {
		String sql = Files.readString(
				Path.of("src/main/resources/db/migration/V6__create_share_snapshot_audit_logs.sql"),
				StandardCharsets.UTF_8);

		assertThat(sql)
				.contains("CREATE TABLE share_snapshot_audit_logs")
				.contains("share_id")
				.contains("inspection_id")
				.contains("actor_username")
				.contains("CREATED")
				.contains("UPDATED")
				.doesNotContain("private_memo")
				.doesNotContain("price_private_note")
				.doesNotContain("contact_log")
				.doesNotContain("internal_risk_memo")
				.doesNotContain("source_storage_key");
	}

	@Test
	void shareSnapshotMapperReadsOnlyPublicSnapshotTables() throws Exception {
		String mapper = Files.readString(
				Path.of("src/main/resources/mappers/ShareSnapshotMapper.xml"),
				StandardCharsets.UTF_8);

		assertThat(mapper)
				.contains("com.imjangbox.share.ShareSnapshotMapper")
				.contains("public_share_snapshots")
				.contains("public_share_snapshot_facilities")
				.contains("public_share_snapshot_images")
				.contains("share_snapshot_audit_logs")
				.contains("public_address_summary")
				.contains("source_storage_key")
				.contains("insertAuditLog")
				.doesNotContain("internal_road_address")
				.doesNotContain("internal_detail_address")
				.doesNotContain("private_memo")
				.doesNotContain("price_private_note")
				.doesNotContain("stakeholder")
				.doesNotContain("contact_log")
				.doesNotContain("internal_risk_memo")
				.doesNotContain("property_search_index")
				.doesNotContain("original_filename");
	}

	@Test
	void shareMapperInterfaceIsRegisteredForMyBatisBootScanning() {
		assertThat(ShareSnapshotMapper.class).hasAnnotation(Mapper.class);
	}
}
