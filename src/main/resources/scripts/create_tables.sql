-- Create Account table
CREATE TABLE IF NOT EXISTS account (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    country VARCHAR(255)
);

-- Create Balance table
CREATE TABLE IF NOT EXISTS balance (
    id SERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    available_amount NUMERIC(19, 4),
    currency VARCHAR(3),
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account(id),
    UNIQUE (account_id, currency)
);

-- Create Transaction table
CREATE TABLE IF NOT EXISTS transaction (
    id SERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    amount NUMERIC(19, 4),
    currency VARCHAR(3),
    direction VARCHAR(10),
    description VARCHAR(255),
    balance_after_transaction NUMERIC(19, 4),
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account(id)
);
