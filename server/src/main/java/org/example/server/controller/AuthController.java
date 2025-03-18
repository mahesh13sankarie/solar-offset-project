package org.example.server.controller;

import org.example.server.dto.LoginDto;
import org.example.server.dto.UserDto;
import org.example.server.entity.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.auth.AuthService;
import org.example.server.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/**
 * *
 * Google login path: <a href="http://localhost:8000/login/oauth2/code/google">...</a>
 */
@RequestMapping("api/v1/auth")
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    private AuthService authService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    TokenProvider tokenProvider;

    //token for Google - OAuth2
    @GetMapping("/generatetoken")
    public ResponseEntity<Map<String, String>> generateOAuthToken(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return ResponseEntity.ok(Map.of("token", "")); //throw Error
        } //TODO adjust return type for customized error
        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();
        return ResponseEntity.ok(Map.of("token", accessToken)); //put constant in util?
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody UserDto userDto) {
        //TODO: check if account is exist! throw from SQLException
        authService.saveUser(userDto);
        return ResponseEntity.ok("Registration is successful!");
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        User user = getUser(loginDto.email());

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!isValidPassword(loginDto.password(), user.getPassword())) {
            return ResponseEntity.notFound().build();
        }
        String token = tokenProvider.generateToken(user);
        return ResponseEntity.ok(token);//TODO: return token
    }

    //remove in front end; refresh token!
    @PostMapping("/logout")
    ResponseEntity<?> logout(@RequestBody LoginDto loginDto) {
        User user = getUser(loginDto.email());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String token = tokenProvider.generateToken(user);
        return ResponseEntity.ok(token); //refresh token
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidPassword(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }
}
