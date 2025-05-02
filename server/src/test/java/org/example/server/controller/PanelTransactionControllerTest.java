package org.example.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.StaffTransactionDTO;
import org.example.server.entity.*;
import org.example.server.service.panel.PanelTransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PanelTransactionController.class)
@Import(TestSecurityConfig.class)
class PanelTransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PanelTransactionService panelTransactionService;

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Successfully adds a panel transaction")
	void addTransaction_ReturnsSuccess() throws Exception {
		// Arrange
		// Using the record constructor pattern instead of setters
		PanelTransactionDTO transactionDTO = new PanelTransactionDTO(1L, 2L, 3L);

		// Act & Assert
		mockMvc.perform(post("/api/v1/transaction/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionDTO)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Returns all transactions")
	void getAllTransaction_ReturnsAllTransactions() throws Exception {
		// Arrange
		List<PanelTransaction> transactions = Arrays.asList(
				createMockPanelTransaction(1L),
				createMockPanelTransaction(2L));

		// Mock the service method to return our transactions
		when(panelTransactionService.fetchAll()).thenReturn(transactions);

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transaction/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Returns transactions for a specific user")
	void getTransactionByUserId_ReturnsUserTransactions() throws Exception {
		// Arrange
		Long userId = 1L;
		List<PanelTransaction> transactions = Arrays.asList(
				createMockPanelTransaction(userId),
				createMockPanelTransaction(userId));

		// Mock the service method to return our transactions
		when(panelTransactionService.fetchById(userId)).thenReturn(transactions);

		// Act & Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transaction/{id}", userId)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 200 OK: Returns staff transaction history")
	void getStaffTransactionHistory_ReturnsStaffTransactions() throws Exception {
		// Arrange
		// Using the record constructor pattern to match the actual DTO structure
		List<StaffTransactionDTO> staffTransactions = Arrays.asList(
				new StaffTransactionDTO("2023-01-01", "User1", "USA", "Solar Panel A", 3, 400, 250.0),
				new StaffTransactionDTO("2023-01-02", "User2", "Germany", "Solar Panel B", 1, 350, 175.0));

		when(panelTransactionService.getStaffTransactionHistory()).thenReturn(staffTransactions);

		// Act & Assert
		mockMvc.perform(get("/api/v1/transaction/staff")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].date").value("2023-01-01"))
				.andExpect(jsonPath("$[0].user").value("User1"))
				.andExpect(jsonPath("$[1].panelType").value("Solar Panel B"));
	}

	@Test
	@WithMockUser
	@DisplayName("HTTP 400 Bad Request: Fails when transaction data is invalid")
	void addTransaction_WithInvalidData_ReturnsBadRequest() throws Exception {
		// Arrange - create invalid data (missing required fields)
		String invalidJson = "{\"userId\": null, \"panelId\": 2}";

		// Act & Assert
		mockMvc.perform(post("/api/v1/transaction/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
				.andExpect(status().isBadRequest());
	}

	// Helper method to create properly mocked PanelTransaction objects
	private PanelTransaction createMockPanelTransaction(Long userId) {
		PanelTransaction transaction = Mockito.mock(PanelTransaction.class);

		// Mock User
		User mockUser = Mockito.mock(User.class);
		when(mockUser.getId()).thenReturn(userId);
		when(mockUser.getEmail()).thenReturn("user" + userId + "@example.com");
		when(mockUser.getFullName()).thenReturn("User " + userId);
		when(mockUser.getDetail(any())).thenReturn(mockUser);

		// Mock CountryPanel with all required nested objects
		CountryPanel mockPanel = Mockito.mock(CountryPanel.class);
		Country mockCountry = Mockito.mock(Country.class);
		Panel mockSolarPanel = Mockito.mock(Panel.class);

		when(mockCountry.getCode()).thenReturn("US");
		when(mockSolarPanel.getName()).thenReturn("Test Panel");
		when(mockSolarPanel.getInstallationCost()).thenReturn(100.0);
		when(mockSolarPanel.getProductionPerPanel()).thenReturn(200.0);
		when(mockSolarPanel.getDescription()).thenReturn("Test Description");
		when(mockSolarPanel.getEfficiency()).thenReturn("90%");
		when(mockSolarPanel.getLifespan()).thenReturn("25 years");
		when(mockSolarPanel.getTemperatureTolerance()).thenReturn("80°C");
		when(mockSolarPanel.getWarranty()).thenReturn("10 years");

		when(mockPanel.getId()).thenReturn(1L);
		when(mockPanel.getCountry()).thenReturn(mockCountry);
		when(mockPanel.getPanel()).thenReturn(mockSolarPanel);

		// Mock Payment
		Payment mockPayment = Mockito.mock(Payment.class);
		when(mockPayment.getAmount()).thenReturn(500);
		when(mockPayment.getType()).thenReturn("CREDIT_CARD");

		// Set up transaction with all mocked components
		when(transaction.getId()).thenReturn(userId);
		when(transaction.getUser()).thenReturn(mockUser);
		when(transaction.getPanel()).thenReturn(mockPanel);
		when(transaction.getPayment()).thenReturn(mockPayment);

		return transaction;
	}

}