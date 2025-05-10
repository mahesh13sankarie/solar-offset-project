package org.example.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.dto.AccountType;
import org.example.server.dto.LoginDto;
import org.example.server.dto.MailDto;
import org.example.server.dto.UserDto;
import org.example.server.entity.User;
import org.example.server.repository.UserRepository;
import org.example.server.service.auth.AuthService;
import org.example.server.service.mail.MailService;
import org.example.server.utils.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private BCryptPasswordEncoder encoder;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private MailService mailService;

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Tests if server is running")
	void test_ReturnsServerIsRunning() throws Exception {
		// Act & Assert
		mockMvc.perform(get("/api/v1/auth/test")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.response.message").value("Server is running"))
				.andExpect(jsonPath("$.error").doesNotExist());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully registers a new user")
	void register_ReturnsSuccess() throws Exception {
		// Arrange
		UserDto userDto = new UserDto("test@example.com", "password123", "Test User", AccountType.Standard);

		// Mock the service to return success response
		when(authService.saveUser(any(UserDto.class))).thenReturn(null);

		// Act & Assert
		mockMvc.perform(post("/api/v1/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.response").doesNotExist())
				.andExpect(jsonPath("$.error").doesNotExist());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully logs in a user")
	void login_ReturnsTokenAndUserData() throws Exception {
		// Arrange
		LoginDto loginDto = new LoginDto("test@example.com", "password123");

		// Mock User
		User user = Mockito.mock(User.class);
		when(user.getAccountType()).thenReturn(0);
		when(user.getPassword()).thenReturn("encodedPassword");

		User userDetails = Mockito.mock(User.class);
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("email", "test@example.com");
		responseMap.put("name", "Test User");
		responseMap.put("token", "dummy-token");
		when(user.getDetail(user)).thenReturn(userDetails);

		// Mock authentication
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				loginDto.email(), loginDto.password());

		// Mock repository, encoder, authentication and token
		when(userRepository.findByEmail(loginDto.email())).thenReturn(user);
		when(encoder.matches(loginDto.password(), user.getPassword())).thenReturn(true);
		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(tokenProvider.generateToken(loginDto.email(), user.getAccountType())).thenReturn("dummy-token");

		// Act & Assert
		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.response").exists())
				.andExpect(jsonPath("$.response.token").value("dummy-token"))
				.andExpect(jsonPath("$.error").doesNotExist());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully logs out a user")
	void logout_ReturnsSuccess() throws Exception {
		// Act & Assert
		mockMvc.perform(post("/api/v1/auth/logout")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.response").doesNotExist())
				.andExpect(jsonPath("$.error").doesNotExist());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully sends password reset email")
	void sendEmail_ReturnsSentEmail() throws Exception {
		// Arrange
		MailDto mailDto = new MailDto("test@example.com");

		// Mock user
		User user = Mockito.mock(User.class);
		when(userRepository.findByEmail(mailDto.email())).thenReturn(user);

		// Act & Assert
		mockMvc.perform(post("/api/v1/auth/forgot-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mailDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.response").value("Please check your e-mail!"))
				.andExpect(jsonPath("$.error").doesNotExist());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully updates user password")
	void updatePassword_ReturnsSuccess() throws Exception {
		// Arrange
		LoginDto loginDto = new LoginDto("test@example.com", "newPassword123");

		// Act & Assert
		mockMvc.perform(put("/api/v1/auth/update-password")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.response").value("Password updated successfully!"))
				.andExpect(jsonPath("$.error").doesNotExist());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully completes Google login")
	void googleLogin_ReturnsTokenAndUserData() throws Exception {
		// Arrange
		Map<String, String> payload = new HashMap<>();
		payload.put("email", "test@gmail.com");
		payload.put("name", "Google User");

		// Mock user
		User user = Mockito.mock(User.class);
		User userDetails = Mockito.mock(User.class);
		when(user.getDetail(user)).thenReturn(userDetails);
		when(userRepository.findByEmail("test@gmail.com")).thenReturn(user);

		// Mock token
		when(tokenProvider.generateToken("test@gmail.com", AccountType.Google.ordinal()))
				.thenReturn("google-dummy-token");

		// Act & Assert
		mockMvc.perform(post("/api/v1/auth/google-login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(payload)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.response").exists())
				.andExpect(jsonPath("$.response.token").value("google-dummy-token"))
				.andExpect(jsonPath("$.error").doesNotExist());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 400 Bad Request: Account does not exist")
	void login_WhenUserNotFound_ReturnsError() throws Exception {
		// Arrange
		LoginDto loginDto = new LoginDto("nonexistent@example.com", "password123");

		// Mock repository to return null (user not found)
		when(userRepository.findByEmail(loginDto.email())).thenReturn(null);

		// Act & Assert
		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.response").doesNotExist())
				.andExpect(jsonPath("$.error.code").value("400"))
				.andExpect(jsonPath("$.error.message").value("Account does not exist!"));
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 400 Bad Request: Invalid password")
	void login_WhenPasswordInvalid_ReturnsError() throws Exception {
		// Arrange
		LoginDto loginDto = new LoginDto("test@example.com", "wrongPassword");

		// Mock User
		User user = Mockito.mock(User.class);
		when(user.getPassword()).thenReturn("encodedPassword");

		// Mock repository and encoder
		when(userRepository.findByEmail(loginDto.email())).thenReturn(user);
		when(encoder.matches(loginDto.password(), user.getPassword())).thenReturn(false);

		// Act & Assert
		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.response").doesNotExist())
				.andExpect(jsonPath("$.error.code").value("400"))
				.andExpect(jsonPath("$.error.message").value("Wrong password!"));
	}
}