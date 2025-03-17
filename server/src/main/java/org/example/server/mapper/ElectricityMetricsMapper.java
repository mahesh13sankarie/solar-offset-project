package org.example.server.mapper;

import org.example.server.dto.ElectricityResponseDto;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.utils.CalculationUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElectricityMetricsMapper {

    /**
     * Maps a single ElectricityBreakdown and CarbonIntensity to
     * ElectricityResponseDto
     */
    public ElectricityResponseDto toDto(ElectricityBreakdown breakdown, CarbonIntensity carbonIntensity) {
        return new ElectricityResponseDto(
                breakdown.getZone(),
                CalculationUtils.calculateCarbonEmissions(breakdown, carbonIntensity),
                CalculationUtils.calculateElectricityAvailability(breakdown),
                CalculationUtils.calculateSolarPowerPotential(breakdown),
                breakdown.getRenewablePercentage());
    }

    /**
     * Maps a single ElectricityBreakdown and average CarbonIntensity value to
     * ElectricityResponseDto
     */
    public ElectricityResponseDto toDtoWithAvgIntensity(ElectricityBreakdown breakdown, int avgCarbonIntensity) {
        CarbonIntensity avgCarbonIntensityObj = CarbonIntensity.builder()
                .countryCode(breakdown.getZone())
                .carbonIntensity(avgCarbonIntensity)
                .updatedAt(LocalDateTime.now())
                .build();

        return toDto(breakdown, avgCarbonIntensityObj);
    }

    /**
     * Calculates average carbon intensity from a list of CarbonIntensity entities
     */
    public int calculateAverageCarbonIntensity(List<CarbonIntensity> intensities) {
        if (intensities == null || intensities.isEmpty()) {
            return 0;
        }

        return (int) CalculationUtils.calculateAverage(
                intensities.stream()
                        .map(CarbonIntensity::getCarbonIntensity)
                        .map(Integer::valueOf)
                        .collect(Collectors.toList()));
    }

    /**
     * Creates an average ElectricityBreakdown from a list of breakdowns for a
     * specific country
     */
    public ElectricityBreakdown createAverageBreakdown(List<ElectricityBreakdown> breakdowns, String countryCode) {
        if (breakdowns == null || breakdowns.isEmpty()) {
            return null;
        }

        double avgPowerConsumptionBreakdownSolar = CalculationUtils.calculateAverage(
                breakdowns.stream()
                        .map(ElectricityBreakdown::getPowerConsumptionBreakdownSolar)
                        .collect(Collectors.toList()));

        double avgPowerProductionBreakdownSolar = CalculationUtils.calculateAverage(
                breakdowns.stream()
                        .map(ElectricityBreakdown::getPowerProductionBreakdownSolar)
                        .collect(Collectors.toList()));

        int avgFossilFreePercentage = (int) CalculationUtils.calculateAverage(
                breakdowns.stream()
                        .map(ElectricityBreakdown::getFossilFreePercentage)
                        .collect(Collectors.toList()));

        int avgRenewablePercentage = (int) CalculationUtils.calculateAverage(
                breakdowns.stream()
                        .map(ElectricityBreakdown::getRenewablePercentage)
                        .collect(Collectors.toList()));

        int avgPowerConsumptionTotal = (int) CalculationUtils.calculateAverage(
                breakdowns.stream()
                        .map(ElectricityBreakdown::getPowerConsumptionTotal)
                        .collect(Collectors.toList()));

        int avgPowerProductionTotal = (int) CalculationUtils.calculateAverage(
                breakdowns.stream()
                        .map(ElectricityBreakdown::getPowerProductionTotal)
                        .collect(Collectors.toList()));

        int avgPowerImportTotal = (int) CalculationUtils.calculateAverage(
                breakdowns.stream()
                        .map(ElectricityBreakdown::getPowerImportTotal)
                        .collect(Collectors.toList()));

        int avgPowerExportTotal = (int) CalculationUtils.calculateAverage(
                breakdowns.stream()
                        .map(ElectricityBreakdown::getPowerExportTotal)
                        .collect(Collectors.toList()));

        return ElectricityBreakdown.builder()
                .zone(countryCode)
                .powerConsumptionBreakdownSolar(avgPowerConsumptionBreakdownSolar)
                .powerProductionBreakdownSolar(avgPowerProductionBreakdownSolar)
                .fossilFreePercentage(avgFossilFreePercentage)
                .renewablePercentage(avgRenewablePercentage)
                .powerConsumptionTotal(avgPowerConsumptionTotal)
                .powerProductionTotal(avgPowerProductionTotal)
                .powerImportTotal(avgPowerImportTotal)
                .powerExportTotal(avgPowerExportTotal)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}