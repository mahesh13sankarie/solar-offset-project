package org.example.server.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * @author: astidhiyaa
 * @date: 24/03/25
 */
@Entity
@Table(name = "panel_transaction")
@Getter
public class PanelTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "panel_id")
    private CountryPanel panel;

    public PanelTransaction(
            User user, CountryPanel panel
    ) {
        this.user = user;
        this.panel = panel;
    }

    public PanelTransaction() {

    }
}
