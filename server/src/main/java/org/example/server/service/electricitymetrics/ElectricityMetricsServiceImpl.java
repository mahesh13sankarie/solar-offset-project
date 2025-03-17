package org.example.server.service.electricitymetrics;

import org.example.server.dto.ElectricityResponseDto;
import org.example.server.entity.CarbonIntensity;
import org.example.server.entity.ElectricityBreakdown;
import org.example.server.exception.DataNotFoundException;
import org.example.server.mapper.ElectricityMetricsMapper;
import org.example.server.repository.CarbonIntensityRepository;
import org.example.server.repository.ElectricityBreakdownRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectricityMetricsServiceImpl implements ElectricityMetricsService {

    private final CarbonIntensityRepository carbonIntensityRepository;
    private final ElectricityBreakdownRepository electricityBreakdownRepository;
    private final ElectricityMetricsMapper electricityMetricsMapper;

    public ElectricityMetricsServiceImpl(CarbonIntensityRepository carbonIntensityRepository, ElectricityBreakdownRepository electricityBreakdownRepository, ElectricityMetricsMapper electricityMetricsMapper) {
        this.carbonIntensityRepository = carbonIntensityRepository;
        this.electricityBreakdownRepository = electricityBreakdownRepository;
        this.electricityMetricsMapper = electricityMetricsMapper;
    }

    @Override
    public List<ElectricityResponseDto> getAllElectricityData() {
        List<ElectricityBreakdown> breakdowns = electricityBreakdownRepository.findAll();

        return breakdowns.stream().map(breakdown -> {
            List<CarbonIntensity> carbonIntensities = carbonIntensityRepository.findByCountryCodeOrderByUpdatedAtDesc(breakdown.getZone());

            if (carbonIntensities.isEmpty()) {
                throw new DataNotFoundException("Carbon intensity data not found for zone: " + breakdown.getZone());
            }

            int avgCarbonIntensity = electricityMetricsMapper.calculateAverageCarbonIntensity(carbonIntensities);
            ElectricityResponseDto response = electricityMetricsMapper.toDtoWithAvgIntensity(breakdown, avgCarbonIntensity);

            System.out.println("Computed metrics for zone " + breakdown.getZone() + " from " + carbonIntensities.size() + " carbon intensity records: " + response);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public ElectricityResponseDto getElectricityDataByCountry(String countryCode) {
        // Get the start of today
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        // Fetch all data for today for the specific country
        List<ElectricityBreakdown> todayData = electricityBreakdownRepository.findTodayDataByZone(countryCode, startOfDay);

        if (todayData.isEmpty()) {
            throw new DataNotFoundException("No electricity breakdown data found for today for country: " + countryCode);
        }

        // Create an average ElectricityBreakdown object
        ElectricityBreakdown averageBreakdown = electricityMetricsMapper.createAverageBreakdown(todayData, countryCode);

        // Get carbon intensity data for this country
        List<CarbonIntensity> carbonIntensities = carbonIntensityRepository.findByCountryCodeOrderByUpdatedAtDesc(countryCode);

        if (carbonIntensities.isEmpty()) {
            throw new DataNotFoundException("Carbon intensity data not found for country: " + countryCode);
        }

        // Calculate average carbon intensity
        int avgCarbonIntensity = electricityMetricsMapper.calculateAverageCarbonIntensity(carbonIntensities);

        // Create the response with calculated metrics
        ElectricityResponseDto response = electricityMetricsMapper.toDtoWithAvgIntensity(averageBreakdown, avgCarbonIntensity);

        System.out.println("Computed average metrics for country " + countryCode + " from " + todayData.size() + " records and " + carbonIntensities.size() + " carbon intensity records: " + response);
        return response;
    }

}
