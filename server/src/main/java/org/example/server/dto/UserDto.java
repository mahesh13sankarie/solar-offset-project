package org.example.server.dto;

public record UserDto(
        String email, String password, String fullName, AccountType accountType
) {
}
