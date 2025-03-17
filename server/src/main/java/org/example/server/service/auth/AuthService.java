package org.example.server.service.auth;

import org.example.server.dto.UserDto;
import org.example.server.entity.User;

public interface AuthService {
    User saveUser(UserDto user);
}
