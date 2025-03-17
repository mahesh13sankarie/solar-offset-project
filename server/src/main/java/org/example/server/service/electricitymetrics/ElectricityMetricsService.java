package org.example.server.service.electricitymetrics;

import org.example.server.dto.ElectricityResponseDto;

import java.util.List;

public interface ElectricityMetricsService {
    List<ElectricityResponseDto> getAllElectricityData();
}
