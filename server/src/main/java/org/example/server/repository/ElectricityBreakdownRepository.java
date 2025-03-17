package org.example.server.repository;

import org.example.server.entity.ElectricityBreakdown;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectricityBreakdownRepository extends JpaRepository<ElectricityBreakdown, Long> {
	Optional<ElectricityBreakdown> findByZone(String zone);
}