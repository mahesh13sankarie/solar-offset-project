INSERT INTO country (code, name, population, updated_at, created_at)
VALUES ('GB', 'United Kingdom', 67000000, NOW(), NOW()),
       ('FR', 'France', 67000000, NOW(), NOW()),
       ('NO', 'Norway', 5400000, NOW(), NOW()),
       ('ZA', 'South Africa', 59000000, NOW(), NOW()),
       ('CA', 'Canada', 38000000, NOW(), NOW())
ON DUPLICATE KEY UPDATE name       = VALUES(name),
                        population = VALUES(population),
                        updated_at = NOW();

-- First, insert the panels
INSERT INTO panel (name, installation_cost, production_per_panel, description)
VALUES ('SunPower Maxeon 7DC 445W', 400.00, 445.00,
        'Top-tier efficiency, extremely durable (40-year warranty), compact and space-saving, excellent sustainability profile'),
       ('Project Solar Evo Max Super Series 480W', 375.00, 480.00,
        'Great customer satisfaction (94.4%), lifetime warranty, performs well in low light, limited eco-credentials'),
       ('AIKO ABC Neostar 3N54 495W', 390.00, 495.00,
        'Highest production capacity on this list, advanced cell technology, standard warranty coverage, efficient for high-demand households'),
       ('Perlight PLM-435 DH8N 450W', 340.00, 450.00,
        'Balanced performance and affordability, moderate durability, less well-known brand but strong specs'),
       ('REC Alpha Pure RX Series 470W', 370.00, 470.00,
        'Good overall performance, solid durability and sustainability rating, aesthetic all-black design')
ON DUPLICATE KEY UPDATE installation_cost    = VALUES(installation_cost),
                        production_per_panel = VALUES(production_per_panel),
                        description          = VALUES(description);

-- United Kingdom (GB): SunPower Maxeon 7DC, Project Solar Evo Max Super Series, REC Alpha Pure RX Series
INSERT INTO country_panel (country_id, panel_id, created_at, updated_at)
SELECT c.id, p.id, NOW(), NOW()
FROM country c,
     panel p
WHERE c.code = 'GB'
  AND p.name IN (
                 'SunPower Maxeon 7DC 445W',
                 'Project Solar Evo Max Super Series 480W',
                 'REC Alpha Pure RX Series 470W'
    )
  AND NOT EXISTS (SELECT 1
                  FROM country_panel cp
                  WHERE cp.country_id = c.id
                    AND cp.panel_id = p.id);

-- France (FR): AIKO ABC Neostar 3N54, Perlight PLM-435 DH8N
INSERT INTO country_panel (country_id, panel_id, created_at, updated_at)
SELECT c.id, p.id, NOW(), NOW()
FROM country c,
     panel p
WHERE c.code = 'FR'
  AND p.name IN (
                 'AIKO ABC Neostar 3N54 495W',
                 'Perlight PLM-435 DH8N 450W'
    )
  AND NOT EXISTS (SELECT 1
                  FROM country_panel cp
                  WHERE cp.country_id = c.id
                    AND cp.panel_id = p.id);

-- Norway (NO): SunPower Maxeon 7DC, Project Solar Evo Max Super Series, Perlight PLM-435 DH8N
INSERT INTO country_panel (country_id, panel_id, created_at, updated_at)
SELECT c.id, p.id, NOW(), NOW()
FROM country c,
     panel p
WHERE c.code = 'NO'
  AND p.name IN (
                 'SunPower Maxeon 7DC 445W',
                 'Project Solar Evo Max Super Series 480W',
                 'Perlight PLM-435 DH8N 450W'
    )
  AND NOT EXISTS (SELECT 1
                  FROM country_panel cp
                  WHERE cp.country_id = c.id
                    AND cp.panel_id = p.id);

-- South Africa (ZA): Project Solar Evo Max Super Series, AIKO ABC Neostar 3N54
INSERT INTO country_panel (country_id, panel_id, created_at, updated_at)
SELECT c.id, p.id, NOW(), NOW()
FROM country c,
     panel p
WHERE c.code = 'ZA'
  AND p.name IN (
                 'Project Solar Evo Max Super Series 480W',
                 'AIKO ABC Neostar 3N54 495W'
    )
  AND NOT EXISTS (SELECT 1
                  FROM country_panel cp
                  WHERE cp.country_id = c.id
                    AND cp.panel_id = p.id);

-- Canada (CA): SunPower Maxeon 7DC, AIKO ABC Neostar 3N54, REC Alpha Pure RX Series
INSERT INTO country_panel (country_id, panel_id, created_at, updated_at)
SELECT c.id, p.id, NOW(), NOW()
FROM country c,
     panel p
WHERE c.code = 'CA'
  AND p.name IN (
                 'SunPower Maxeon 7DC 445W',
                 'AIKO ABC Neostar 3N54 495W',
                 'REC Alpha Pure RX Series 470W'
    )
  AND NOT EXISTS (SELECT 1
                  FROM country_panel cp
                  WHERE cp.country_id = c.id
                    AND cp.panel_id = p.id);