package com.imjangbox.share;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imjangbox.facility.FacilityCheckAnswer;
import com.imjangbox.property.CommercialPropertyInspection;
import com.imjangbox.property.ContactLog;
import com.imjangbox.property.InternalAddress;
import com.imjangbox.property.Pricing;
import com.imjangbox.property.PublicAddress;
import com.imjangbox.property.Stakeholder;
import com.imjangbox.property.VerificationStatus;
import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

class PublicShareSnapshotPrivacyTest {

	private static final Set<String> DENIED_NAMES = Set.of(
			"private_memo",
			"privateMemo",
			"price_private_note",
			"pricePrivateNote",
			"stakeholder.phone",
			"phone",
			"contact_log.content",
			"content",
			"internal_risk_memo",
			"internalRiskMemo");

	private static final Set<String> DENIED_VALUES = Set.of(
			"PRIVATE_MEMO_VALUE",
			"PRICE_PRIVATE_NOTE_VALUE",
			"STAKEHOLDER_PHONE_VALUE",
			"CONTACT_LOG_CONTENT_VALUE",
			"INTERNAL_RISK_MEMO_VALUE",
			"INTERNAL_EXACT_ADDRESS_VALUE",
			"PRIVATE_FACILITY_ANSWER_VALUE");

	private static final Set<String> DENIED_TEMPLATE_TOKENS = Set.of(
			"private_memo",
			"privateMemo",
			"price_private_note",
			"pricePrivateNote",
			"stakeholder.phone",
			"contact_log.content",
			"internal_risk_memo",
			"internalRiskMemo",
			"internalAddress",
			"stakeholders",
			"contactLogs",
			"facilityChecks");

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void publicSnapshotTypeDoesNotContainInternalRecordShapes() {
		assertThat(componentNames(PublicShareSnapshot.class))
				.doesNotContainAnyElementsOf(DENIED_NAMES)
				.contains("publicAddress")
				.doesNotContain("internalAddress");

		assertThat(componentTypes(PublicShareSnapshot.class))
				.doesNotContain(
						CommercialPropertyInspection.class,
						InternalAddress.class,
						Pricing.class,
						FacilityCheckAnswer.class,
						Stakeholder.class,
						ContactLog.class);
	}

	@Test
	void publicProjectionSerializesOnlyAllowlistedFields() throws Exception {
		PublicShareSnapshot snapshot = PublicShareSnapshotFactory.from("share-1", internalRecord());

		String json = objectMapper.writeValueAsString(snapshot);

		assertThat(json)
				.contains("Customer-safe title")
				.contains("PUBLIC_DISTRICT_VALUE")
				.contains(VerificationStatus.AGENT_CHECKED.name());
		assertThat(json).doesNotContain(DENIED_NAMES.toArray(String[]::new));
		assertThat(json).doesNotContain(DENIED_VALUES.toArray(String[]::new));
	}

	@Test
	void publicTemplateRendersOnlyPublicSnapshotFields() {
		PublicShareSnapshot snapshot = PublicShareSnapshotFactory.from("share-1", internalRecord());
		Context context = new Context();
		context.setVariable("snapshot", snapshot);

		String rendered = templateEngine().process("share/public-card", context);

		assertThat(rendered)
				.contains("Customer-safe title")
				.contains("PUBLIC_DISTRICT_VALUE")
				.contains("현장 확인");
		assertThat(rendered).doesNotContain("Agent checked");
		assertThat(rendered).doesNotContain(DENIED_TEMPLATE_TOKENS.toArray(String[]::new));
		assertThat(rendered).doesNotContain(DENIED_VALUES.toArray(String[]::new));
	}

	private static CommercialPropertyInspection internalRecord() {
		return new CommercialPropertyInspection(
				1L,
				"Customer-safe title",
				new InternalAddress("INTERNAL_EXACT_ADDRESS_VALUE", "101", "private geocode memo"),
				new PublicAddress("PUBLIC_DISTRICT_VALUE", "near station"),
				new Pricing(100_000_000L, 6_000_000L, 30_000_000L, "PRICE_PRIVATE_NOTE_VALUE"),
				List.of(new FacilityCheckAnswer(
						"kitchen-ventilation",
						"restaurant",
						"Kitchen ventilation",
						"PRIVATE_FACILITY_ANSWER_VALUE",
						false)),
				List.of(new Stakeholder("owner", "STAKEHOLDER_PHONE_VALUE", "OWNER_CLAIM")),
				List.of(new ContactLog("2026-06-05", "CONTACT_LOG_CONTENT_VALUE")),
				"PRIVATE_MEMO_VALUE",
				"INTERNAL_RISK_MEMO_VALUE",
				VerificationStatus.AGENT_CHECKED);
	}

	private static List<String> componentNames(Class<? extends Record> recordType) {
		return List.of(recordType.getRecordComponents()).stream()
				.map(RecordComponent::getName)
				.toList();
	}

	private static List<Class<?>> componentTypes(Class<? extends Record> recordType) {
		return List.of(recordType.getRecordComponents()).stream()
				.map(RecordComponent::getType)
				.toList();
	}

	private static SpringTemplateEngine templateEngine() {
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode("HTML");
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(resolver);
		return templateEngine;
	}
}
