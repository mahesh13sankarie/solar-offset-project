package org.example.server.repository;

import org.example.server.entity.CarbonIntensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarbonIntensityRepository extends JpaRepository<CarbonIntensity, Long> {
    Optional<CarbonIntensity> findByCountryCode(String countryCode);

    @Query("SELECT c FROM CarbonIntensity c WHERE c.countryCode = :countryCode ORDER BY c.updatedAt DESC")
    List<CarbonIntensity> findByCountryCodeOrderByUpdatedAtDesc(@Param("countryCode") String countryCode);
}
