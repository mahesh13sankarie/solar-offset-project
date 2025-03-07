package org.example.server.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "electricity-map")
public class ElectricityMapPropertiesDTO {
    private List<ElectricityMapCredentialDTO> credentials;
}