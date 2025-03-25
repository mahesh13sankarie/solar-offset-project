package org.example.server.service.electricitymetrics;

import org.example.server.dto.CountryDetailDTO;
import org.example.server.dto.SolarPanelDTO;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.Country;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.entity.Panel;
import org.example.server.exception.DataNotFoundException;
import org.example.server.mapper.ElectricityMetricsMapper;
import org.example.server.repository.CarbonIntensityRepository;
import org.example.server.repository.CountryRepository;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ElectricityMetricsServiceImpl implements ElectricityMetricsService {

    private final CarbonIntensityRepository carbonIntensityRepository;
    private final ElectricityBreakdownRepository electricityBreakdownRepository;
    private final ElectricityMetricsMapper electricityMetricsMapper;
    private final CountryRepository countryRepository;

    public ElectricityMetricsServiceImpl(CarbonIntensityRepository carbonIntensityRepository,
                                         ElectricityBreakdownRepository electricityBreakdownRepository,
                                         ElectricityMetricsMapper electricityMetricsMapper,
                                         CountryRepository countryRepository) {
        this.carbonIntensityRepository = carbonIntensityRepository;
        this.electricityBreakdownRepository = electricityBreakdownRepository;
        this.electricityMetricsMapper = electricityMetricsMapper;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<CountryDetailDTO> getAllElectricityData() {
        List<ElectricityBreakdown> breakdowns = electricityBreakdownRepository.findAll();
        List<CarbonIntensity> carbonIntensities = carbonIntensityRepository.findAll();

        Map<String, CarbonIntensity> intensitiesByZone = carbonIntensities.stream()
                .collect(Collectors.toMap(CarbonIntensity::getCountryCode, carbonIntensity -> carbonIntensity));
        return breakdowns.stream().map(breakdown -> {
            CarbonIntensity intensitiesForZone = intensitiesByZone.get(breakdown.getZone());
            return electricityMetricsMapper.toDTO(breakdown, intensitiesForZone);
        }).collect(Collectors.toList());
    }

    @Override
    public CountryDetailDTO getElectricityDataByCountry(String countryCode) {
        Country country = countryRepository.findByCode(countryCode)
                .orElseThrow(() -> new DataNotFoundException("Country not found for code: " + countryCode));


        ElectricityBreakdown breakdownData = electricityBreakdownRepository.findByZone(countryCode).orElseThrow(
                () -> new DataNotFoundException("Electricity breakdown data not found for country: " + countryCode)
        );

        CarbonIntensity carbonIntensity = carbonIntensityRepository.findByCountryCode(countryCode).orElseThrow(
                () -> new DataNotFoundException("Carbon intensity data not found for country: " + countryCode)
        );
        CountryDetailDTO response = electricityMetricsMapper.toDTO(breakdownData, carbonIntensity);

        List<SolarPanelDTO> panelDTOs = country.getCountryPanels().stream()
                .map(cp -> {
                    Panel p = cp.getPanel();
                    return SolarPanelDTO.builder()
                            .panelName(p.getName())
                            .installationCost(p.getInstallationCost())
                            .productionPerPanel(p.getProductionPerPanel())
                            .description(p.getDescription())
                            .efficiency(p.getEfficiency())
                            .lifespan(p.getLifespan())
                            .temperatureTolerance(p.getTemperatureTolerance())
                            .warranty(p.getWarranty())
                            .build();
                }).toList();

        response.setSolarPanels(panelDTOs);
        System.out.println("Computed average metrics for country " + countryCode + "carbon intensity records: " + response);
        return response;
    }

}
