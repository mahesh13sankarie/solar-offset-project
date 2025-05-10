package org.example.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.example.server.dto.UserRequest;
import org.example.server.entity.User;
import org.example.server.mapper.AuthResponseMapper;
import org.example.server.service.auth.AuthService;
import org.example.server.utils.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class AdminControllerTest {

	private AdminController adminController;
	private AuthService authService;
	private AuthResponseMapper responseMapper;

	@BeforeEach
	void setUp() {
		// Mock the service and mapper
		authService = mock(AuthService.class);
		responseMapper = mock(AuthResponseMapper.class);

		// Create controller with mocked dependencies
		adminController = new AdminController();

		// Set dependencies using reflection (since there are no setters)
		try {
			java.lang.reflect.Field authServiceField = AdminController.class.getDeclaredField("authService");
			authServiceField.setAccessible(true);
			authServiceField.set(adminController, authService);

			java.lang.reflect.Field responseMapperField = AdminController.class.getDeclaredField("responseMapper");
			responseMapperField.setAccessible(true);
			responseMapperField.set(adminController, responseMapper);
		} catch (Exception e) {
			throw new RuntimeException("Error setting up test dependencies", e);
		}
	}

	@Test
	@DisplayName("HTTP 200 OK: Returns users when list is not empty")
	void fetchUser_ReturnsUsers_WhenUsersExist() {
		// Arrange
		List<User> mockUsers = Arrays.asList(
				createMockUser(1L, "user1@example.com", "User One"),
				createMockUser(2L, "user2@example.com", "User Two"));

		when(authService.getUsers()).thenReturn(mockUsers);

		// Act
		ApiResponse<ApiResponse.CustomBody<List<User>>> response = adminController.fetchUser();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertEquals(2, response.getBody().getResponse().size());
		assertNull(response.getBody().getError());
	}

	@Test
	@DisplayName("HTTP 200 OK: Returns empty message when no users exist")
	void fetchUser_ReturnsEmptyMessage_WhenNoUsers() {
		// Arrange
		List<User> emptyUsers = new ArrayList<>();
		when(authService.getUsers()).thenReturn(emptyUsers);

		// Act
		ApiResponse<ApiResponse.CustomBody<List<User>>> response = adminController.fetchUser();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().getSuccess());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getError());
		assertEquals("EMPTY_USER_LIST", response.getBody().getError().getCode());
		assertEquals("No users found", response.getBody().getError().getMessage());
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully updates user role")
	void updateRole_ReturnsSuccess() {
		// Arrange - Create POJO directly instead of mocking
		UserRequest validRequest = createUserRequest(1L, Optional.of(2));
		doNothing().when(authService).updateRole(any(UserRequest.class));

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = adminController.updateRole(validRequest);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNull(response.getBody().getError());

		// Verify service was called
		verify(authService).updateRole(any(UserRequest.class));
	}

	@Test
	@DisplayName("HTTP 400 BAD REQUEST: Rejects update when user request is invalid")
	void updateRole_ReturnsBadRequest_WhenUserRequestInvalid() {
		// Arrange - null userId
		UserRequest invalidRequest = createUserRequest(null, Optional.of(2));

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = adminController.updateRole(invalidRequest);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertFalse(response.getBody().getSuccess());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getError());
		assertEquals("VALIDATION_ERROR", response.getBody().getError().getCode());
		assertEquals("User ID must not be null", response.getBody().getError().getMessage());
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully deletes user")
	void deleteUser_ReturnsSuccess() {
		// Arrange
		UserRequest validRequest = createUserRequest(1L, Optional.empty());
		doNothing().when(authService).deleteUser(anyLong());

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = adminController.deleteUser(validRequest);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNull(response.getBody().getError());

		// Verify service was called with correct ID
		verify(authService).deleteUser(1L);
	}

	@Test
	@DisplayName("HTTP 400 BAD REQUEST: Rejects delete when user request is invalid")
	void deleteUser_ReturnsBadRequest_WhenUserRequestInvalid() {
		// Arrange - null userId
		UserRequest invalidRequest = createUserRequest(null, Optional.empty());

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = adminController.deleteUser(invalidRequest);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertFalse(response.getBody().getSuccess());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getError());
		assertEquals("VALIDATION_ERROR", response.getBody().getError().getCode());
		assertEquals("User ID must not be null", response.getBody().getError().getMessage());
	}

	// Helper method to create UserRequest objects directly
	private UserRequest createUserRequest(Long userId, Optional<Integer> accountType) {
		return new UserRequest(userId, accountType);
	}

	// Helper method to create mock User
	private User createMockUser(Long id, String email, String fullName) {
		User mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(id);
		when(mockUser.getEmail()).thenReturn(email);
		when(mockUser.getFullName()).thenReturn(fullName);
		return mockUser;
	}
}