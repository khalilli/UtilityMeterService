CREATE TABLE IF NOT EXISTS meters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,
    meter_type meter_type NOT NULL,
    meter_number VARCHAR(255) NOT NULL UNIQUE,
    unit_of_measurement VARCHAR(255),
    installation_date DATE NOT NULL,
    status meter_status NOT NULL,

    CONSTRAINT fk_meters_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_id ON meters (user_id);

CREATE INDEX IF NOT EXISTS idx_meter_number ON meters (meter_number);
