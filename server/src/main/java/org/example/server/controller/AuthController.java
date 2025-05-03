package org.example.server.controller;

import jakarta.mail.MessagingException;
import org.example.server.dto.*;
import org.example.server.entity.Enquiry;
import org.example.server.entity.MailAttributes;
import org.example.server.entity.User;
import org.example.server.mapper.AuthResponseMapper;
import org.example.server.repository.EnquiryRepository;
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

import java.util.List;
import java.util.Map;


/**
 * *
 * Google login path: <a href="http://localhost:8000/login/oauth2/code/google">...</a>
 */

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private static final String CLIENT_ID = "234686151907-h0egb9h34beugoudlrffovu95nkt4a10.apps.googleusercontent.com";

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

    @Autowired
    private EnquiryRepository enquiryRepository;

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
        authService.saveUser(userDto);
        return ResponseEntity.ok().body(responseMapper.buildSuccessResponse());
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
            return ResponseEntity.ok().body(responseMapper.buildLoginResponse(user.getDetail(user), token));
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

            String token = tokenProvider.generateToken(email, AccountType.Google.ordinal());

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
    //contact us
    @PostMapping("/contact-us")
    public ResponseEntity<?> sendEnquiry(@RequestBody EnquiryRequestDto payload) {
        try {
            Enquiry enquiry = Enquiry.builder()
                    .subject(payload.subject())
                    .body(payload.body())
                    .name(payload.name())
                    .email(payload.email())
                    .build();

            enquiryRepository.save(enquiry);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", enquiry
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Send enquiry failed",
                    "error", e.getMessage()
            ));
        }
    }

    //contact us
    @GetMapping("/enquiries")
    public ResponseEntity<?> getEnquiries() {
        try {
            List <Enquiry> enquiries = enquiryRepository.findAll();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", enquiries
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Fetching enquiry failed",
                    "error", e.getMessage()
            ));
        }
    }

}
