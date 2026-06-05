CREATE TABLE property_inspections (
    inspection_id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    verification_status VARCHAR(32) NOT NULL,
    internal_road_address VARCHAR(500) NOT NULL,
    internal_detail_address VARCHAR(500),
    internal_geocode_memo VARCHAR(500),
    public_address_summary VARCHAR(500) NOT NULL,
    public_landmark_hint VARCHAR(500),
    deposit_amount BIGINT NOT NULL,
    monthly_rent_amount BIGINT NOT NULL,
    premium_amount BIGINT NOT NULL,
    price_private_note TEXT,
    private_memo TEXT,
    internal_risk_memo TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (inspection_id),
    CONSTRAINT ck_property_inspections_verification_status CHECK (
        verification_status IN (
            'UNVERIFIED',
            'OWNER_CLAIM',
            'TENANT_CLAIM',
            'CO_BROKER_CLAIM',
            'AGENT_CHECKED',
            'DOCUMENT_CHECKED'
        )
    )
);

CREATE TABLE property_stakeholders (
    stakeholder_id BIGINT NOT NULL AUTO_INCREMENT,
    inspection_id BIGINT NOT NULL,
    role VARCHAR(64) NOT NULL,
    phone VARCHAR(64),
    verification_claim VARCHAR(64),
    PRIMARY KEY (stakeholder_id),
    CONSTRAINT fk_property_stakeholders_inspection FOREIGN KEY (inspection_id)
        REFERENCES property_inspections (inspection_id)
);

CREATE TABLE property_contact_logs (
    contact_log_id BIGINT NOT NULL AUTO_INCREMENT,
    inspection_id BIGINT NOT NULL,
    contacted_on DATE NOT NULL,
    content TEXT NOT NULL,
    PRIMARY KEY (contact_log_id),
    CONSTRAINT fk_property_contact_logs_inspection FOREIGN KEY (inspection_id)
        REFERENCES property_inspections (inspection_id)
);

CREATE TABLE property_facility_check_answers (
    facility_check_answer_id BIGINT NOT NULL AUTO_INCREMENT,
    inspection_id BIGINT NOT NULL,
    template_item_key VARCHAR(100) NOT NULL,
    business_type VARCHAR(100) NOT NULL,
    label VARCHAR(200) NOT NULL,
    answer VARCHAR(500),
    customer_visible BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (facility_check_answer_id),
    CONSTRAINT fk_property_facility_check_answers_inspection FOREIGN KEY (inspection_id)
        REFERENCES property_inspections (inspection_id)
);

CREATE TABLE public_share_snapshots (
    share_id VARCHAR(64) NOT NULL,
    inspection_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    verification_status VARCHAR(32) NOT NULL,
    verification_display_text VARCHAR(100) NOT NULL,
    public_address_summary VARCHAR(500) NOT NULL,
    public_landmark_hint VARCHAR(500),
    deposit_amount BIGINT NOT NULL,
    monthly_rent_amount BIGINT NOT NULL,
    premium_amount BIGINT NOT NULL,
    snapshot_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (share_id),
    CONSTRAINT fk_public_share_snapshots_inspection FOREIGN KEY (inspection_id)
        REFERENCES property_inspections (inspection_id)
);
