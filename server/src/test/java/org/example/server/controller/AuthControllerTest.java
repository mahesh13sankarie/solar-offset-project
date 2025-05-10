package org.example.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.example.server.mapper.BaseResponse;
import org.example.server.repository.EnquiryRepository;
import org.example.server.repository.UserRepository;
import org.example.server.service.auth.AuthService;
import org.example.server.service.mail.MailService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

class AuthControllerTest {

	private AuthController authController;
	private AuthService authService;
	private UserRepository userRepository;
	private BCryptPasswordEncoder encoder;
	private TokenProvider tokenProvider;
	private AuthenticationManager authenticationManager;
	private AuthResponseMapper responseMapper;
	private MailService mailService;
	private EnquiryRepository enquiryRepository;

	@BeforeEach
	void setUp() {
		// Mock all dependencies
		authService = mock(AuthService.class);
		userRepository = mock(UserRepository.class);
		encoder = mock(BCryptPasswordEncoder.class);
		tokenProvider = mock(TokenProvider.class);
		authenticationManager = mock(AuthenticationManager.class);
		responseMapper = mock(AuthResponseMapper.class);
		mailService = mock(MailService.class);
		enquiryRepository = mock(EnquiryRepository.class);

		// Create controller with mocked dependencies using constructor injection
		authController = new AuthController(
				userRepository,
				authService,
				encoder,
				tokenProvider,
				authenticationManager,
				responseMapper,
				mailService,
				enquiryRepository);
	}

	@Test
	@DisplayName("HTTP 200 OK: Tests if server is running")
	void test_ReturnsServerIsRunning() {
		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.test();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());

		@SuppressWarnings("unchecked")
		Map<String, String> responseData = (Map<String, String>) response.getBody().getResponse();
		assertEquals("Server is running", responseData.get("message"));
		assertNull(response.getBody().getError());
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully registers a new user")
	void register_ReturnsSuccess() {
		// Arrange
		UserDto userDto = new UserDto("test@example.com", "password123", "Test User", AccountType.Standard);
		BaseResponse successResponse = new BaseResponse(200, "", "", null);

		when(authService.saveUser(any(UserDto.class))).thenReturn(null);
		when(responseMapper.buildSuccessResponse()).thenReturn(successResponse);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.register(userDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNull(response.getBody().getError());

		// Verify service was called
		verify(authService).saveUser(userDto);
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully logs in a user")
	void login_ReturnsTokenAndUserData() {
		// Arrange
		LoginDto loginDto = new LoginDto("test@example.com", "password123");
		User user = mock(User.class);
		User userDetails = mock(User.class);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				loginDto.email(), loginDto.password());
		BaseResponse loginResponse = new BaseResponse(200, "", "dummy-token", userDetails);

		when(userRepository.findByEmail(loginDto.email())).thenReturn(user);
		when(user.getPassword()).thenReturn("encodedPassword");
		when(user.getAccountType()).thenReturn(0);
		when(user.getDetail(user)).thenReturn(userDetails);
		when(encoder.matches(loginDto.password(), user.getPassword())).thenReturn(true);
		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(tokenProvider.generateToken(loginDto.email(), user.getAccountType())).thenReturn("dummy-token");
		when(responseMapper.buildLoginResponse(userDetails, "dummy-token")).thenReturn(loginResponse);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.login(loginDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertNull(response.getBody().getError());
	}

	@Test
	@DisplayName("HTTP 400 Bad Request: Account does not exist")
	void login_WhenUserNotFound_ReturnsError() {
		// Arrange
		LoginDto loginDto = new LoginDto("nonexistent@example.com", "password123");

		when(userRepository.findByEmail(loginDto.email())).thenReturn(null);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.login(loginDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertFalse(response.getBody().getSuccess());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getError());
		assertEquals("USER_NOT_FOUND", response.getBody().getError().getCode());
		assertEquals("Account does not exist!", response.getBody().getError().getMessage());
	}

	@Test
	@DisplayName("HTTP 400 Bad Request: Invalid password")
	void login_WhenPasswordInvalid_ReturnsError() {
		// Arrange
		LoginDto loginDto = new LoginDto("test@example.com", "wrongPassword");
		User user = mock(User.class);

		when(userRepository.findByEmail(loginDto.email())).thenReturn(user);
		when(user.getPassword()).thenReturn("encodedPassword");
		when(encoder.matches(loginDto.password(), user.getPassword())).thenReturn(false);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.login(loginDto);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertFalse(response.getBody().getSuccess());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getError());
		assertEquals("INVALID_PASSWORD", response.getBody().getError().getCode());
		assertEquals("Wrong password!", response.getBody().getError().getMessage());
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully logs out a user")
	void logout_ReturnsSuccess() {
		// Arrange
		BaseResponse successResponse = new BaseResponse(200, "", "", null);
		when(responseMapper.buildSuccessResponse()).thenReturn(successResponse);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.logout();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNull(response.getBody().getError());
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully sends password reset email")
	void sendEmail_ReturnsSentEmail() throws Exception {
		// Arrange
		MailDto mailDto = new MailDto("test@example.com");
		User user = mock(User.class);

		when(userRepository.findByEmail(mailDto.email())).thenReturn(user);
		doNothing().when(mailService).sendEmail(any(MailAttributes.class));

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.sendEmail(mailDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertEquals("Please check your e-mail!", response.getBody().getResponse());
		assertNull(response.getBody().getError());

		// Verify email service was called
		verify(mailService).sendEmail(any(MailAttributes.class));
	}

	@Test
	@DisplayName("HTTP 404 Not Found: User not found for password reset")
	void sendEmail_WhenUserNotFound_ReturnsError() throws Exception {
		// Arrange
		MailDto mailDto = new MailDto("nonexistent@example.com");

		when(userRepository.findByEmail(mailDto.email())).thenReturn(null);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.sendEmail(mailDto);

		// Assert
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertFalse(response.getBody().getSuccess());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getError());
		assertEquals("USER_NOT_FOUND", response.getBody().getError().getCode());
		assertEquals("Account does not exist!", response.getBody().getError().getMessage());
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully updates user password")
	void updatePassword_ReturnsSuccess() {
		// Arrange
		LoginDto loginDto = new LoginDto("test@example.com", "newPassword123");
		BaseResponse successResponse = new BaseResponse(200, "Password updated successfully!", "", null);

		doNothing().when(authService).updatePassword(loginDto);
		when(responseMapper.buildCustomMessage("Password updated successfully!")).thenReturn(successResponse);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.updatePassword(loginDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertNull(response.getBody().getError());

		// Verify service was called
		verify(authService).updatePassword(loginDto);
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully completes Google login")
	void googleLogin_ReturnsTokenAndUserData() {
		// Arrange
		Map<String, String> payload = Map.of(
				"email", "test@gmail.com",
				"name", "Google User");
		User user = mock(User.class);
		User userDetails = mock(User.class);

		when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);
		when(user.getDetail(user)).thenReturn(userDetails);
		when(tokenProvider.generateToken("test@gmail.com", AccountType.Google.ordinal()))
				.thenReturn("google-dummy-token");

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.googleLogin(payload);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertNull(response.getBody().getError());

		// Verify token generation
		verify(tokenProvider).generateToken("test@gmail.com", AccountType.Google.ordinal());
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully handles GoogleOAuth token")
	void generateOAuthToken_ReturnsSuccess() {
		// Arrange
		OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
		OAuth2User oauth2User = mock(OAuth2User.class);
		BaseResponse loginResponse = new BaseResponse(200, "", "oauth-token", "userData");

		when(authentication.getPrincipal()).thenReturn(oauth2User);
		when(oauth2User.getAttribute("email")).thenReturn("test@gmail.com");
		when(oauth2User.getAttribute("name")).thenReturn("Google User");
		when(tokenProvider.generateToken(eq("test@gmail.com"), anyInt())).thenReturn("oauth-token");
		when(responseMapper.buildLoginResponse(any(UserDto.class), eq("oauth-token"))).thenReturn(loginResponse);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.generateOAuthToken(authentication);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertNull(response.getBody().getError());
	}

	@Test
	@DisplayName("HTTP 400 Bad Request: Authentication is null")
	void generateOAuthToken_WhenNullAuthentication_ReturnsError() {
		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.generateOAuthToken(null);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertFalse(response.getBody().getSuccess());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getError());
		assertEquals("AUTH_ERROR", response.getBody().getError().getCode());
		assertEquals("Authentication failed", response.getBody().getError().getMessage());
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully adds enquiry")
	void sendEnquiry_ReturnsSuccess() {
		// Arrange
		EnquiryRequestDto requestDto = new EnquiryRequestDto("Test Name", "test@example.com", "Test Subject",
				"Test Body");
		Enquiry savedEnquiry = Enquiry.builder()
				.id(1L)
				.name(requestDto.name())
				.email(requestDto.email())
				.subject(requestDto.subject())
				.body(requestDto.body())
				.build();

		when(enquiryRepository.save(any(Enquiry.class))).thenReturn(savedEnquiry);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.sendEnquiry(requestDto);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertNull(response.getBody().getError());

		// Verify repository was called
		verify(enquiryRepository).save(any(Enquiry.class));
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully gets all enquiries")
	void getEnquiries_ReturnsAllEnquiries() {
		// Arrange
		List<Enquiry> enquiries = new ArrayList<>();
		enquiries.add(Enquiry.builder().id(1L).name("Test").email("test@example.com").subject("Subject 1")
				.body("Body 1").build());
		enquiries.add(Enquiry.builder().id(2L).name("Test 2").email("test2@example.com").subject("Subject 2")
				.body("Body 2").build());

		when(enquiryRepository.findAll()).thenReturn(enquiries);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = authController.getEnquiries();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertEquals(enquiries, response.getBody().getResponse());
		assertNull(response.getBody().getError());

		// Verify repository was called
		verify(enquiryRepository).findAll();
	}
}