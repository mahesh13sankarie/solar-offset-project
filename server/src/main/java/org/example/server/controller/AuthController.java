package org.example.server.controller;

import org.example.server.dto.LoginDto;
import org.example.server.dto.UserDto;
import org.example.server.entity.User;
import org.example.server.mapper.AuthResponseMapper;
import org.example.server.repository.UserRepository;
import org.example.server.service.auth.AuthService;
import org.example.server.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthResponseMapper responseMapper;

    //token for Google - OAuth2
    @GetMapping("/generatetoken")
    public ResponseEntity<?> generateOAuthToken(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return ResponseEntity.ok(Map.of("token", "")); //throw Error
        }
        //Logout is via /logout
        String email = authentication.getPrincipal().getAttribute("email");
        String name = authentication.getPrincipal().getAttribute("name");
        String token = tokenProvider.generateToken(email);
        User user = new User(email, name);

        return ResponseEntity.ok().body(responseMapper.buildLoginResponse(user.getDetail(user), token));
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody UserDto userDto) {
        authService.saveUser(userDto);
        return ResponseEntity.ok().body(responseMapper.buildSuccessResponse());
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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.email(), loginDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(loginDto.email());
        return ResponseEntity.ok().body(responseMapper.buildLoginResponse(user.getDetail(user), token));
    }

    //remove in front end;
    @PostMapping("/logout")
    ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(responseMapper.buildSuccessResponse());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidPassword(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }
}
