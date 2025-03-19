package org.example.server.service.electricitymetrics;

import org.example.server.dto.ElectricityResponseDto;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.exception.DataNotFoundException;
import org.example.server.mapper.ElectricityMetricsMapper;
import org.example.server.repository.CarbonIntensityRepository;
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

    public ElectricityMetricsServiceImpl(CarbonIntensityRepository carbonIntensityRepository, ElectricityBreakdownRepository electricityBreakdownRepository, ElectricityMetricsMapper electricityMetricsMapper) {
        this.carbonIntensityRepository = carbonIntensityRepository;
        this.electricityBreakdownRepository = electricityBreakdownRepository;
        this.electricityMetricsMapper = electricityMetricsMapper;
    }

    @Override
    public List<ElectricityResponseDto> getAllElectricityData() {
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
    public ElectricityResponseDto getElectricityDataByCountry(String countryCode) {

        ElectricityBreakdown breakdownData = electricityBreakdownRepository.findByZone(countryCode).orElseThrow(
                () -> new DataNotFoundException("Electricity breakdown data not found for country: " + countryCode)
        );

        CarbonIntensity carbonIntensity = carbonIntensityRepository.findByCountryCode(countryCode).orElseThrow(
                () -> new DataNotFoundException("Carbon intensity data not found for country: " + countryCode)
        );

        ElectricityResponseDto response = electricityMetricsMapper.toDTO(breakdownData, carbonIntensity);
        System.out.println("Computed average metrics for country " + countryCode + "carbon intensity records: " + response);
        return response;
    }

}
