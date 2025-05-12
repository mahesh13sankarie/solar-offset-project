package org.example.server.unit.services;

import org.example.server.dto.CountryDetailDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.entity.*;
import org.example.server.exception.DataNotFoundException;
import org.example.server.mapper.ElectricityMetricsMapper;
import org.example.server.repository.CarbonIntensityRepository;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.CountryRepository;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.example.server.service.electricitymetrics.ElectricityMetricsServiceImpl;
import org.example.server.utils.CalculationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author: astidhiyaa
 * @date: 08/05/25
 */
@ExtendWith(MockitoExtension.class)
public class ElectricityMetricsServiceTest {

    private static long A_MILLION = 1_000_000L;
    @InjectMocks
    private ElectricityMetricsServiceImpl electricityMetricsService;
    @Mock
    private CarbonIntensityRepository carbonIntensityRepository;
    @Mock
    private ElectricityBreakdownRepository electricityBreakdownRepository;
    @Mock
    private ElectricityMetricsMapper electricityMetricsMapper;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CountryPanelRepository countryPanelRepository;

    @Test
    void test_getAllElectricityData_success() {
        // given
        ElectricityBreakdown electricityBreakdown = buildBreakdown();
        List<ElectricityBreakdown> breakdowns = List.of(electricityBreakdown);

        CarbonIntensity carbonIntensity = buildCarbonIntensity("GB");
        List<CarbonIntensity> intensities = List.of(carbonIntensity);

        Country country = buildCountry("GB");
        List<Country> countries = List.of(country);

        try (MockedStatic<CalculationUtils> calculationUtils = mockStatic(CalculationUtils.class)) {
            CountryDetailDTO dto = electricityMetricsMapper.toDTO(electricityBreakdown, carbonIntensity, CalculationUtils.formatPopulation(country.getPopulation()));

            when(electricityBreakdownRepository.findAll()).thenReturn(breakdowns);
            when(carbonIntensityRepository.findAll()).thenReturn(intensities);
            when(countryRepository.findAll()).thenReturn(countries);
            calculationUtils.when(() -> CalculationUtils.formatPopulation(country.getPopulation())).thenReturn(A_MILLION);

            when(electricityMetricsMapper.toDTO(eq(electricityBreakdown), eq(carbonIntensity), eq(A_MILLION))).thenReturn(dto);

            // when
            List<CountryDetailDTO> result = electricityMetricsService.getAllElectricityData();

            // then
            assertNotNull(result);

            verify(electricityBreakdownRepository).findAll();
            verify(carbonIntensityRepository).findAll();
            verify(countryRepository).findAll();
        }
    }

    @Test
    void test_getAllElectricityData_countryNotFound() {
        // given
        ElectricityBreakdown breakdown = new ElectricityBreakdown();
        breakdown.setZone("XX");

        CarbonIntensity intensity = buildCarbonIntensity("XX");
        intensity.setCountryCode("XX");

        when(electricityBreakdownRepository.findAll()).thenReturn(List.of(breakdown));
        when(carbonIntensityRepository.findAll()).thenReturn(List.of(intensity));
        when(countryRepository.findAll()).thenReturn(List.of());

        // when, then
        assertThrows(DataNotFoundException.class, () -> electricityMetricsService.getAllElectricityData());
    }

    @Test
    void test_getAllElectricityData_carbonIntensityNotFound() {
        // given
        ElectricityBreakdown breakdown = new ElectricityBreakdown();
        breakdown.setZone("US");

        Country country = buildCountry("US");

        when(electricityBreakdownRepository.findAll()).thenReturn(List.of(breakdown));
        when(carbonIntensityRepository.findAll()).thenReturn(List.of());
        when(countryRepository.findAll()).thenReturn(List.of(country));

        // when, then
        assertThrows(DataNotFoundException.class, () -> electricityMetricsService.getAllElectricityData());
    }

    @Test
    void test_getElectricityDataByCountry_success() {
        // given
        String countryCode = "FR";
        ElectricityBreakdown breakdown = new ElectricityBreakdown();
        breakdown.setZone(countryCode);

        CarbonIntensity intensity = buildCarbonIntensity(countryCode);
        intensity.setCountryCode(countryCode);

        Country country = buildCountry(countryCode);
        CountryPanel countryPanel = buildCountryPanel(country, buildPanel());

        CountryDetailDTO dto = new CountryDetailDTO(
                "FR", "FR", 0.45, 0.85, 0.65, 28, 67000000L, Collections.emptyList());

        List<SolarPanelDTO> panelDTOs = List.of(buildSolarPanelDTO(countryCode));
        dto.setSolarPanels(panelDTOs);

        try (MockedStatic<CalculationUtils> calculationUtils = mockStatic(CalculationUtils.class)) {
            when(electricityBreakdownRepository.findByZone(countryCode)).thenReturn(Optional.of(breakdown));
            when(carbonIntensityRepository.findByCountryCode(countryCode)).thenReturn(Optional.of(intensity));
            when(countryRepository.findByCode(countryCode)).thenReturn(Optional.of(country));
            when(countryPanelRepository.findByCountryCode(countryCode)).thenReturn(List.of(countryPanel));

            calculationUtils.when(() -> CalculationUtils.formatPopulation(country.getPopulation())).thenReturn(A_MILLION);

            when(electricityMetricsMapper.toDTO(breakdown, intensity, A_MILLION)).thenReturn(dto);

            // when
            CountryDetailDTO result = electricityMetricsService.getElectricityDataByCountry(countryCode);

            // then
            assertNotNull(result);
            assertEquals(countryCode, result.getCountry());
            assertEquals(1, result.getSolarPanels().size());
            assertEquals("SolarMax Pro", result.getSolarPanels().get(0).getPanelName());

            verify(electricityBreakdownRepository).findByZone(countryCode);
            verify(carbonIntensityRepository).findByCountryCode(countryCode);
            verify(countryRepository).findByCode(countryCode);
            verify(countryPanelRepository).findByCountryCode(countryCode);
        }
    }

    @Test
    void test_breakdown_country_not_found() {
        // given
        String countryCode = "ID";
        when(electricityBreakdownRepository.findByZone(countryCode)).thenReturn(Optional.empty());

        // when and then
        assertThrows(DataNotFoundException.class, () -> electricityMetricsService.getElectricityDataByCountry(countryCode));
        verify(electricityBreakdownRepository).findByZone(countryCode);
    }

    @Test
    void test_breakdown_intensity_not_found() {
        // given
        String countryCode = "XX";

        ElectricityBreakdown breakdown = buildBreakdown();

        when(electricityBreakdownRepository.findByZone(countryCode)).thenReturn(Optional.of(breakdown));
        when(carbonIntensityRepository.findByCountryCode(countryCode)).thenReturn(Optional.empty());

        // when and then
        assertThrows(DataNotFoundException.class, () -> electricityMetricsService.getElectricityDataByCountry(countryCode));
        verify(electricityBreakdownRepository).findByZone(countryCode);
        verify(carbonIntensityRepository).findByCountryCode(countryCode);
    }

    @Test
    void test_electricity_data_country_notfound() {
        // given
        String countryCode = "XX";

        ElectricityBreakdown breakdown = new ElectricityBreakdown();
        breakdown.setZone(countryCode);

        CarbonIntensity intensity = buildCarbonIntensity(countryCode);
        intensity.setCountryCode(countryCode);

        when(electricityBreakdownRepository.findByZone(countryCode)).thenReturn(Optional.of(breakdown));
        when(carbonIntensityRepository.findByCountryCode(countryCode)).thenReturn(Optional.of(intensity));
        when(countryRepository.findByCode(countryCode)).thenReturn(Optional.empty());

        // when and then
        assertThrows(DataNotFoundException.class, () -> electricityMetricsService.getElectricityDataByCountry(countryCode));
        verify(electricityBreakdownRepository).findByZone(countryCode);
        verify(carbonIntensityRepository).findByCountryCode(countryCode);
        verify(countryRepository).findByCode(countryCode);
    }

    private Country buildCountry(String countryCode) {
        return Country.builder()
                .code(countryCode)
                .name("United Kingdom")
                .population(1000_000_000L)
                .build();
    }

    private ElectricityBreakdown buildBreakdown() {
        return ElectricityBreakdown.builder().zone("GB").build();
    }

    private CarbonIntensity buildCarbonIntensity(String countryCode) {
        return CarbonIntensity.builder()
                .countryCode(countryCode)
                .build();
    }

    private Panel buildPanel() {
        return Panel.builder()
                .name("SolarMax Pro")
                .installationCost(1200.0)
                .productionPerPanel(350.0)
                .description("High efficiency panel")
                .efficiency("22.5")
                .lifespan("25")
                .temperatureTolerance("-40")
                .warranty("10")
                .build();
    }

    private CountryPanel buildCountryPanel(Country country, Panel panel) {
        return CountryPanel.createCountryPanel(country, panel);
    }

    private SolarPanelDTO buildSolarPanelDTO(String countryCode) {
        return SolarPanelDTO.builder()
                .id(1L)
                .countryCode(countryCode)
                .panelName("SolarMax Pro")
                .installationCost(1200.0)
                .productionPerPanel(350.0)
                .description("High efficiency panel")
                .efficiency("22.5")
                .lifespan("25")
                .temperatureTolerance("-40")
                .warranty("10")
                .build();
    }
}