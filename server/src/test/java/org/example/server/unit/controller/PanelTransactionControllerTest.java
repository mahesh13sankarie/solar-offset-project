package org.example.server.unit.controller;

import org.example.server.controller.PanelTransactionController;
import org.example.server.dto.PanelTransactionDTO;
import org.example.server.dto.StaffTransactionDTO;
import org.example.server.entity.*;
import org.example.server.entity.response.PanelTransactionResponse;
import org.example.server.service.panel.PanelTransactionService;
import org.example.server.utils.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PanelTransactionControllerTest {

    private PanelTransactionController panelTransactionController;
    private PanelTransactionService panelTransactionService;

    @BeforeEach
    void setUp() {
        // Mock the service layer
        panelTransactionService = mock(PanelTransactionService.class);

        // Create controller with mocked dependency
        panelTransactionController = new PanelTransactionController();

        // Set panelTransactionService field using reflection since it's autowired
        try {
            java.lang.reflect.Field field = PanelTransactionController.class
                    .getDeclaredField("panelTransactionService");
            field.setAccessible(true);
            field.set(panelTransactionController, panelTransactionService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set mock service", e);
        }
    }

    @Test
    @DisplayName("HTTP 200 OK: Successfully adds a new transaction")
    void addTransaction_WithValidData_ReturnsSuccess() {
        // Arrange
        PanelTransactionDTO validTransaction = new PanelTransactionDTO(1L, 2L, 3L);

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelTransactionController
                .addTransaction(validTransaction);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNull(response.getBody().getError());

        // Verify service method was called
        verify(panelTransactionService).save(validTransaction);
    }

    @Test
    @DisplayName("HTTP 400 BAD REQUEST: Returns error when user ID is missing")
    void addTransaction_WithMissingUserId_ReturnsBadRequest() {
        // Arrange
        PanelTransactionDTO invalidTransaction = new PanelTransactionDTO(null, 2L, 3L);

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelTransactionController
                .addTransaction(invalidTransaction);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNotNull(response.getBody().getError());
        assertEquals("VALIDATION_ERROR", response.getBody().getError().getCode());
        assertEquals("User ID must not be null", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 400 BAD REQUEST: Returns error when panel ID is missing")
    void addTransaction_WithMissingPanelId_ReturnsBadRequest() {
        // Arrange
        PanelTransactionDTO invalidTransaction = new PanelTransactionDTO(1L, null, 3L);

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelTransactionController
                .addTransaction(invalidTransaction);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNotNull(response.getBody().getError());
        assertEquals("VALIDATION_ERROR", response.getBody().getError().getCode());
        assertEquals("Panel ID must not be null", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 400 BAD REQUEST: Returns error when payment ID is missing")
    void addTransaction_WithMissingPaymentId_ReturnsBadRequest() {
        // Arrange
        PanelTransactionDTO invalidTransaction = new PanelTransactionDTO(1L, 2L, null);

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelTransactionController
                .addTransaction(invalidTransaction);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNotNull(response.getBody().getError());
        assertEquals("VALIDATION_ERROR", response.getBody().getError().getCode());
        assertEquals("Payment ID must not be null", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL SERVER ERROR: Returns error when unexpected exception occurs during transaction creation")
    void addTransaction_WithServiceException_ReturnsServerError() {
        // Arrange
        PanelTransactionDTO validTransaction = new PanelTransactionDTO(1L, 2L, 3L);
        doThrow(new RuntimeException("Database error")).when(panelTransactionService)
                .save(any(PanelTransactionDTO.class));

        // Act
        ApiResponse<ApiResponse.CustomBody<Object>> response = panelTransactionController
                .addTransaction(validTransaction);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNotNull(response.getBody().getError());
        assertEquals("SERVER_ERROR", response.getBody().getError().getCode());
        assertEquals("An unexpected error occurred", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Successfully returns all transactions")
    void getAllTransaction_ReturnsTransactionList() {
        // Arrange
        List<PanelTransaction> transactions = createMockTransactions();
        when(panelTransactionService.fetchAll()).thenReturn(transactions);

        // Act
        ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> response = panelTransactionController
                .getAllTransaction();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNotNull(response.getBody().getResponse());
        assertEquals(2, response.getBody().getResponse().size());
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL SERVER ERROR: Returns error when exception occurs fetching all transactions")
    void getAllTransaction_WithServiceException_ReturnsServerError() {
        // Arrange
        when(panelTransactionService.fetchAll()).thenThrow(new RuntimeException("Database error"));

        // Act
        ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> response = panelTransactionController
                .getAllTransaction();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("SERVER_ERROR", response.getBody().getError().getCode());
        assertEquals("Failed to fetch transactions", response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Successfully returns transactions for a specific user")
    void getTransactionByUserId_ReturnsUserTransactions() {
        // Arrange
        Long userId = 1L;
        List<PanelTransaction> transactions = createMockTransactions();
        when(panelTransactionService.fetchById(userId)).thenReturn(transactions);

        // Act
        ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> response = panelTransactionController
                .getTransactionByUserId(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNotNull(response.getBody().getResponse());
        assertEquals(2, response.getBody().getResponse().size());
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL SERVER ERROR: Returns error when exception occurs fetching user transactions")
    void getTransactionByUserId_WithServiceException_ReturnsServerError() {
        // Arrange
        Long userId = 1L;
        when(panelTransactionService.fetchById(userId)).thenThrow(new RuntimeException("Database error"));

        // Act
        ApiResponse<ApiResponse.CustomBody<List<PanelTransactionResponse>>> response = panelTransactionController
                .getTransactionByUserId(userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("SERVER_ERROR", response.getBody().getError().getCode());
        assertEquals("Failed to fetch transactions for user: " + userId, response.getBody().getError().getMessage());
    }

    @Test
    @DisplayName("HTTP 200 OK: Successfully returns staff transaction history")
    void getStaffTransactionHistory_ReturnsTransactionList() {
        // Arrange
        List<StaffTransactionDTO> staffTransactions = new ArrayList<>();
        staffTransactions.add(mock(StaffTransactionDTO.class));
        staffTransactions.add(mock(StaffTransactionDTO.class));

        when(panelTransactionService.getStaffTransactionHistory()).thenReturn(staffTransactions);

        // Act
        ApiResponse<ApiResponse.CustomBody<List<StaffTransactionDTO>>> response = panelTransactionController
                .getStaffTransactionHistory();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertNotNull(response.getBody().getResponse());
        assertEquals(2, response.getBody().getResponse().size());
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL SERVER ERROR: Returns error when exception occurs fetching staff transaction history")
    void getStaffTransactionHistory_WithServiceException_ReturnsServerError() {
        // Arrange
        when(panelTransactionService.getStaffTransactionHistory()).thenThrow(new RuntimeException("Database error"));

        // Act
        ApiResponse<ApiResponse.CustomBody<List<StaffTransactionDTO>>> response = panelTransactionController
                .getStaffTransactionHistory();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().getSuccess());
        assertNull(response.getBody().getResponse());
        assertNotNull(response.getBody().getError());
        assertEquals("SERVER_ERROR", response.getBody().getError().getCode());
        assertEquals("Failed to fetch staff transaction history", response.getBody().getError().getMessage());
    }

    // Helper method to create mock panel transactions
    private List<PanelTransaction> createMockTransactions() {
        // Mock all objects instead of creating them directly to avoid constructor
        // visibility issues
        User user = mock(User.class);
        when(user.getDetail(any())).thenReturn(user);

        Panel panel1 = mock(Panel.class);
        when(panel1.getName()).thenReturn("Solar Panel A");
        when(panel1.getInstallationCost()).thenReturn(400.00);
        when(panel1.getProductionPerPanel()).thenReturn(445.00);
        when(panel1.getDescription()).thenReturn("High-efficiency panel");
        when(panel1.getEfficiency()).thenReturn("22.8");
        when(panel1.getWarranty()).thenReturn("25 years");
        when(panel1.getId()).thenReturn(1L);

        Panel panel2 = mock(Panel.class);
        when(panel2.getName()).thenReturn("Solar Panel B");
        when(panel2.getInstallationCost()).thenReturn(350.00);
        when(panel2.getProductionPerPanel()).thenReturn(420.00);
        when(panel2.getDescription()).thenReturn("Budget-friendly panel");
        when(panel2.getEfficiency()).thenReturn("21.5");
        when(panel2.getWarranty()).thenReturn("20 years");
        when(panel2.getId()).thenReturn(2L);

        Country country = mock(Country.class);
        when(country.getCode()).thenReturn("GB");

        CountryPanel countryPanel1 = mock(CountryPanel.class);
        when(countryPanel1.getPanel()).thenReturn(panel1);
        when(countryPanel1.getCountry()).thenReturn(country);

        CountryPanel countryPanel2 = mock(CountryPanel.class);
        when(countryPanel2.getPanel()).thenReturn(panel2);
        when(countryPanel2.getCountry()).thenReturn(country);

        Payment payment1 = mock(Payment.class);
        when(payment1.getAmount()).thenReturn(1000);
        when(payment1.getType()).thenReturn("STRIPE");
        when(payment1.getTransactionId()).thenReturn("TX123");
        when(payment1.getCountryPanel()).thenReturn(countryPanel1);

        Payment payment2 = mock(Payment.class);
        when(payment2.getAmount()).thenReturn(1500);
        when(payment2.getType()).thenReturn("PAYPAL");
        when(payment2.getTransactionId()).thenReturn("TX456");
        when(payment2.getCountryPanel()).thenReturn(countryPanel2);

        PanelTransaction transaction1 = mock(PanelTransaction.class);
        when(transaction1.getId()).thenReturn(1L);
        when(transaction1.getUser()).thenReturn(user);
        when(transaction1.getPayment()).thenReturn(payment1);

        PanelTransaction transaction2 = mock(PanelTransaction.class);
        when(transaction2.getId()).thenReturn(2L);
        when(transaction2.getUser()).thenReturn(user);
        when(transaction2.getPayment()).thenReturn(payment2);

        return Arrays.asList(transaction1, transaction2);
    }
}