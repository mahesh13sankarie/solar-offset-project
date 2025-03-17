package org.example.server.service.electricitymetrics;

import org.example.server.dto.ElectricityResponseDto;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.exception.DataNotFoundException;
import org.example.server.repository.CarbonIntensityRepository;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.example.server.utils.CalculationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectricityMetricsServiceImpl implements ElectricityMetricsService {

    private final CarbonIntensityRepository carbonIntensityRepository;
    private final ElectricityBreakdownRepository electricityBreakdownRepository;

    public ElectricityMetricsServiceImpl(CarbonIntensityRepository carbonIntensityRepository,
            ElectricityBreakdownRepository electricityBreakdownRepository) {
        this.carbonIntensityRepository = carbonIntensityRepository;
        this.electricityBreakdownRepository = electricityBreakdownRepository;
    }

    @Override
    public List<ElectricityResponseDto> getAllElectricityData() {
        List<ElectricityBreakdown> breakdowns = electricityBreakdownRepository.findAll();

        return breakdowns.stream().map(breakdown -> {
            CarbonIntensity carbonIntensity = carbonIntensityRepository.findByCountryCode(breakdown.getZone())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Carbon intensity data not found for zone: " + breakdown.getZone())); // todo

            ElectricityResponseDto response = new ElectricityResponseDto(
                    breakdown.getZone(),
                    CalculationUtils.calculateCarbonEmissions(breakdown, carbonIntensity),
                    CalculationUtils.calculateElectricityAvailability(breakdown),
                    CalculationUtils.calculateSolarPowerPotential(breakdown),
                    breakdown.getRenewablePercentage());
            System.out.println("Computed metrics: " + response);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public ElectricityResponseDto getElectricityDataByCountry(String countryCode) {
        ElectricityBreakdown breakdown = electricityBreakdownRepository.findByZone(countryCode)
                .orElseThrow(() -> new DataNotFoundException(
                        "Electricity breakdown data not found for country: " + countryCode));

        CarbonIntensity carbonIntensity = carbonIntensityRepository.findByCountryCode(countryCode)
                .orElseThrow(
                        () -> new DataNotFoundException("Carbon intensity data not found for country: " + countryCode));

        ElectricityResponseDto response = new ElectricityResponseDto(
                breakdown.getZone(),
                CalculationUtils.calculateCarbonEmissions(breakdown, carbonIntensity),
                CalculationUtils.calculateElectricityAvailability(breakdown),
                CalculationUtils.calculateSolarPowerPotential(breakdown),
                breakdown.getRenewablePercentage());

        System.out.println("Computed metrics for country " + countryCode + ": " + response);
        return response;
    }
}
