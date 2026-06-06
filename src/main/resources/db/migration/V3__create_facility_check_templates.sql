ALTER TABLE property_inspections
    ADD COLUMN business_type VARCHAR(100) NOT NULL DEFAULT 'GENERAL';

CREATE TABLE facility_check_templates (
    facility_check_template_id BIGINT NOT NULL AUTO_INCREMENT,
    business_type VARCHAR(100) NOT NULL,
    item_key VARCHAR(100) NOT NULL,
    label VARCHAR(200) NOT NULL,
    display_order INT NOT NULL,
    customer_visible BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (facility_check_template_id),
    UNIQUE KEY uq_facility_check_templates_business_item (business_type, item_key)
);

INSERT INTO facility_check_templates (
    business_type,
    item_key,
    label,
    display_order,
    customer_visible
) VALUES
    ('CAFE', 'water_supply', '급배수 확인', 10, TRUE),
    ('CAFE', 'electric_capacity', '전기 용량 확인', 20, FALSE),
    ('RESTAURANT', 'exhaust_duct', '배기 덕트 확인', 10, FALSE),
    ('RESTAURANT', 'grease_trap', '그리스트랩 확인', 20, FALSE);
