package org.example.server.service.electricitymap;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.CarbonIntensityResponseDTO;
import org.example.server.entity.CarbonIntensity;
import org.example.server.repository.ElectricityMapRepository;
import org.example.server.service.external.ElectricityMapWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElectricityMapServiceImpl implements ElectricityMapService {


    private final ElectricityMapWebClient electricityMapWebClient;
    private final ElectricityMapRepository electricityMapRepository;

    // Tokens loaded from application.yml for API calls
    @Value("${electricityMap.tokens}")
    private String[] tokens;

    // Scheduler to execute every hour (fixedRate: 3600000 milliseconds)
    // Loop sends API requests 5 times if tokens array contains 5 tokens
    @Scheduled(fixedRate = 3600000)
    public void fetchAndSaveElectricityData() {
        for (String token : tokens) {
            fetchAndSaveElectricityDataAsync(token);
        }
    }

    // Asynchronous method to process each API call concurrently
    @Async
    public void fetchAndSaveElectricityDataAsync(String token) {
        CarbonIntensityResponseDTO response = electricityMapWebClient.fetchCarbonIntensity(token);
        CarbonIntensity entity = CarbonIntensity.builder()
                .carbonIntensity(response.carbonIntensity())
                .countryCode(response.countryCode())
                .datetime(response.datetime())
                .updatedAt(response.updatedAt())
                .build();
        electricityMapRepository.save(entity);
    }
}
