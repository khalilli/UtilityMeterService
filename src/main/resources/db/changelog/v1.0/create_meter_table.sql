CREATE TABLE IF NOT EXISTS meters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id VARCHAR(255) NOT NULL,
    meter_type meter_type NOT NULL,
    meter_number VARCHAR(255) NOT NULL UNIQUE,
    unit_of_measurement VARCHAR(255),
    installation_date DATE NOT NULL,
    status meter_status NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_meters_user_id ON meters (user_id);

CREATE INDEX IF NOT EXISTS idx_meter_number ON meters (meter_number);
