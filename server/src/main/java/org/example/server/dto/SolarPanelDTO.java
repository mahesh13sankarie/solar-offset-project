package org.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolarPanelDTO {
    private Long id;
    private String panelName;
    private Double installationCost;
    private Double productionPerPanel;
    private String description;
    private String countryCode;
    private String efficiency;
    private String lifespan;
    private String temperatureTolerance;
    private String warranty;
}
