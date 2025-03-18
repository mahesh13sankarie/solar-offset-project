package org.example.server.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ElectricityBreakdownResponseDTO(
        String zone,
        LocalDateTime updatedAt,
        Map<String, Double> powerConsumptionBreakdown,
        Map<String, Double> powerProductionBreakdown,
        int fossilFreePercentage,
        int renewablePercentage,
        int powerConsumptionTotal,
        int powerProductionTotal,
        int powerImportTotal,
        int powerExportTotal
) {
}