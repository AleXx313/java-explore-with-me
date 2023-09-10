DROP TABLE IF EXISTS endpoint_hits;

CREATE TABLE IF NOT EXISTS endpoint_hits(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR(255) NOT NULL,
    uri VARCHAR(255) NOT NULL,
    ip VARCHAR(16) NOT NULL,
    hit_time TIMESTAMP NOT NULL
);