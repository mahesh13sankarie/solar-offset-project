package org.example.server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectricityBreakdown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID values using the database identity strategy
    private Long id;

    @Column(nullable = false)
    private String zone;

    private Double powerConsumptionBreakdownSolar;
    private Double powerProductionBreakdownSolar;
    private Integer fossilFreePercentage;
    private Integer renewablePercentage;
    private Integer powerConsumptionTotal;
    private Integer powerProductionTotal;
    private Integer powerImportTotal;
    private Integer powerExportTotal;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone", referencedColumnName = "code", insertable = false, updatable = false)
    private Country country;

}