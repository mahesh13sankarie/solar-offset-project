package org.example.server.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column
    private int accountType;

    public User(Long id, String email, String password, String fullName, int accountType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.accountType = accountType;
    }

    public User() {
    }
}