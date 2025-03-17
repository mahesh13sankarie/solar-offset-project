INSERT INTO country (code, name, population, updated_at, created_at) VALUES
                                                                         ('GB', 'United Kingdom', 67000000, NOW(), NOW()),
                                                                         ('FR', 'France', 67000000, NOW(), NOW()),
                                                                         ('NO', 'Norway', 5400000, NOW(), NOW()),
                                                                         ('ZA', 'South Africa', 59000000, NOW(), NOW()),
                                                                         ('CA', 'Canada', 38000000, NOW(), NOW())
ON DUPLICATE KEY UPDATE
                     name = VALUES(name),
                     population = VALUES(population),
                     updated_at = NOW();