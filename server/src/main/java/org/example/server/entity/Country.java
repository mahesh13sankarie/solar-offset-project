package org.example.server.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Generates a protected no-argument constructor
public class Country {


    @Id // Specifies the primary key of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID values using the database identity strategy
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String code;
    private Long population;

    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElectricityBreakdown> breakdowns;
}
