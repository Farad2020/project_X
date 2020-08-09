CREATE TABLE Management_Staff(
    id UUID PRIMARY KEY NOT NULL,
    name VARCHAR(100),
    surname VARCHAR(100),
    lastname VARCHAR(100),
    login VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telephone VARCHAR(100),
    is_account_non_expired BOOLEAN NOT NULL,
    is_account_non_locked BOOLEAN NOT NULL,
    is_credentials_non_expired BOOLEAN NOT NULL,
    is_enabled BOOLEAN NOT NULL,
    company_id UUID REFERENCES Companies(id) NOT NULL
);