package org.example.server.service.electricitymetrics;

import org.example.server.dto.CountryDetailDTO;

import java.util.List;

public interface ElectricityMetricsService {
    List<CountryDetailDTO> getAllElectricityData();

    CountryDetailDTO getElectricityDataByCountry(String countryCode);
}
