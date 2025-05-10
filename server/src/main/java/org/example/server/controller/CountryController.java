package org.example.server.controller;

import org.example.server.dto.CountryDetailDTO;
import org.example.server.service.electricitymetrics.ElectricityMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<CountryDetailDTO>> getAllElectricityMetrics() {
        System.out.println("Fetching all electricity metrics");
        List<CountryDetailDTO> countries = electricityMetricsService.getAllElectricityData();
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{countryCode}")
    public ResponseEntity<CountryDetailDTO> getElectricityMetricsByCountry(
            @PathVariable String countryCode) {
        System.out.println("Fetching electricity metrics for country: " + countryCode);
        CountryDetailDTO country = electricityMetricsService.getElectricityDataByCountry(countryCode);
        return ResponseEntity.ok(country);
    }

}
