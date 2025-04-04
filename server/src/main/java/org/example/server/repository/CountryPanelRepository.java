package org.example.server.repository;

import org.example.server.entity.CountryPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryPanelRepository extends JpaRepository<CountryPanel, Long> {

    @Query("SELECT cp FROM CountryPanel cp JOIN cp.country c WHERE c.code = :code")
    List<CountryPanel> findByCountryCode(@Param("code") String code);
}