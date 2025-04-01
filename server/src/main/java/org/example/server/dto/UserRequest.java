package org.example.server.dto;

import java.util.Optional;

/**
 * @author: astidhiyaa
 * @date: 01/04/25
 * TODO: tidy up all request class! this parent class? or User DTO
 */
public record UserRequest(
        Long userId, Optional<Integer> accountType
) {
}
