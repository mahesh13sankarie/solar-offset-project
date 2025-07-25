package org.example.server.service.auth;

import org.example.server.dto.LoginDto;
import org.example.server.dto.UserDto;
import org.example.server.dto.UserRequest;
import org.example.server.entity.User;

import java.util.List;

public interface AuthService {
    User saveUser(UserDto user);
    List<User> getUsers();
    void deleteUser(Long id);
    void updateRole(UserRequest userRequest);
    void updatePassword(LoginDto loginDto);
}
