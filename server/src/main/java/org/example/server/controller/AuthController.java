package org.example.server.controller;

import java.util.List;
import java.util.Map;

import org.example.server.dto.AccountType;
import org.example.server.dto.EnquiryRequestDto;
import org.example.server.dto.LoginDto;
import org.example.server.dto.MailDto;
import org.example.server.dto.UserDto;
import org.example.server.entity.Enquiry;
import org.example.server.entity.MailAttributes;
import org.example.server.entity.User;
import org.example.server.mapper.AuthResponseMapper;
import org.example.server.repository.EnquiryRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.auth.AuthService;
import org.example.server.service.mail.MailService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.ApiResponseGenerator;
import org.example.server.utils.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;

/**
 * *
 * Google login path:
 * <a href="http://localhost:8000/login/oauth2/code/google">...</a>
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

    // token for Google - OAuth2
    @GetMapping("/generatetoken")
    public ApiResponse<ApiResponse.CustomBody<Object>> generateOAuthToken(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return ApiResponseGenerator.fail("AUTH_ERROR", "Authentication failed", HttpStatus.BAD_REQUEST);
        }
        // Logout is via /logout
        String email = authentication.getPrincipal().getAttribute("email");
        String name = authentication.getPrincipal().getAttribute("name");
        String token = tokenProvider.generateToken(email, AccountType.Google.ordinal());
        UserDto user = new UserDto(email, "", name, AccountType.Google);
        authService.saveUser(user);
        return ApiResponseGenerator.success(HttpStatus.OK, responseMapper.buildLoginResponse(user, token));
    }

    @PostMapping("/register")
    ApiResponse<ApiResponse.CustomBody<Object>> register(@RequestBody UserDto userDto) {
        authService.saveUser(userDto);
        return ApiResponseGenerator.success(HttpStatus.OK, responseMapper.buildSuccessResponse());
    }

    @PostMapping("/login")
    ApiResponse<ApiResponse.CustomBody<Object>> login(@RequestBody LoginDto loginDto) {
        User user = getUser(loginDto.email());

        if (user == null) {
            return ApiResponseGenerator.fail("USER_NOT_FOUND", "Account does not exist!", HttpStatus.BAD_REQUEST);
        } else if (!isValidPassword(loginDto.password(), user.getPassword())) {
            return ApiResponseGenerator.fail("INVALID_PASSWORD", "Wrong password!", HttpStatus.BAD_REQUEST);
        } else {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.email(), loginDto.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.generateToken(loginDto.email(), user.getAccountType());
            return ApiResponseGenerator.success(HttpStatus.OK,
                    responseMapper.buildLoginResponse(user.getDetail(user), token));
        }
    }

    // remove in front end;
    @PostMapping("/logout")
    ApiResponse<ApiResponse.CustomBody<Object>> logout() {
        SecurityContextHolder.clearContext();
        return ApiResponseGenerator.success(HttpStatus.OK, responseMapper.buildSuccessResponse());
    }

    @PostMapping("/forgot-password")
    ApiResponse<ApiResponse.CustomBody<Object>> sendEmail(@RequestBody MailDto email) throws MessagingException {
        String mail = email.email();
        User user = getUser(mail);
        if (user == null) {
            return ApiResponseGenerator.fail("USER_NOT_FOUND", "Account does not exist!", HttpStatus.NOT_FOUND);
        } else {
            String link = "https://localhost:5173/new-password?" + email.email(); // should be dynamic link
            String content = "<html><body>" +
                    "<p>Please click the following link: " +
                    "<a href=\"" + link + "\">Reset password</a></p>" +
                    "<p>Best regards,<br/>Syntax Squad</p>" +
                    "</body></html>";

            mailService.sendEmail(new MailAttributes(mail, "Solar Offset: Change password!",
                    "Dear " + mail, content)); // construct body with link!
            return ApiResponseGenerator.success(HttpStatus.OK, "Please check your e-mail!");
        }
    }

    // could be use as regular change password as well!
    @PutMapping("/update-password")
    ApiResponse<ApiResponse.CustomBody<Object>> updatePassword(@RequestBody LoginDto loginDto) {
        authService.updatePassword(loginDto);
        return ApiResponseGenerator.success(HttpStatus.OK,
                responseMapper.buildCustomMessage("Password updated successfully!"));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email);
    }

    private boolean isValidPassword(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }

    @GetMapping("/test")
    public ApiResponse<ApiResponse.CustomBody<Object>> test() {
        return ApiResponseGenerator.success(HttpStatus.OK, Map.of("message", "Server is running"));
    }

    // Receive token from frontend
    @PostMapping("/google-login")
    public ApiResponse<ApiResponse.CustomBody<Object>> googleLogin(@RequestBody Map<String, String> payload) {
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

            return ApiResponseGenerator.success(HttpStatus.OK, Map.of(
                    "token", token,
                    "data", user.getDetail(user)));

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseGenerator.fail("AUTH_ERROR", "Google login failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // contact us
    @PostMapping("/contact-us")
    public ApiResponse<ApiResponse.CustomBody<Object>> sendEnquiry(@RequestBody EnquiryRequestDto payload) {
        try {
            Enquiry enquiry = Enquiry.builder()
                    .subject(payload.subject())
                    .body(payload.body())
                    .name(payload.name())
                    .email(payload.email())
                    .build();

            enquiryRepository.save(enquiry);

            return ApiResponseGenerator.success(HttpStatus.OK, enquiry);

        } catch (Exception e) {
            return ApiResponseGenerator.fail("ENQUIRY_ERROR", "Send enquiry failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // contact us
    @GetMapping("/enquiries")
    public ApiResponse<ApiResponse.CustomBody<Object>> getEnquiries() {
        try {
            List<Enquiry> enquiries = enquiryRepository.findAll();
            return ApiResponseGenerator.success(HttpStatus.OK, enquiries);
        } catch (Exception e) {
            return ApiResponseGenerator.fail("ENQUIRY_ERROR", "Fetching enquiry failed: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
