package org.example.server.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.example.server.entity.ElectricityBreakdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ElectricityBreakdownRepository extends JpaRepository<ElectricityBreakdown, Long> {
	@Query("SELECT e FROM ElectricityBreakdown e WHERE e.zone = :zone AND e.updatedAt >= :startDateTime")
	List<ElectricityBreakdown> findTodayDataByZone(@Param("zone") String zone,
			@Param("startDateTime") LocalDateTime startDateTime);
}