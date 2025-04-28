CREATE TABLE IF NOT EXISTS country
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    code       VARCHAR(10)  NOT NULL UNIQUE,
    name       VARCHAR(255) NOT NULL,
    population BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS panel
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                 VARCHAR(255) NOT NULL,
    installation_cost    DOUBLE       NOT NULL,
    production_per_panel DOUBLE       NOT NULL,
    description          TEXT,
    efficiency           VARCHAR(50),
    warranty             VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS country_panel
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_id BIGINT NOT NULL,
    panel_id   BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (country_id) REFERENCES country (id),
    FOREIGN KEY (panel_id) REFERENCES panel (id)
);