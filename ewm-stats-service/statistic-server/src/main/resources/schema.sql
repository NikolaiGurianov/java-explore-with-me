create TABLE IF NOT EXISTS endpointhits (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app VARCHAR(255) NOT NULL,
  uri VARCHAR(255) NOT NULL,
  ip VARCHAR(15) NOT NULL,
  timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL
);