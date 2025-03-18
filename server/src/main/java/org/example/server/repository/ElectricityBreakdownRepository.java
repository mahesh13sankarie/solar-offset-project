package org.example.server.repository;

import org.example.server.entity.ElectricityBreakdown;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectricityBreakdownRepository extends JpaRepository<ElectricityBreakdown, Long> {
}