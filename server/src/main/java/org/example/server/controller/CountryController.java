package org.example.server.controller;

import java.util.List;

import org.example.server.dto.ElectricityResponseDto;
import org.example.server.service.electricitymetrics.ElectricityMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final ElectricityMetricsService electricityMetricsService;

    @Autowired
    public CountryController(ElectricityMetricsService electricityMetricsService) {
        this.electricityMetricsService = electricityMetricsService;
    }

    @GetMapping("")
    public List<ElectricityResponseDto> getAllElectricityMetrics() {
        System.out.println("Fetching all electricity metrics");
        return electricityMetricsService.getAllElectricityData();
    }

    @GetMapping("/{countryCode}")
    public ElectricityResponseDto getElectricityMetricsByCountry(
            @PathVariable String countryCode) {
        System.out.println("Fetching electricity metrics for country: " + countryCode);
        return electricityMetricsService.getElectricityDataByCountry(countryCode);
    }

}
