CREATE TABLE notifications
(
    id UUID PRIMARY KEY,

    recipient VARCHAR(255) NOT NULL,

    subject VARCHAR(255) NOT NULL,

    message VARCHAR(3000) NOT NULL,

    channel VARCHAR(50) NOT NULL,

    status VARCHAR(50) NOT NULL,

    appointment_id UUID,

    created_at TIMESTAMP NOT NULL,

    created_by VARCHAR(100),

    updated_at TIMESTAMP,

    updated_by VARCHAR(100),

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_notification_appointment
        FOREIGN KEY (appointment_id)
            REFERENCES appointments(id)
);

CREATE INDEX idx_notification_status
    ON notifications(status);

CREATE INDEX idx_notification_channel
    ON notifications(channel);

CREATE INDEX idx_notification_appointment
    ON notifications(appointment_id);
