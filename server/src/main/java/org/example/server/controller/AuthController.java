package org.example.server.controller;

import org.example.server.dto.AccountType;
import org.example.server.dto.LoginDto;
import org.example.server.dto.MailDto;
import org.example.server.dto.UserDto;
import org.example.server.dto.GoogleSignInDTO;
import org.example.server.entity.MailAttributes;
import org.example.server.entity.User;
import org.example.server.mapper.AuthResponseMapper;
import org.example.server.mapper.UserMapper;
import org.example.server.repository.UserRepository;
import org.example.server.service.auth.AuthService;
import org.example.server.service.mail.MailService;
import org.example.server.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;
import java.util.Map;


/**
 * *
 * Google login path: <a href="http://localhost:8000/login/oauth2/code/google">...</a>
 */

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private static final String CLIENT_ID = "981770573082-avncdn59tmpu735gaofksnp9ijvhle53.apps.googleusercontent.com";

    @Autowired
    private UserRepository userRepository;

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

    @Autowired
    MailService mailService;

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
        UserDto user = new UserDto(email, "", name, AccountType.Google);
        authService.saveUser(user);
        return ResponseEntity.ok().body(responseMapper.buildLoginResponse(user, token));
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

    @PostMapping("/forgot-password")
    ResponseEntity<?> sendEmail(@RequestBody MailDto email) {
        //TODO: send email with information and link, in link put email info so FE could retrieve!
        //check if user exist
        String mail = email.email();
        mailService.sendEmail(new MailAttributes(mail, "Solar Offset: Change password!", "Dear " + mail + " please change your password thru out this link!")); //construct body with link!
        return ResponseEntity.ok().body(responseMapper.buildCustomMessage("Please check your e-mail!"));
    }

    //could be use as regular change password as well!
    @PutMapping("/update-password")
    ResponseEntity<?> updatePassword(@RequestBody LoginDto loginDto) {
        authService.updatePassword(loginDto);
        return ResponseEntity.ok().body(responseMapper.buildCustomMessage("Password updated successfully!"));
    }


    private User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidPassword(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("message", "Server is running"));
    }

    // Receive token from frontend
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String name = payload.get("name");

            User user = userRepository.findByEmail(email);
            if (user == null) {
                UserDto newUser = new UserDto(email, "", name, AccountType.Google);
                authService.saveUser(newUser);
                user = userRepository.findByEmail(email);
            }

            String token = tokenProvider.generateToken(email);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "token", token,
                    "data", user.getDetail(user)
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Google login failed",
                    "error", e.getMessage()
            ));
        }
    }


}
