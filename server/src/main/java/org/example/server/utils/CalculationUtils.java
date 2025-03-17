package org.example.server.utils;

import java.util.List;

import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;

public class CalculationUtils {

    public static double calculateCarbonEmissions(ElectricityBreakdown breakdown, CarbonIntensity carbonIntensity) {
        if (breakdown.getPowerConsumptionTotal() == null || breakdown.getPowerConsumptionTotal() == 0)
            return 0;
        return (breakdown.getPowerConsumptionTotal() * 1000 * 24 * carbonIntensity.getCarbonIntensity()) / 1000000.0;
    }

    public static double calculateElectricityAvailability(ElectricityBreakdown breakdown) {
        return (breakdown.getPowerProductionTotal() + breakdown.getPowerImportTotal() - breakdown.getPowerExportTotal())
                * 1000 * 24;
    }

    public static double calculateSolarPowerPotential(ElectricityBreakdown breakdown) {
        if (breakdown.getPowerProductionTotal() == null || breakdown.getPowerProductionTotal() == 0)
            return 0;
        return (breakdown.getPowerProductionBreakdownSolar() * 100) / breakdown.getPowerProductionTotal();
    }

    public static double calculateAverage(List<? extends Number> values) {
        return values.stream()
                .filter(value -> value != null)
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0.0);
    }
}
