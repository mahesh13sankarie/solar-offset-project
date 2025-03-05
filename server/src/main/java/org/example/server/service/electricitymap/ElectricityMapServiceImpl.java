package org.example.server.service.electricitymap;

import lombok.RequiredArgsConstructor;
import org.example.server.dto.CarbonIntensityResponseDTO;
import org.example.server.entity.CarbonIntensity;
import org.example.server.repository.ElectricityMapRepository;
import org.example.server.service.external.ElectricityMapWebClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElectricityMapServiceImpl implements ElectricityMapService {


    private final ElectricityMapWebClient electricityMapWebClient;
    private final ElectricityMapRepository electricityMapRepository;

    @Scheduled(fixedRate = 3600000)  // 1시간마다 실행
    public void fetchAndSaveElectricityData() {
        String token = ""; // Todo: add secret api key
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
