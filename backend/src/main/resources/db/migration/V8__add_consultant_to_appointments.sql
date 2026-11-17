ALTER TABLE appointments
    ADD COLUMN consultant_id UUID;

ALTER TABLE appointments
    ADD CONSTRAINT fk_appointment_consultant
        FOREIGN KEY (consultant_id)
            REFERENCES users(id);

CREATE INDEX idx_appointment_consultant
    ON appointments(consultant_id);