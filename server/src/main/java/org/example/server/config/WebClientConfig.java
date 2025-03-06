package org.example.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Latest Carbon Intensity
    // https://portal.electricitymaps.com/docs/api#latest-carbon-intensity
    @Bean(name = "electricityMapApiWebClient")
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.electricitymap.org")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
