package org.example.server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Panel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "installation_cost", nullable = false)
    private Double installationCost;

    @Column(name = "production_per_panel", nullable = false)
    private Double productionPerPanel;

    @Column(name = "description")
    private String description;

    private String efficiency;
    private String lifespan;
    private String temperatureTolerance;
    private String warranty;

    @OneToMany(mappedBy = "panel", cascade = CascadeType.ALL)
    private List<CountryPanel> countryPanels;
}
