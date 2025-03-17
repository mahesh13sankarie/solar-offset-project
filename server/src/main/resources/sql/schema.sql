-- Country 테이블이 존재하지 않으면 생성
CREATE TABLE IF NOT EXISTS country (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       code VARCHAR(10) NOT NULL UNIQUE,
                                       name VARCHAR(255) NOT NULL,
                                       population BIGINT,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
