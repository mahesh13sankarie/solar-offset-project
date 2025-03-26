package org.example.server.mapper;

import org.example.server.dto.CountryDetailDTO;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.utils.CalculationUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ElectricityMetricsMapper {

    /**
     * Maps a single ElectricityBreakdown and average CarbonIntensity value to
     * ElectricityResponseDto
     */
    public CountryDetailDTO toDTO(ElectricityBreakdown breakdown, CarbonIntensity carbonIntensity, long population) {
        String countryName = breakdown.getCountry() != null ? breakdown.getCountry().getName() : breakdown.getZone();

        return new CountryDetailDTO(
                breakdown.getZone(),
                countryName,
                CalculationUtils.calculateCarbonEmissions(breakdown, carbonIntensity),
                CalculationUtils.calculateElectricityAvailability(breakdown),
                CalculationUtils.calculateSolarPowerPotential(breakdown),
                breakdown.getRenewablePercentage(),
                population,
                Collections.emptyList()
        );
    }


}