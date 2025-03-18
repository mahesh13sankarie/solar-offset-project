package org.example.server.service.electricitymap;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.CarbonIntensityResponseDTO;
import org.example.server.dto.ElectricityBreakdownResponseDTO;
import org.example.server.dto.ElectricityMapCredentialDTO;
import org.example.server.dto.ElectricityMapPropertiesDTO;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.repository.CarbonIntensityRepository;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.example.server.service.external.ElectricityMapWebClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ElectricityMapPropertiesDTO.class)
public class ElectricityMapServiceImpl implements ElectricityMapService {

    private final ElectricityMapWebClient electricityMapWebClient;
    private final CarbonIntensityRepository carbonIntensityRepository;
    private final ElectricityBreakdownRepository electricityBreakdownRepository;

    private final ElectricityMapPropertiesDTO properties;

    // Scheduler to execute every hour (fixedRate: 3600000 milliseconds)
    // Loop sends API requests 5 times if tokens array contains 5 tokens
    @Scheduled(fixedRate = 3600000)
    public void saveCarbonIntensity() {
        List<ElectricityMapCredentialDTO> credentials = properties.getCredentials();
        for (ElectricityMapCredentialDTO credential : credentials) {
            processCarbonIntensity(credential);
        }
    }

    // Asynchronous method to process each API call concurrently
    @Async
    public void processCarbonIntensity(ElectricityMapCredentialDTO credential) {
        CarbonIntensityResponseDTO response = electricityMapWebClient.fetchCarbonIntensity(credential);
        CarbonIntensity entity = CarbonIntensity.builder()
                .carbonIntensity(response.carbonIntensity())
                .countryCode(response.zone())
                .datetime(response.datetime())
                .updatedAt(response.updatedAt())
                .build();
        carbonIntensityRepository.save(entity);
    }

    @Scheduled(fixedRate = 3600000)
    public void saveElectricityBreakdownData() {
        List<ElectricityMapCredentialDTO> credentials = properties.getCredentials();
        for (ElectricityMapCredentialDTO credential : credentials) {
            processElectricityBreakdownData(credential);
        }
    }

    @Async
    public void processElectricityBreakdownData(ElectricityMapCredentialDTO credential) {
        ElectricityBreakdownResponseDTO response = electricityMapWebClient.fetchElectricityBreakdown(credential);
        System.out.println(response.powerConsumptionBreakdown());

        ElectricityBreakdown entity = ElectricityBreakdown.builder()
                .zone(response.zone())
                .updatedAt(response.updatedAt())
                .powerConsumptionBreakdownSolar(response.powerConsumptionBreakdown().get("solar"))
                .powerProductionBreakdownSolar(response.powerProductionBreakdown().get("solar"))
                .fossilFreePercentage(response.fossilFreePercentage())
                .renewablePercentage(response.renewablePercentage())
                .powerConsumptionTotal(response.powerConsumptionTotal())
                .powerProductionTotal(response.powerProductionTotal())
                .powerImportTotal(response.powerImportTotal())
                .powerExportTotal(response.powerExportTotal())
                .build();
        electricityBreakdownRepository.save(entity);
    }
}