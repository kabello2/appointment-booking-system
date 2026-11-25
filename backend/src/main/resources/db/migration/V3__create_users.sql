CREATE TABLE users
(
    id UUID PRIMARY KEY,

    first_name VARCHAR(50) NOT NULL,

    last_name VARCHAR(50) NOT NULL,

    email VARCHAR(255) NOT NULL UNIQUE,

    phone_number VARCHAR(20) NOT NULL,

    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(50) NOT NULL,

    enabled BOOLEAN NOT NULL,

    account_locked BOOLEAN NOT NULL,

    email_verified BOOLEAN NOT NULL,

    failed_login_attempts INTEGER NOT NULL,

    locked_until TIMESTAMP,

    branch_id UUID,

    created_at TIMESTAMP NOT NULL,

    created_by VARCHAR(100),

    updated_at TIMESTAMP,

    updated_by VARCHAR(100),

    version BIGINT,

    CONSTRAINT fk_user_branch
        FOREIGN KEY (branch_id)
            REFERENCES branches(id)
);