package org.example.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) //// Generates a protected no-argument constructor
public class CarbonIntensity {

    @Id // Specifies the primary key of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID values using the database identity strategy
    private Long id;

    private String countryCode;

    private int carbonIntensity;
    private LocalDateTime updatedAt;
    private LocalDateTime datetime; // API Response time
}
