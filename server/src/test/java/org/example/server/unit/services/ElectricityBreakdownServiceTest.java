package org.example.server.unit.services;

import org.example.server.entity.Country;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.repository.CountryRepository;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.example.server.service.electricitymap.ElectricityMapServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class ElectricityBreakdownServiceTest {

    @Autowired
    private ElectricityMapServiceImpl electricityMapService;

    @Autowired
    private ElectricityBreakdownRepository electricityBreakdownRepository;

    @Autowired
    private CountryRepository countryRepository;

    /**
     * Set up test data by creating required country entities.
     * This is necessary because electricity breakdown data has a foreign key
     * reference to country codes.
     */
    @BeforeEach
    void setUp() {
        // Create countries for the test with the same country codes
        // that the ElectricityMapService will try to use
        createCountryIfNotExists("GB", "United Kingdom");
        createCountryIfNotExists("FR", "France");
        createCountryIfNotExists("TH", "Thailand");
        createCountryIfNotExists("ZA", "South Africa");
        createCountryIfNotExists("CA", "Canada");
    }

    /**
     * Helper method to create a country entity if it doesn't already exist
     */
    private void createCountryIfNotExists(String code, String name) {
        Optional<Country> existingCountry = countryRepository.findByCode(code);
        if (existingCountry.isEmpty()) {
            Country country = Country.builder()
                    .code(code)
                    .name(name)
                    .population(1000L) // Dummy value for testing
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            countryRepository.save(country);
        }
    }

    @Test
    void shouldFetchAndSaveElectricityBreakdownData() {
        // Call scheduled method directly
        electricityMapService.saveElectricityBreakdownData();

        // Validate repository saved data (assert count or content)
        long count = electricityBreakdownRepository.count();
        assertThat(count).isGreaterThan(0);

        // Fetch and print all saved data for verification
        List<ElectricityBreakdown> allData = electricityBreakdownRepository.findAll();
        System.out.println("=== Fetched Electricity Breakdown Data ===");
        for (ElectricityBreakdown data : allData) {
            System.out.printf("Zone: %s, Updated At: %s, Solar Consumption: %.2f, Solar Production: %.2f, " +
                            "Fossil-Free%%: %d, Renewable%%: %d, Total Consumption: %d, Total Production: %d, " +
                            "Import Total: %d, Export Total: %d%n",
                    data.getZone(),
                    data.getUpdatedAt(),
                    data.getPowerConsumptionBreakdownSolar(),
                    data.getPowerProductionBreakdownSolar(),
                    data.getFossilFreePercentage(),
                    data.getRenewablePercentage(),
                    data.getPowerConsumptionTotal(),
                    data.getPowerProductionTotal(),
                    data.getPowerImportTotal(),
                    data.getPowerExportTotal());
        }
        System.out.println("==========================================");
    }
}