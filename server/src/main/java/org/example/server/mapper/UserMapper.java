package org.example.server.mapper;

import org.example.server.dto.UserDto;
import org.example.server.entity.User;

public class UserMapper {
    public User mapUser(UserDto userDto, String encrypted){
        return new User(userDto.email(), encrypted, userDto.fullName(), userDto.accountType().ordinal());
    }
}
