CREATE TABLE word (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    english_word VARCHAR(255) NOT NULL,
    trn VARCHAR(255) NOT NULL,
    transcription VARCHAR(255) DEFAULT NULL
);