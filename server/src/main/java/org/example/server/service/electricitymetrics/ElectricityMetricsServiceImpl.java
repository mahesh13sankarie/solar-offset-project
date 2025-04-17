package org.example.server.service.electricitymetrics;

import org.example.server.dto.CountryDetailDTO;
import org.example.server.dto.CountryPanelDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.entity.*;
import org.example.server.exception.DataNotFoundException;
import org.example.server.mapper.ElectricityMetricsMapper;
import org.example.server.repository.CarbonIntensityRepository;
import org.example.server.repository.CountryPanelRepository;
import org.example.server.repository.CountryRepository;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.example.server.utils.CalculationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ElectricityMetricsServiceImpl implements ElectricityMetricsService {

    private final CarbonIntensityRepository carbonIntensityRepository;
    private final ElectricityBreakdownRepository electricityBreakdownRepository;
    private final ElectricityMetricsMapper electricityMetricsMapper;
    private final CountryRepository countryRepository;
    private final CountryPanelRepository countryPanelRepository;

    public ElectricityMetricsServiceImpl(CarbonIntensityRepository carbonIntensityRepository,
                                         ElectricityBreakdownRepository electricityBreakdownRepository,
                                         ElectricityMetricsMapper electricityMetricsMapper,
                                         CountryRepository countryRepository, CountryPanelRepository countryPanelRepository) {
        this.carbonIntensityRepository = carbonIntensityRepository;
        this.electricityBreakdownRepository = electricityBreakdownRepository;
        this.electricityMetricsMapper = electricityMetricsMapper;
        this.countryRepository = countryRepository;
        this.countryPanelRepository = countryPanelRepository;
    }

    @Override
    public List<CountryDetailDTO> getAllElectricityData() {
        List<ElectricityBreakdown> breakdowns = electricityBreakdownRepository.findAll();
        List<CarbonIntensity> carbonIntensities = carbonIntensityRepository.findAll();

        Map<String, CarbonIntensity> intensitiesByZone = carbonIntensities.stream()
                .collect(Collectors.toMap(CarbonIntensity::getCountryCode,
                        carbonIntensity -> carbonIntensity));

        Map<String, Country> countryMap = countryRepository.findAll().stream()
                .collect(Collectors.toMap(Country::getCode,
                        country -> country));

        return breakdowns.stream()
                .map(breakdown -> {
                    String zone = breakdown.getZone();

                    Country country = Optional.ofNullable(countryMap.get(zone))
                            .orElseThrow(() -> new DataNotFoundException("Country not found for code: " + zone));

                    CarbonIntensity intensity = Optional.ofNullable(intensitiesByZone.get(zone))
                            .orElseThrow(() -> new DataNotFoundException("Carbon intensity not found for country: " + zone));

                    return electricityMetricsMapper.toDTO(breakdown, intensity, CalculationUtils.formatPopulation(country.getPopulation()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public CountryDetailDTO getElectricityDataByCountry(String countryCode) {

        ElectricityBreakdown breakdown = electricityBreakdownRepository.findByZone(countryCode)
                .orElseThrow(() -> new DataNotFoundException("Electricity breakdown not found for: " + countryCode));

        CarbonIntensity intensity = carbonIntensityRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new DataNotFoundException("Carbon intensity not found for: " + countryCode));

        Country country = countryRepository.findByCode(countryCode)
                .orElseThrow(() -> new DataNotFoundException("Country not found for code: " + countryCode));

        List<SolarPanelDTO> countryPanels = countryPanelRepository.findByCountryCode(countryCode)
                .stream().map(this::mapPanels).collect(Collectors.toList());

        CountryDetailDTO dto = electricityMetricsMapper.toDTO(breakdown, intensity, CalculationUtils.formatPopulation(country.getPopulation()));
        dto.setSolarPanels(countryPanels);

        System.out.println("Computed average metrics for country " + countryCode + "carbon intensity records: " + dto);
        return dto;
    }

    private SolarPanelDTO mapPanels(CountryPanel cp) {
        Panel p = cp.getPanel();
        Country c = cp.getCountry();
        return SolarPanelDTO.builder()
                .id(cp.getId())
                .countryCode(c.getCode())
                .panelName(p.getName())
                .installationCost(p.getInstallationCost())
                .productionPerPanel(p.getProductionPerPanel())
                .description(p.getDescription())
                .efficiency(p.getEfficiency())
                .lifespan(p.getLifespan())
                .temperatureTolerance(p.getTemperatureTolerance())
                .warranty(p.getWarranty())
                .build();
    }
}
