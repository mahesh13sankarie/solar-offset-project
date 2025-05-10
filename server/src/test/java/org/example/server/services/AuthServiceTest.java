package org.example.server.services;

import org.example.server.dto.AccountType;
import org.example.server.dto.LoginDto;
import org.example.server.dto.UserDto;
import org.example.server.dto.UserRequest;
import org.example.server.entity.User;
import org.example.server.mapper.UserMapper;
import org.example.server.repository.UserRepository;
import org.example.server.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author: astidhiyaa
 * @date: 30/04/25
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private User buildUser(AccountType accountType) {
        return new User("test@gmail.com", "password", "User", accountType.ordinal());
    }

    private UserDto buildUserDto(AccountType accountType) {
        return new UserDto("test@gmail.com", "password", "User", accountType);
    }

    private LoginDto buildLoginDto() {
        return new LoginDto("test@gmail.com", "password");
    }

    @Test
    void test_standard_account() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("password");

        UserDto userDto = buildUserDto(AccountType.Standard);
        User user = buildUser(AccountType.Standard);

        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userMapper.mapUser(any(), anyString())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        // when
        User result = authService.saveUser(userDto);

        // then
        assertNotNull(result);
        assertEquals(userDto.email(), result.getEmail());
        assertEquals(userDto.accountType().ordinal(), result.getAccountType());
    }

    @Test
    void test_admin_account() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("password");

        UserDto userDto = buildUserDto(AccountType.Admin);
        User user = buildUser(AccountType.Admin);

        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userMapper.mapUser(any(), anyString())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        // when
        User result = authService.saveUser(userDto);

        // then
        assertNotNull(result);
        assertEquals(userDto.email(), result.getEmail());
        assertEquals(userDto.accountType().ordinal(), result.getAccountType());
    }

    @Test
    void test_google_account() {
        // given
        when(passwordEncoder.encode(anyString())).thenReturn("password");

        UserDto userDto = buildUserDto(AccountType.Google);
        User user = buildUser(AccountType.Google);

        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userMapper.mapUser(any(), anyString())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        // when
        User result = authService.saveUser(userDto);

        // then
        assertNotNull(result);
        assertEquals(userDto.email(), result.getEmail());
        assertEquals(userDto.accountType().ordinal(), result.getAccountType());
    }

    @Test
    void test_update_password_success() {
        // given
        LoginDto loginDto = buildLoginDto();
        User user = buildUser(AccountType.Standard);

        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        // when
        authService.updatePassword(loginDto);

        // then
        verify(userRepository).updatePassword(loginDto.email(), loginDto.password());
    }

    @Test
    void test_update_password_not_found() {
        // given
        LoginDto loginDto = buildLoginDto();

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // when & then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> authService.updatePassword(loginDto));
        assertEquals(loginDto.email(), exception.getMessage());
    }

    @Test
    void test_update_role() {
        // given
        UserRequest userRequest = new UserRequest(1L, Optional.empty());

        // when
        authService.updateRole(userRequest);

        // then
        verify(userRepository).updateRole(userRequest);
    }

    @Test
    void test_delete_user_success() {
        // given
        Long userId = 1L;
        User user = buildUser(AccountType.Standard);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        authService.deleteUser(userId);

        // then
        verify(userRepository).findById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void test_delete_user_not_found() {
        // given
        Long userId = 1L;

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        //when and then
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> authService.deleteUser(userId));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void test_get_users() {
        // given
        List<User> users = List.of(buildUser(AccountType.Standard));

        when(userRepository.fetchUsers()).thenReturn(users);

        // when
        authService.getUsers();

        // then
        verify(userRepository).fetchUsers();
    }

    @Test
    void test_load_uname() {
        // given
        User user = buildUser(AccountType.Standard);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        // when
        authService.loadUserByUsername(user.getEmail());

        // then
        verify(userRepository).findByEmail(user.getEmail());
    }
}
