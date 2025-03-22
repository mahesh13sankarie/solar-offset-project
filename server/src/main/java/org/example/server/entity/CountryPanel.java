package org.example.server.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
}
