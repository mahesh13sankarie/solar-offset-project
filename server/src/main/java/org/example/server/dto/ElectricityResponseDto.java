package org.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElectricityResponseDto {
    private String zone;
    private double carbonEmissions;
    private double electricityAvailability;
    private double solarPowerPotential;
    private int renewablePercentage;
}
