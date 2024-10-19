CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    frequency INT NOT NULL,
    preference VARCHAR(255) NOT NULL DEFAULT 'words',
    last_sent_time BIGINT NOT NULL,
    active BOOLEAN DEFAULT TRUE
);