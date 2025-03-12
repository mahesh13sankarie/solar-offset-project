package org.example.server.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class UserDto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;
    @Getter @Setter private String firstName;
    @Getter @Setter private String lastName;
    @Getter @Setter private LocalDate loginDate;
    @Getter @Setter private String email;
    @Getter @Setter private String password;
    @Getter @Setter private int accountType;

    public UserDto(Long id, String firstName, String lastName, LocalDate loginDate, String email, String password, int accountType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginDate = loginDate;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public UserDto() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", loginDate=" + loginDate +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}
