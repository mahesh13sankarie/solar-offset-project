package org.example.server.controller;

import org.example.server.dto.ElectricityResponseDto;
import org.example.server.service.electricitymetrics.ElectricityMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/electricity")
public class ElectricityController {

    private final ElectricityMetricsService electricityMetricsService;

    @Autowired
    public ElectricityController(ElectricityMetricsService electricityMetricsService) {
        this.electricityMetricsService = electricityMetricsService;
    }

    // @return List of electricity metrics for all available zones
    @GetMapping("/metrics")
    public List<ElectricityResponseDto> getAllElectricityMetrics() {
        System.out.println("Fetching all electricity metrics");
        return electricityMetricsService.getAllElectricityData();
    }
}
