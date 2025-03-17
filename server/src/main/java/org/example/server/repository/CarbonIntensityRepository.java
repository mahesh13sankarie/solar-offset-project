package org.example.server.repository;

import org.example.server.entity.CarbonIntensity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CarbonIntensityRepository extends JpaRepository<CarbonIntensity, Long> {
    Optional<CarbonIntensity> findByCountryCode(String countryCode);
}
