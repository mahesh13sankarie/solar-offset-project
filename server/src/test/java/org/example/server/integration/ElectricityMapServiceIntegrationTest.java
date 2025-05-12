package org.example.server.integration;

import org.example.server.dto.CarbonIntensityResponseDTO;
import org.example.server.dto.ElectricityMapCredentialDTO;
import org.example.server.dto.ElectricityMapPropertiesDTO;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.Country;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.repository.CarbonIntensityRepository;
import org.example.server.repository.CountryRepository;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.example.server.service.electricitymap.ElectricityMapService;
import org.example.server.service.electricitymap.ElectricityMapServiceImpl;
import org.example.server.service.external.ElectricityMapWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ElectricityMapService.
 * <p>
 * These tests make actual API calls to the Electricity Map service using
 * credentials from application-test.yml. They validate the full integration
 * from API connection through data processing to database storage.
 * <p>
 * Run these tests selectively as they depend on external services and
 * may consume API quotas.
 */
@SpringBootTest
@ActiveProfiles("test")
@Tag("integration")
public class ElectricityMapServiceIntegrationTest {

    @Autowired
    private ElectricityMapService electricityMapService;

    @Autowired
    private ElectricityMapWebClient electricityMapWebClient;

    @Autowired
    private CarbonIntensityRepository carbonIntensityRepository;

    @Autowired
    private ElectricityBreakdownRepository electricityBreakdownRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ElectricityMapPropertiesDTO properties;

    @BeforeEach
    void setUp() {
        // Create Country entities for the test credentials if they don't exist
        for (ElectricityMapCredentialDTO credential : properties.getCredentials()) {
            Optional<Country> existingCountry = countryRepository.findByCode(credential.getCountryCode());
            if (existingCountry.isEmpty()) {
                Country country = Country.builder()
                        .code(credential.getCountryCode())
                        .name(credential.getCountryCode() + " Country")
                        .population(1000000L)
                        .build();
                countryRepository.save(country);
            }
        }
    }

    /**
     * Tests that carbon intensity data can be successfully fetched from
     * the real API and saved to the database.
     */
    @Test
    void testSaveCarbonIntensity() {
        // Get first credential to test with
        ElectricityMapCredentialDTO credential = properties.getCredentials().get(0);
        assertNotNull(credential, "Test requires at least one credential in application.yml");

        // Call the service method that makes API request and saves to database
        ((ElectricityMapServiceImpl) electricityMapService).processCarbonIntensity(credential);

        // Verify data was saved to database
        Optional<CarbonIntensity> result = carbonIntensityRepository.findByCountryCode(credential.getCountryCode());

        assertTrue(result.isPresent(), "Carbon intensity data should be saved to database");
        assertNotNull(result.get().getCarbonIntensity(), "Carbon intensity value should not be null");
        assertNotNull(result.get().getDatetime(), "Datetime should not be null");
    }

    /**
     * Tests that electricity breakdown data can be successfully fetched
     * from the real API and saved to the database.
     */
    @Test
    void testSaveElectricityBreakdown() {
        // Get first credential to test with
        ElectricityMapCredentialDTO credential = properties.getCredentials().get(0);
        assertNotNull(credential, "Test requires at least one credential in application.yml");

        // Call the service method that makes API request and saves to database
        ((ElectricityMapServiceImpl) electricityMapService).processElectricityBreakdownData(credential);

        // Verify data was saved to database
        Optional<ElectricityBreakdown> result = electricityBreakdownRepository.findByZone(credential.getCountryCode());

        assertTrue(result.isPresent(), "Electricity breakdown data should be saved to database");
        assertNotNull(result.get().getFossilFreePercentage(), "Fossil free percentage should not be null");
        assertNotNull(result.get().getRenewablePercentage(), "Renewable percentage should not be null");
    }

    /**
     * Tests direct API client connection without database interaction.
     * This validates the web client configuration and API communication.
     */
    @Test
    void testDirectApiConnection() {
        // Get first credential to test with
        ElectricityMapCredentialDTO credential = properties.getCredentials().get(0);
        assertNotNull(credential, "Test requires at least one credential in application.yml");

        // Make direct API call
        CarbonIntensityResponseDTO response = electricityMapWebClient.fetchCarbonIntensity(credential);

        // Verify response data
        assertNotNull(response, "API response should not be null");
        assertEquals(credential.getCountryCode(), response.zone(), "Response zone should match request country code");
        assertNotNull(response.carbonIntensity(), "Carbon intensity should not be null");
    }
}