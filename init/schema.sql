CREATE TABLE gambler (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    wallet_balance NUMERIC(12,2) NOT NULL DEFAULT 0,
    refresh_token TEXT,
    refresh_token_expiry TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE TABLE bet (
    id SERIAL PRIMARY KEY,
    created_by INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    bet_price NUMERIC(4,2) NOT NULL CHECK (bet_price > 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    bet_ends_at TIMESTAMPTZ,
    status varchar(50) NOT NULL DEFAULT 'OPEN',

    CONSTRAINT fk_bet_created_by
        FOREIGN KEY (created_by)
        REFERENCES gambler(id)
        ON DELETE CASCADE
);

CREATE TABLE bet_option (
    id SERIAL PRIMARY KEY,
    bet_id INT NOT NULL,
    option_text VARCHAR(200) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_bet_option_bet
        FOREIGN KEY (bet_id)
        REFERENCES bet(id)
        ON DELETE CASCADE
);

CREATE TABLE joined_bet (
    id SERIAL PRIMARY KEY,
    gambler_id INT NOT NULL,
    bet_id INT NOT NULL,
    selected_option_id INT NOT NULL,
    joined_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_joined_bet_gambler
        FOREIGN KEY (gambler_id)
        REFERENCES gambler(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_joined_bet_bet
        FOREIGN KEY (bet_id)
        REFERENCES bet(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_joined_bet_option
        FOREIGN KEY (selected_option_id)
        REFERENCES bet_option(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_gambler_joined_bet
        UNIQUE (gambler_id, bet_id)
);
