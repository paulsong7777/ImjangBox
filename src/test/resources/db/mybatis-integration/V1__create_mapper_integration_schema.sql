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
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    area_square_meters VARCHAR(32),
    business_fit_memo TEXT,
    condition_memo TEXT,
    business_type VARCHAR(100) NOT NULL DEFAULT 'GENERAL',
    PRIMARY KEY (inspection_id)
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

CREATE TABLE property_file_attachments (
    file_attachment_id BIGINT NOT NULL AUTO_INCREMENT,
    inspection_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    content_type VARCHAR(255),
    size_bytes BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (file_attachment_id),
    CONSTRAINT fk_property_file_attachments_inspection FOREIGN KEY (inspection_id)
        REFERENCES property_inspections (inspection_id)
);

CREATE TABLE property_search_index (
    inspection_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    public_address_summary VARCHAR(500) NOT NULL,
    public_landmark_hint VARCHAR(500),
    business_type VARCHAR(100) NOT NULL,
    verification_status VARCHAR(32) NOT NULL,
    area_square_meters VARCHAR(32),
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    search_text TEXT NOT NULL,
    indexed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (inspection_id),
    CONSTRAINT fk_property_search_index_inspection FOREIGN KEY (inspection_id)
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

CREATE TABLE public_share_snapshot_facilities (
    share_facility_snapshot_id BIGINT NOT NULL AUTO_INCREMENT,
    share_id VARCHAR(64) NOT NULL,
    display_order INT NOT NULL,
    label VARCHAR(200) NOT NULL,
    answer VARCHAR(500) NOT NULL,
    PRIMARY KEY (share_facility_snapshot_id),
    CONSTRAINT fk_public_share_snapshot_facilities_share FOREIGN KEY (share_id)
        REFERENCES public_share_snapshots (share_id)
);

CREATE TABLE public_share_snapshot_images (
    share_image_snapshot_id BIGINT NOT NULL AUTO_INCREMENT,
    share_id VARCHAR(64) NOT NULL,
    display_order INT NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    source_storage_key VARCHAR(500) NOT NULL,
    PRIMARY KEY (share_image_snapshot_id),
    CONSTRAINT fk_public_share_snapshot_images_share FOREIGN KEY (share_id)
        REFERENCES public_share_snapshots (share_id)
);

CREATE TABLE share_snapshot_audit_logs (
    share_snapshot_audit_log_id BIGINT NOT NULL AUTO_INCREMENT,
    share_id VARCHAR(64) NOT NULL,
    inspection_id BIGINT NOT NULL,
    action VARCHAR(32) NOT NULL,
    actor_username VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (share_snapshot_audit_log_id),
    CONSTRAINT fk_share_snapshot_audit_logs_share FOREIGN KEY (share_id)
        REFERENCES public_share_snapshots (share_id),
    CONSTRAINT fk_share_snapshot_audit_logs_inspection FOREIGN KEY (inspection_id)
        REFERENCES property_inspections (inspection_id)
);
