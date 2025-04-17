package org.example.server.service.auth;

import org.example.server.dto.LoginDto;
import org.example.server.dto.UserDto;
import org.example.server.dto.UserRequest;
import org.example.server.entity.User;
import org.example.server.mapper.UserMapper;
import org.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    UserMapper userMapper;

    @Override
    public User saveUser(UserDto user) {
        User mappedUser = userMapper.mapUser(user, encryptPassword(user.password()));
        userRepository.save(mappedUser);
        return mappedUser;
    }

    private String encryptPassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.fetchUsers().stream()
                .map(user -> user.getDetail(user))
                .collect(Collectors.toList());

    }

    public void deleteUser(Long id) {
        //check if user exist, make a global function..!
        userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        userRepository.deleteById(id);
    }

    @Override
    public void updateRole(UserRequest userRequest) {
        userRepository.updateRole(userRequest);
    }

    public void updatePassword(LoginDto loginDto) {
        //check if user is exist!
        UserDetails user = loadUserByUsername(loginDto.email());
        if (user == null) {
            throw new UsernameNotFoundException(loginDto.email());
        } else {
            userRepository.updatePassword(loginDto.email(), encryptPassword(loginDto.password()));
        }
    }
}
