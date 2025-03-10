package org.example.server.service.auth;

import org.example.server.dto.UserDto;

public interface AuthService {
    UserDto saveUser(UserDto userDto);
}
