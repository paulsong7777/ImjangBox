CREATE TABLE share_snapshot_audit_logs (
    share_snapshot_audit_log_id BIGINT NOT NULL AUTO_INCREMENT,
    share_id VARCHAR(64) NOT NULL,
    inspection_id BIGINT NOT NULL,
    action VARCHAR(32) NOT NULL,
    actor_username VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (share_snapshot_audit_log_id),
    KEY idx_share_snapshot_audit_logs_share (share_id),
    KEY idx_share_snapshot_audit_logs_inspection (inspection_id),
    CONSTRAINT fk_share_snapshot_audit_logs_share FOREIGN KEY (share_id)
        REFERENCES public_share_snapshots (share_id),
    CONSTRAINT fk_share_snapshot_audit_logs_inspection FOREIGN KEY (inspection_id)
        REFERENCES property_inspections (inspection_id),
    CONSTRAINT ck_share_snapshot_audit_logs_action CHECK (
        action IN ('CREATED', 'UPDATED')
    )
);
