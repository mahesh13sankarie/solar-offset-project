package org.example.server.repository;

import org.example.server.dto.PanelResponseDTO;
import org.example.server.entity.Panel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PanelRepository extends JpaRepository<Panel, Long> {

    @Query("SELECT p FROM Panel p WHERE p.country.code = :code")
    List<PanelResponseDTO> findByCountryCode(String code);
}
