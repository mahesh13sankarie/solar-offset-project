package org.example.server.service.external;

import org.example.server.dto.CarbonIntensityResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ElectricityMapWebClient {

    private final WebClient webClient;

    public ElectricityMapWebClient(@Qualifier("electricityMapApiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public CarbonIntensityResponseDTO fetchCarbonIntensity(String token) {
        return webClient.get()
                .uri("/v3/carbon-intensity/latest")
                .header("auth-token", token)
                .retrieve()
                .bodyToMono(CarbonIntensityResponseDTO.class)
                .block();
    }
}
