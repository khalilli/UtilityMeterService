CREATE TABLE IF NOT EXISTS meter_readings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    meter_id UUID NOT NULL,
    reading_value NUMERIC NOT NULL CHECK (reading_value >= 0),
    consumption NUMERIC,
    reading_date DATE NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_meter_readings_meter
        FOREIGN KEY (meter_id)
        REFERENCES meters (id)
        ON DELETE CASCADE,

    CONSTRAINT uq_meter_reading_per_day
        UNIQUE (meter_id, reading_date)
);

CREATE INDEX IF NOT EXISTS idx_meter_reading_date ON meter_readings (meter_id, reading_date DESC);
