CREATE TABLE service_offerings
(
    id UUID PRIMARY KEY,

    service_code VARCHAR(50) NOT NULL,

    name VARCHAR(255) NOT NULL,

    description VARCHAR(1000),

    duration_minutes INTEGER NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    created_by VARCHAR(100),

    updated_at TIMESTAMP,

    updated_by VARCHAR(100),

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_service_code
        UNIQUE (service_code)
);

CREATE INDEX idx_service_code
    ON service_offerings(service_code);

CREATE INDEX idx_service_active
    ON service_offerings(active);
