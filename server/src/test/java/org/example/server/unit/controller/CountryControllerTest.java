package org.example.server.unit.controller;

import org.example.server.controller.CountryController;
import org.example.server.dto.CountryDetailDTO;
import org.example.server.exception.DataNotFoundException;
import org.example.server.service.electricitymetrics.ElectricityMetricsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
@Import(TestSecurityConfig.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ElectricityMetricsService electricityMetricsService;

    @Test
    @WithMockUser
    @DisplayName("HTTP 200 OK: Returns list of countries with electricity metrics")
    void getAllElectricityMetrics_ReturnsListOfCountries() throws Exception {
        // Arrange
        CountryDetailDTO country1 = new CountryDetailDTO(
                "GB", "United Kingdom", 0.45, 0.85, 0.65, 28, 67000000L, Collections.emptyList());

        CountryDetailDTO country2 = new CountryDetailDTO(
                "FR", "France", 0.38, 0.92, 0.7, 32, 67000000L, Collections.emptyList());

        CountryDetailDTO country3 = new CountryDetailDTO(
                "TH", "Thailand", 0.2, 0.95, 0.8, 98, 5400000L, Collections.emptyList());

        CountryDetailDTO country4 = new CountryDetailDTO(
                "ZA", "South Africa", 0.78, 0.65, 0.9, 12, 59000000L, Collections.emptyList());

        CountryDetailDTO country5 = new CountryDetailDTO(
                "CA", "Canada", 0.35, 0.9, 0.6, 67, 38000000L, Collections.emptyList());

        List<CountryDetailDTO> countries = Arrays.asList(country1, country2, country3, country4, country5);

        when(electricityMetricsService.getAllElectricityData()).thenReturn(countries);

        // Act & Assert
        mockMvc.perform(get("/api/v1/countries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response[0].zone").value("GB"))
                .andExpect(jsonPath("$.response[0].country").value("United Kingdom"))
                .andExpect(jsonPath("$.response[1].zone").value("FR"))
                .andExpect(jsonPath("$.response[1].country").value("France"))
                .andExpect(jsonPath("$.response[2].zone").value("TH"))
                .andExpect(jsonPath("$.response[2].country").value("Thailand"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @WithMockUser
    @DisplayName("HTTP 200 OK: Returns specific country electricity metrics when valid country code provided")
    void getElectricityMetricsByCountry_ReturnsCountryDetails() throws Exception {
        // Arrange
        String countryCode = "GB";
        CountryDetailDTO country = new CountryDetailDTO(
                countryCode, "United Kingdom", 0.45, 0.85, 0.65, 28, 67000000L, Collections.emptyList());

        when(electricityMetricsService.getElectricityDataByCountry(countryCode)).thenReturn(country);

        // Act & Assert
        mockMvc.perform(get("/api/v1/countries/{countryCode}", countryCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.zone").value(countryCode))
                .andExpect(jsonPath("$.response.country").value("United Kingdom"))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @WithMockUser
    @DisplayName("HTTP 200 OK: Returns Thailand's electricity metrics with high renewable percentage")
    void getElectricityMetricsByThailand_ReturnsCountryDetails() throws Exception {
        // Arrange
        String countryCode = "TH";
        CountryDetailDTO country = new CountryDetailDTO(
                countryCode, "Thailand", 0.2, 0.95, 0.8, 98, 5400000L, Collections.emptyList());

        when(electricityMetricsService.getElectricityDataByCountry(countryCode)).thenReturn(country);

        // Act & Assert
        mockMvc.perform(get("/api/v1/countries/{countryCode}", countryCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.response.zone").value(countryCode))
                .andExpect(jsonPath("$.response.country").value("Thailand"))
                .andExpect(jsonPath("$.response.renewablePercentage").value(98))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @WithMockUser
    @DisplayName("HTTP 404 NOT_FOUND: Returns error response when country code doesn't exist")
    void getElectricityMetricsByCountry_WhenCountryNotFound_ReturnsErrorResponse() throws Exception {
        // Arrange
        String nonExistentCountryCode = "XYZ";

        when(electricityMetricsService.getElectricityDataByCountry(nonExistentCountryCode))
                .thenThrow(new DataNotFoundException("Country not found for code: " + nonExistentCountryCode));

        // Act & Assert
        mockMvc.perform(get("/api/v1/countries/{countryCode}", nonExistentCountryCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.response").doesNotExist())
                .andExpect(jsonPath("$.error.code").value("DATA-001"))
                .andExpect(jsonPath("$.error.message").value("Country not found for code: " + nonExistentCountryCode));
    }
}