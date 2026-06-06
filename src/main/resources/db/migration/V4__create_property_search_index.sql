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
    indexed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (inspection_id),
    KEY idx_property_search_index_business_type (business_type),
    KEY idx_property_search_index_verification_status (verification_status),
    KEY idx_property_search_index_coordinates (latitude, longitude),
    FULLTEXT KEY ft_property_search_index_text (search_text),
    CONSTRAINT fk_property_search_index_inspection FOREIGN KEY (inspection_id)
        REFERENCES property_inspections (inspection_id)
);
