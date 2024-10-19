CREATE TABLE grammar_rules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tense_name VARCHAR(255) NOT NULL,
    rule_formula TEXT,
    rule_explanation TEXT,
    example_1 TEXT,
    example_2 TEXT,
    example_3 TEXT
);