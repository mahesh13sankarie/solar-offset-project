package org.example.server.mapper;

import org.example.server.dto.ElectricityResponseDto;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.utils.CalculationUtils;
import org.springframework.stereotype.Component;

@Component
public class ElectricityMetricsMapper {

    /**
     * Maps a single ElectricityBreakdown and average CarbonIntensity value to
     * ElectricityResponseDto
     */
    public ElectricityResponseDto toDTO(ElectricityBreakdown breakdown, CarbonIntensity carbonIntensity) {
        return new ElectricityResponseDto(
                breakdown.getZone(),
                CalculationUtils.calculateCarbonEmissions(breakdown, carbonIntensity),
                CalculationUtils.calculateElectricityAvailability(breakdown),
                CalculationUtils.calculateSolarPowerPotential(breakdown),
                breakdown.getRenewablePercentage()
        );
    }


}