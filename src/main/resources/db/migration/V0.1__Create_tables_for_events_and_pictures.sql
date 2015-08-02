CREATE TABLE IF NOT EXISTS events (
  id VARCHAR(50) PRIMARY KEY,
  title TEXT NOT NULL,
  date DATE
);

CREATE TABLE IF NOT EXISTS pictures (
  id VARCHAR(50) PRIMARY KEY,
  title TEXT NOT NULL,
  path TEXT,
  thumb TEXT
);

CREATE TABLE IF NOT EXISTS event_picture (
  id_event VARCHAR(50),
  id_picture VARCHAR(50),
  PRIMARY KEY (id_event, id_picture),
  FOREIGN KEY (id_event) REFERENCES events (id),
  FOREIGN KEY (id_picture) REFERENCES pictures (id)
);
