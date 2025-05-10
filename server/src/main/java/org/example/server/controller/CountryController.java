package org.example.server.controller;

import org.example.server.dto.CountryDetailDTO;
import org.example.server.service.electricitymetrics.ElectricityMetricsService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.ApiResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ApiResponse<ApiResponse.CustomBody<List<CountryDetailDTO>>> getAllElectricityMetrics() {
        System.out.println("Fetching all electricity metrics");
        List<CountryDetailDTO> countries = electricityMetricsService.getAllElectricityData();
        return ApiResponseGenerator.success(HttpStatus.OK, countries);
    }

    @GetMapping("/{countryCode}")
    public ApiResponse<ApiResponse.CustomBody<CountryDetailDTO>> getElectricityMetricsByCountry(
            @PathVariable String countryCode) {
        System.out.println("Fetching electricity metrics for country: " + countryCode);
        CountryDetailDTO country = electricityMetricsService.getElectricityDataByCountry(countryCode);
        return ApiResponseGenerator.success(HttpStatus.OK, country);
    }

}
