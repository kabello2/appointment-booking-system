CREATE TABLE appointments
(
    id UUID PRIMARY KEY,

    appointment_number VARCHAR(50) NOT NULL,

    customer_id UUID NOT NULL,

    branch_id UUID NOT NULL,

    service_offering_id UUID NOT NULL,

    appointment_date DATE NOT NULL,

    start_time TIME NOT NULL,

    end_time TIME NOT NULL,

    status VARCHAR(50) NOT NULL,

    notes VARCHAR(1000),

    created_at TIMESTAMP NOT NULL,

    created_by VARCHAR(100),

    updated_at TIMESTAMP,

    updated_by VARCHAR(100),

    version BIGINT NOT NULL DEFAULT 0,

    consultant_id UUID,

    CONSTRAINT uk_appointment_number
        UNIQUE (appointment_number),

    CONSTRAINT fk_appointment_customer
        FOREIGN KEY (customer_id)
            REFERENCES users(id),

    CONSTRAINT fk_appointment_branch
        FOREIGN KEY (branch_id)
            REFERENCES branches(id),

    CONSTRAINT fk_appointment_service
        FOREIGN KEY (service_offering_id)
            REFERENCES service_offerings(id),

    CONSTRAINT fk_appointment_consultant
        FOREIGN KEY (consultant_id)
            REFERENCES users(id)
);

CREATE INDEX idx_appointment_date
    ON appointments(appointment_date);

CREATE INDEX idx_appointment_customer
    ON appointments(customer_id);

CREATE INDEX idx_appointment_consultant
    ON appointments(consultant_id);