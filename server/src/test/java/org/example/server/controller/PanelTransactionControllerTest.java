package org.example.server.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.StaffTransactionDTO;
import org.example.server.entity.Country;
import org.example.server.entity.CountryPanel;
import org.example.server.entity.Panel;
import org.example.server.entity.PanelTransaction;
import org.example.server.entity.Payment;
import org.example.server.entity.User;
import org.example.server.entity.response.PanelTransactionResponse;
import org.example.server.service.panel.PanelTransactionService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.PaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class PanelTransactionControllerTest {

	private PanelTransactionController panelTransactionController;
	private PanelTransactionService panelTransactionService;

	@BeforeEach
	void setUp() {
		// Mock the service layer
		panelTransactionService = mock(PanelTransactionService.class);

		// Create controller instance
		panelTransactionController = new PanelTransactionController();

		// Set dependencies using reflection (since there are no setters)
		try {
			java.lang.reflect.Field field = PanelTransactionController.class
					.getDeclaredField("panelTransactionService");
			field.setAccessible(true);
			field.set(panelTransactionController, panelTransactionService);
		} catch (Exception e) {
			throw new RuntimeException("Error setting up test dependencies", e);
		}
	}

	@Test
	@DisplayName("HTTP 200 OK: Successfully adds a panel transaction")
	void addTransaction_ReturnsSuccess() {
		// Arrange
		PanelTransactionDTO transactionDTO = new PanelTransactionDTO(1L, 2L, 3L);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = panelTransactionController
				.addTransaction(transactionDTO);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNull(response.getBody().getError());

		// Verify that panelTransactionService.save was called with the correct
		// parameters
		verify(panelTransactionService).save(transactionDTO);
	}

	@Test
	@DisplayName("HTTP 200 OK: Returns all transactions")
	void getAllTransaction_ReturnsAllTransactions() {
		// Arrange - Create a properly mocked PanelTransaction that won't throw
		// exceptions
		List<PanelTransaction> transactions = Arrays.asList(
				createFullyMockedPanelTransaction(1L),
				createFullyMockedPanelTransaction(2L));

		// Set up the mock service
		when(panelTransactionService.fetchAll()).thenReturn(transactions);

		// Act
		ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> response = panelTransactionController
				.getAllTransaction();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertNull(response.getBody().getError());
	}

	@Test
	@DisplayName("HTTP 200 OK: Returns transactions for a specific user")
	void getTransactionByUserId_ReturnsUserTransactions() {
		// Arrange
		Long userId = 1L;
		List<PanelTransaction> transactions = Arrays.asList(
				createFullyMockedPanelTransaction(userId),
				createFullyMockedPanelTransaction(userId));

		// Set up the mock service
		when(panelTransactionService.fetchById(userId)).thenReturn(transactions);

		// Act
		ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> response = panelTransactionController
				.getTransactionByUserId(userId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertNull(response.getBody().getError());

		// Verify that the service was called with the correct user ID
		verify(panelTransactionService).fetchById(userId);
	}

	@Test
	@DisplayName("HTTP 200 OK: Returns staff transaction history")
	void getStaffTransactionHistory_ReturnsStaffTransactions() {
		// Arrange
		List<StaffTransactionDTO> staffTransactions = Arrays.asList(
				new StaffTransactionDTO("2023-01-01", "User1", "USA", "Solar Panel A", 3, 400, 250.0),
				new StaffTransactionDTO("2023-01-02", "User2", "Germany", "Solar Panel B", 1, 350, 175.0));

		when(panelTransactionService.getStaffTransactionHistory()).thenReturn(staffTransactions);

		// Act
		ApiResponse<ApiResponse.CustomBody<List<StaffTransactionDTO>>> response = panelTransactionController
				.getStaffTransactionHistory();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().getSuccess());
		assertNotNull(response.getBody().getResponse());
		assertEquals(2, response.getBody().getResponse().size());
		assertEquals("User1", response.getBody().getResponse().get(0).user());
		assertEquals("Solar Panel B", response.getBody().getResponse().get(1).panelType());
		assertNull(response.getBody().getError());
	}

	@Test
	@DisplayName("HTTP 400 Bad Request: Fails when transaction data is invalid")
	void addTransaction_WithInvalidData_ReturnsBadRequest() {
		// Arrange - create invalid data (missing required fields)
		PanelTransactionDTO invalidData = new PanelTransactionDTO(null, 2L, 3L);

		// Act
		ApiResponse<ApiResponse.CustomBody<Object>> response = panelTransactionController.addTransaction(invalidData);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertFalse(response.getBody().getSuccess());
		assertNull(response.getBody().getResponse());
		assertNotNull(response.getBody().getError());
		assertEquals("VALIDATION_ERROR", response.getBody().getError().getCode());
		assertEquals("User ID must not be null", response.getBody().getError().getMessage());
	}

	// Helper method to create fully mocked PanelTransaction objects with all
	// necessary nested objects
	private PanelTransaction createFullyMockedPanelTransaction(Long userId) {
		PanelTransaction transaction = mock(PanelTransaction.class);

		// Mock User
		User mockUser = mock(User.class);
		when(mockUser.getId()).thenReturn(userId);
		when(mockUser.getEmail()).thenReturn("user" + userId + "@example.com");
		when(mockUser.getFullName()).thenReturn("User " + userId);
		when(mockUser.getDetail(any())).thenReturn(mockUser);

		// Mock Panel
		Panel mockPanel = mock(Panel.class);
		when(mockPanel.getId()).thenReturn(userId);
		when(mockPanel.getName()).thenReturn("Test Panel " + userId);
		when(mockPanel.getInstallationCost()).thenReturn(100.0);
		when(mockPanel.getProductionPerPanel()).thenReturn(200.0);
		when(mockPanel.getDescription()).thenReturn("Test Description");
		when(mockPanel.getEfficiency()).thenReturn("90%");
		when(mockPanel.getLifespan()).thenReturn("25 years");
		when(mockPanel.getTemperatureTolerance()).thenReturn("80°C");
		when(mockPanel.getWarranty()).thenReturn("10 years");

		// Mock Country
		Country mockCountry = mock(Country.class);
		when(mockCountry.getCode()).thenReturn("US");

		// Mock CountryPanel
		CountryPanel mockCountryPanel = mock(CountryPanel.class);
		when(mockCountryPanel.getId()).thenReturn(userId);
		when(mockCountryPanel.getCountry()).thenReturn(mockCountry);
		when(mockCountryPanel.getPanel()).thenReturn(mockPanel);

		// Mock Payment
		Payment mockPayment = mock(Payment.class);
		when(mockPayment.getAmount()).thenReturn(500);
		when(mockPayment.getType()).thenReturn(PaymentType.STRIPE.name());
		when(mockPayment.getCountryPanel()).thenReturn(mockCountryPanel);

		// Set up transaction with all mocked components
		when(transaction.getId()).thenReturn(userId);
		when(transaction.getUser()).thenReturn(mockUser);
		when(transaction.getPanel()).thenReturn(mockCountryPanel);
		when(transaction.getPayment()).thenReturn(mockPayment);

		return transaction;
	}
}