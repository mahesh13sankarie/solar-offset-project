package org.example.server.repository;

import org.example.server.entity.CarbonIntensity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ElectricityMapRepository extends JpaRepository<CarbonIntensity,Long> {

}
