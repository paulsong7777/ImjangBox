ALTER TABLE property_inspections
    ADD COLUMN area_square_meters VARCHAR(32),
    ADD COLUMN business_fit_memo TEXT,
    ADD COLUMN condition_memo TEXT;

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
