package org.example.server.controller;

import jakarta.mail.MessagingException;
import org.example.server.dto.AccountType;
import org.example.server.dto.LoginDto;
import org.example.server.dto.MailDto;
import org.example.server.dto.UserDto;
import org.example.server.entity.MailAttributes;
import org.example.server.entity.User;
import org.example.server.mapper.AuthResponseMapper;
import org.example.server.mapper.BaseResponse;
import org.example.server.repository.UserRepository;
import org.example.server.service.auth.AuthService;
import org.example.server.service.mail.MailService;
import org.example.server.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * *
 * Google login path: <a href="http://localhost:8000/login/oauth2/code/google">...</a>
 * Clean code; so many duplicate!
 */
@RequestMapping("api/v1/auth")
@RestController
@CrossOrigin
public class AuthController {

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
        String token = tokenProvider.generateToken(email, AccountType.Google.ordinal());
        UserDto user = new UserDto(email, "", name, AccountType.Google);
        authService.saveUser(user);
        return ResponseEntity.ok().body(responseMapper.buildLoginResponse(user, token));
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody UserDto userDto) {
        User user = getUser(userDto.email());
        BaseResponse response;

        if (user == null) {
            authService.saveUser(userDto);
            response = responseMapper.buildSuccessResponse();
        } else {
            response = responseMapper.buildErrorMessage("User is already exist!", 400);
        }

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        User user = getUser(loginDto.email());
        if (user == null) {
            return ResponseEntity.ok().body(responseMapper.buildErrorMessage("Account does not exist!", 400));
        } else if (!isValidPassword(loginDto.password(), user.getPassword())) {
            return ResponseEntity.ok().body(responseMapper.buildErrorMessage("Wrong password!", 400));
        } else {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.email(), loginDto.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.generateToken(loginDto.email(), user.getAccountType());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.ok().body(responseMapper.buildLoginResponse(auth, token));

        }
    }

    //remove in front end;
    @PostMapping("/logout")
    ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(responseMapper.buildSuccessResponse());
    }

    @PostMapping("/forgot-password")
    ResponseEntity<?> sendEmail(@RequestBody MailDto email) throws MessagingException {
        String mail = email.email();
        User user = getUser(mail);
        if (user == null) {
            return ResponseEntity.ok().body(responseMapper.buildErrorMessage("Account does not exist!", 400));
        } else {
            String link = "https://localhost:5173/new-password?" + email.email(); //should be dynamic link
            String content = "<html><body>" +
                    "<p>Please click the following link: " +
                    "<a href=\"" + link + "\">Reset password</a></p>" +
                    "<p>Best regards,<br/>Syntax Squad</p>" +
                    "</body></html>";

            mailService.sendEmail(new MailAttributes(mail, "Solar Offset: Change password!",
                    "Dear " + mail, content)); //construct body with link!
            return ResponseEntity.ok().body(responseMapper.buildCustomMessage("Please check your e-mail!"));
        }
    }

    //could be use as regular change password as well!
    @PutMapping("/update-password")
    ResponseEntity<?> updatePassword(@RequestBody LoginDto loginDto) {
        User user = getUser(loginDto.email());
        if (user == null) {
            return ResponseEntity.ok().body(responseMapper.buildErrorMessage("Account does not exist!", 400));
        } else {
            authService.updatePassword(loginDto);
            return ResponseEntity.ok().body(responseMapper.buildCustomMessage("Password updated successfully!"));
        }
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidPassword(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }
}
