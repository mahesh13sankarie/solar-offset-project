package org.example.server.controller;

import org.example.server.dto.CountryDetailDTO;
import org.example.server.service.electricitymetrics.ElectricityMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final ElectricityMetricsService electricityMetricsService;

    @Autowired
    public CountryController(ElectricityMetricsService electricityMetricsService) {
        this.electricityMetricsService = electricityMetricsService;
    }

    @GetMapping("")
    public List<CountryDetailDTO> getAllElectricityMetrics() {
        System.out.println("Fetching all electricity metrics");
        return electricityMetricsService.getAllElectricityData();
    }

    @GetMapping("/{countryCode}")
    public CountryDetailDTO getElectricityMetricsByCountry(
            @PathVariable String countryCode) {
        System.out.println("Fetching electricity metrics for country: " + countryCode);
        return electricityMetricsService.getElectricityDataByCountry(countryCode);
    }

}
