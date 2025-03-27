package org.example.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryDetailDTO {
    private String zone;
    private String country;
    private double carbonEmissions;
    private double electricityAvailability;
    private double solarPowerPotential;
    private int renewablePercentage;
    private Long population;
    private List<SolarPanelDTO> solarPanels;
}
