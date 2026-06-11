CREATE TABLE public_share_snapshot_facilities (
    share_facility_snapshot_id BIGINT NOT NULL AUTO_INCREMENT,
    share_id VARCHAR(64) NOT NULL,
    display_order INT NOT NULL,
    label VARCHAR(200) NOT NULL,
    answer VARCHAR(500) NOT NULL,
    PRIMARY KEY (share_facility_snapshot_id),
    KEY idx_public_share_snapshot_facilities_share (share_id),
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
    KEY idx_public_share_snapshot_images_share (share_id),
    CONSTRAINT fk_public_share_snapshot_images_share FOREIGN KEY (share_id)
        REFERENCES public_share_snapshots (share_id)
);
