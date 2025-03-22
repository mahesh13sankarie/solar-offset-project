package org.example.server.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "country_panel")
public class CountryPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "panel_id")
    private Panel panel;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Factory method to create a CountryPanel instance
    public static CountryPanel createCountryPanel(Country country, Panel panel) {
        CountryPanel countryPanel = new CountryPanel();
        countryPanel.country = country;
        countryPanel.panel = panel;

        // Set the createdAt and updatedAt fields to the current time
        LocalDateTime createdAt = LocalDateTime.now();
        countryPanel.createdAt = createdAt;
        countryPanel.updatedAt = createdAt;
        return countryPanel;
    }
}
