package org.example.server.services;

import org.example.server.entity.ElectricityBreakdown;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.example.server.service.electricitymap.ElectricityMapServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ElectricityBreakdownServiceTest {

    @Autowired
    private ElectricityMapServiceImpl electricityMapService;

    @Autowired
    private ElectricityBreakdownRepository electricityBreakdownRepository;

    @Test
    void shouldFetchAndSaveElectricityBreakdownData() {

        // Call scheduled method directly
        electricityMapService.saveElectricityBreakdownData();

        // Validate repository saved data (assert count or content)
        long count = electricityBreakdownRepository.count();
        assertThat(count).isGreaterThan(0);  // 데이터가 저장되는지 확인

        // Fetch and print all saved data for verification
        List<ElectricityBreakdown> allData = electricityBreakdownRepository.findAll();
        System.out.println("=== Fetched Electricity Breakdown Data ===");
        for (ElectricityBreakdown data : allData) {
            System.out.printf("Zone: %s, Updated At: %s, Solar Consumption: %.2f, Solar Production: %.2f, " +
                            "Fossil-Free%%: %d, Renewable%%: %d, Total Consumption: %d, Total Production: %d, " +
                            "Import Total: %d, Export Total: %d%n",
                    data.getZone(),
                    data.getUpdatedAt(),
                    data.getPowerConsumptionBreakdownSolar(),
                    data.getPowerProductionBreakdownSolar(),
                    data.getFossilFreePercentage(),
                    data.getRenewablePercentage(),
                    data.getPowerConsumptionTotal(),
                    data.getPowerProductionTotal(),
                    data.getPowerImportTotal(),
                    data.getPowerExportTotal());
        }
        System.out.println("==========================================");
    }
}