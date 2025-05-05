package org.example.server.utils;

import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;

import java.util.List;

public class CalculationUtils {

    public static double calculateCarbonEmissions(ElectricityBreakdown breakdown, CarbonIntensity carbonIntensity) {
        if (breakdown.getPowerConsumptionTotal() == null || breakdown.getPowerConsumptionTotal() == 0)
            return 0.0;

        double emissions = (breakdown.getPowerConsumptionTotal() * 24 * carbonIntensity.getCarbonIntensity()) / 1000.0;
        return round(emissions); // tonnes
    }

    public static int calculateElectricityAvailability(ElectricityBreakdown breakdown) {
        double availabilityWh = (breakdown.getPowerProductionTotal()
                + breakdown.getPowerImportTotal()
                - breakdown.getPowerExportTotal()) * 24;

        return (int) Math.round(availabilityWh / 1000); // return in MWh
    }

    public static double calculateSolarPowerPotential(ElectricityBreakdown breakdown) {
        if (breakdown.getPowerProductionTotal() == null || breakdown.getPowerProductionTotal() == 0)
            return 0.0;

        double solarPercent = (breakdown.getPowerProductionBreakdownSolar() * 100.0) / breakdown.getPowerProductionTotal();
        return round(solarPercent);
    }

    public static long formatPopulation(long population) {
        return Math.round((double) population / 1_000_000.0);
    }

    public static double calculateAverage(List<? extends Number> values) {
        return values.stream()
                .filter(value -> value != null)
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(0.0);
    }

    public static double round(double value) {
        double scale = Math.pow(10, 2);
        return Math.round(value * scale) / scale;
    }
}
