-- Insert into country table only if it's empty
INSERT INTO country (code, name, population, updated_at, created_at)
SELECT t.code, t.name, t.population, NOW(), NOW()
FROM (
    SELECT 'GB' as code, 'United Kingdom' as name, 67000000 as population UNION ALL
    SELECT 'FR', 'France', 67000000 UNION ALL
    SELECT 'NO', 'Norway', 5400000 UNION ALL
    SELECT 'ZA', 'South Africa', 59000000 UNION ALL
    SELECT 'CA', 'Canada', 38000000
) t
WHERE NOT EXISTS (SELECT 1 FROM country LIMIT 1)
ON DUPLICATE KEY UPDATE name = VALUES(name),
                        population = VALUES(population),
                        updated_at = NOW();

-- Insert into panel table only if it's empty
INSERT INTO panel (name, installation_cost, production_per_panel, description)
SELECT t.name, t.cost, t.production, t.description
FROM (
    SELECT 'SunPower Maxeon 7DC 445W' as name, 400.00 as cost, 445.00 as production,
        'Top-tier efficiency, extremely durable (40-year warranty)' as description UNION ALL
    SELECT 'Project Solar Evo Max Super Series 480W', 375.00, 480.00,
        'Great customer satisfaction (94.4%), lifetime warranty' UNION ALL
    SELECT 'AIKO ABC Neostar 3N54 495W', 390.00, 495.00,
        'Highest production capacity on this list, advanced cell technology, standard warranty coverage, efficient for high-demand households' UNION ALL
    SELECT 'Perlight PLM-435 DH8N 450W', 340.00, 450.00,
        'Balanced performance and affordability, moderate durability, less well-known brand but strong specs' UNION ALL
    SELECT 'REC Alpha Pure RX Series 470W', 370.00, 470.00,
        'Good overall performance, solid durability and sustainability rating, aesthetic all-black design'
) t 
WHERE NOT EXISTS (SELECT 1 FROM panel LIMIT 1)
ON DUPLICATE KEY UPDATE installation_cost = VALUES(installation_cost),
                        production_per_panel = VALUES(production_per_panel),
                        description = VALUES(description);

-- Insert into country_panel table only if it's empty
INSERT INTO country_panel (country_id, panel_id, created_at, updated_at)
SELECT c.id, p.id, NOW(), NOW()
FROM country c
JOIN panel p ON 1=0 -- Actual join conditions are set in the conditions below
   OR (c.code = 'GB' AND p.name IN ('SunPower Maxeon 7DC 445W', 'Project Solar Evo Max Super Series 480W', 'REC Alpha Pure RX Series 470W'))
   OR (c.code = 'FR' AND p.name IN ('AIKO ABC Neostar 3N54 495W', 'Perlight PLM-435 DH8N 450W'))
   OR (c.code = 'NO' AND p.name IN ('SunPower Maxeon 7DC 445W', 'Project Solar Evo Max Super Series 480W', 'Perlight PLM-435 DH8N 450W'))
   OR (c.code = 'ZA' AND p.name IN ('Project Solar Evo Max Super Series 480W', 'AIKO ABC Neostar 3N54 495W'))
   OR (c.code = 'CA' AND p.name IN ('SunPower Maxeon 7DC 445W', 'AIKO ABC Neostar 3N54 495W', 'REC Alpha Pure RX Series 470W'))
WHERE NOT EXISTS (SELECT 1 FROM country_panel LIMIT 1);