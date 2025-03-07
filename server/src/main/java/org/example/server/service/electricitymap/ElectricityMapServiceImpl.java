package org.example.server.service.electricitymap;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.CarbonIntensityResponseDTO;
import org.example.server.dto.ElectricityMapCredentialDTO;
import org.example.server.dto.ElectricityMapPropertiesDTO;
import org.example.server.entity.CarbonIntensity;
import org.example.server.repository.ElectricityMapRepository;
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
    private final ElectricityMapRepository electricityMapRepository;

    // Tokens loaded from application.yml for API calls
    private final ElectricityMapPropertiesDTO properties;

    // Scheduler to execute every hour (fixedRate: 3600000 milliseconds)
    // Loop sends API requests 5 times if tokens array contains 5 tokens
    @Scheduled(fixedRate = 3600000)
    public void fetchAndSaveElectricityData() {
        List<ElectricityMapCredentialDTO> credentials = properties.getCredentials();
        for (ElectricityMapCredentialDTO credential : credentials) {
            fetchAndSaveElectricityDataAsync(credential);
        }
    }

    // Asynchronous method to process each API call concurrently
    @Async
    public void fetchAndSaveElectricityDataAsync(ElectricityMapCredentialDTO credential) {
        CarbonIntensityResponseDTO response = electricityMapWebClient.fetchCarbonIntensity(credential);
        CarbonIntensity entity = CarbonIntensity.builder()
                .carbonIntensity(response.carbonIntensity())
                .countryCode(response.zone())
                .datetime(response.datetime())
                .updatedAt(response.updatedAt())
                .build();
        electricityMapRepository.save(entity);
    }
}
