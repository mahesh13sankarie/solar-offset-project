package org.example.server;

import org.example.server.entity.CarbonIntensity;
import org.example.server.repository.ElectricityMapRepository;
import org.example.server.service.electricitymap.ElectricityMapServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ElectricityMapServiceTest {

    @Autowired
    private ElectricityMapServiceImpl electricityMapService;

    @Autowired
    private ElectricityMapRepository electricityMapRepository;

    @Test
    void shouldFetchAndSaveElectricityData() {
        // Call scheduled method directly
        electricityMapService.saveCarbonIntensity();

        // Validate repository saved data (assert count or content)
        long count = electricityMapRepository.count();
        assertThat(count).isGreaterThan(0);  // 데이터가 저장되는지 확인

        // Fetch and print all saved data for verification
        List<CarbonIntensity> allData = electricityMapRepository.findAll();
        System.out.println("=== Fetched Carbon Intensity Data ===");
        for (CarbonIntensity data : allData) {
            System.out.printf("Country: %s, Carbon Intensity: %s, Datetime: %s, UpdatedAt: %s%n",
                    data.getCountryCode(),
                    data.getCarbonIntensity(),
                    data.getDatetime(),
                    data.getUpdatedAt());
        }
        System.out.println("====================================");
    }
}
