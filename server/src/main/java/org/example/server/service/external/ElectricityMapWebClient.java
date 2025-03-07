package org.example.server.service.external;

import org.example.server.dto.CarbonIntensityResponseDTO;
import org.example.server.dto.ElectricityMapCredentialDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ElectricityMapWebClient {

    private final WebClient webClient;

    public ElectricityMapWebClient(@Qualifier("electricityMapApiWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public CarbonIntensityResponseDTO fetchCarbonIntensity(ElectricityMapCredentialDTO credential) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/carbon-intensity/latest")
                        .queryParam("zone", credential.getCountryCode())
                        .build())
                .header("auth-token", credential.getToken())
                .retrieve()
                .bodyToMono(CarbonIntensityResponseDTO.class)
                .block();
    }
}