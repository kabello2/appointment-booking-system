CREATE TABLE branches
(
    id UUID PRIMARY KEY,

    branch_code VARCHAR(50) NOT NULL,

    name VARCHAR(255) NOT NULL,

    address_line1 VARCHAR(255),

    address_line2 VARCHAR(255),

    suburb VARCHAR(100),

    city VARCHAR(100),

    province VARCHAR(100),

    postal_code VARCHAR(20),

    contact_number VARCHAR(20),

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,

    created_by VARCHAR(100),

    updated_at TIMESTAMP,

    updated_by VARCHAR(100),

    version BIGINT,

    CONSTRAINT uk_branch_code
        UNIQUE (branch_code)
);

CREATE INDEX idx_branch_code
    ON branches(branch_code);

CREATE INDEX idx_branch_name
    ON branches(name);

CREATE INDEX idx_branch_active
    ON branches(active);