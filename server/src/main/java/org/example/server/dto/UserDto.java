package org.example.server.dto;

public record UserDto(
        Long id, String email, String password, String fullName, AccountType accountType
) {
}
