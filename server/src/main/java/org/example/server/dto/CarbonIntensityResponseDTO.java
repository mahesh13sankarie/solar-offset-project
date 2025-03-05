package org.example.server.dto;

import java.time.LocalDateTime;

// Immutable DTO using Java 17 Record syntax
public record CarbonIntensityResponseDTO(
        String countryCode,       // Country identifier (e.g., "KR", "US")
        int carbonIntensity,      // Carbon emitted per unit of electricity
        LocalDateTime datetime,   // Timestamp of data collection
        LocalDateTime updatedAt   // Timestamp when data was fetched
) {}
