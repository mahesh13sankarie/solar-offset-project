package org.example.server.service.auth;

import org.example.server.dto.UserDto;
import org.example.server.entity.User;
import org.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User saveUser(UserDto user) {
        //TODO: map dto to user!
        //userRepository.save(user);
        return null;
    }
}
