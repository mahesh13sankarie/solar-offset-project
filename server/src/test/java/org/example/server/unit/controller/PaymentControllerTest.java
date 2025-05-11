package org.example.server.unit.controller;

import org.example.server.controller.PaymentController;
import org.example.server.dto.PaymentRequestDTO;
import org.example.server.dto.PaymentResponseDTO;
import org.example.server.exception.PaymentException;
import org.example.server.service.Payment.PaymentService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.PaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaymentControllerTest {

    private PaymentController paymentController;
    private PaymentService paymentService;

    private PaymentRequestDTO validRequest;
    private PaymentResponseDTO successResponse;

    @BeforeEach
    void setUp() {
        // Mock the service layer
        paymentService = mock(PaymentService.class);

        // Create controller with mocked dependency
        paymentController = new PaymentController(paymentService);

        // Setup valid payment request
        validRequest = new PaymentRequestDTO(
                1L,
                100L,
                2,
                PaymentType.STRIPE,
                "pm_card_visa");

        // Setup mock response
        successResponse = new PaymentResponseDTO(
                1L,
                "tx_123456789",
                "https://receipt.example.com/123456789");
    }

    @Test
    @DisplayName("HTTP 200 OK: Successfully processes payment with valid payment details")
    void shouldProcessValidPayment() {
        // Arrange
        when(paymentService.processPayment(any(PaymentRequestDTO.class)))
                .thenReturn(successResponse);

        // Act
        ApiResponse<ApiResponse.CustomBody<PaymentResponseDTO>> response = paymentController
                .processPayment(validRequest);
        System.out.println("Response: " + response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getSuccess());
        assertEquals(successResponse, response.getBody().getResponse());
    }

    @Test
    @DisplayName("HTTP 400 BAD_REQUEST: Rejects payment when user ID is missing")
    void shouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        PaymentRequestDTO invalidRequest = new PaymentRequestDTO(
                null,
                100L,
                2,
                PaymentType.STRIPE,
                "pm_card_visa");

        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentController.processPayment(invalidRequest));

        System.out.println("User ID null exception - Code: " + exception.getCode()
                + ", Message: " + exception.getMessage()
                + ", Status: " + exception.getStatus());

        assertEquals(PaymentException.VALIDATION_ERROR, exception.getCode());
        assertEquals("User ID must not be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("HTTP 400 BAD_REQUEST: Rejects payment when country panel ID is missing")
    void shouldThrowExceptionWhenCountryPanelIdIsNull() {
        // Arrange
        PaymentRequestDTO invalidRequest = new PaymentRequestDTO(
                1L,
                null,
                2,
                PaymentType.STRIPE,
                "pm_card_visa");

        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentController.processPayment(invalidRequest));

        System.out.println("Country Panel ID null exception - Code: " + exception.getCode()
                + ", Message: " + exception.getMessage()
                + ", Status: " + exception.getStatus());

        assertEquals(PaymentException.VALIDATION_ERROR, exception.getCode());
        assertEquals("Country Panel ID must not be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("HTTP 400 BAD_REQUEST: Rejects payment when quantity is missing")
    void shouldThrowExceptionWhenQuantityIsNull() {
        // Arrange
        PaymentRequestDTO invalidRequest = new PaymentRequestDTO(
                1L,
                100L,
                null,
                PaymentType.STRIPE,
                "pm_card_visa");

        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentController.processPayment(invalidRequest));

        System.out.println("Quantity null exception - Code: " + exception.getCode()
                + ", Message: " + exception.getMessage()
                + ", Status: " + exception.getStatus());

        assertEquals(PaymentException.VALIDATION_ERROR, exception.getCode());
        assertEquals("Quantity must not be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("HTTP 400 BAD_REQUEST: Rejects payment when payment type is missing")
    void shouldThrowExceptionWhenPaymentTypeIsNull() {
        // Arrange
        PaymentRequestDTO invalidRequest = new PaymentRequestDTO(
                1L,
                100L,
                2,
                null,
                "pm_card_visa");

        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentController.processPayment(invalidRequest));

        System.out.println("Payment Type null exception - Code: " + exception.getCode()
                + ", Message: " + exception.getMessage()
                + ", Status: " + exception.getStatus());

        assertEquals(PaymentException.VALIDATION_ERROR, exception.getCode());
        assertEquals("Payment type must not be null", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("HTTP 400 BAD_REQUEST: Rejects payment when payment method ID is missing")
    void shouldThrowExceptionWhenPaymentMethodIdIsNull() {
        // Arrange
        PaymentRequestDTO invalidRequest = new PaymentRequestDTO(
                1L,
                100L,
                2,
                PaymentType.STRIPE,
                null);

        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentController.processPayment(invalidRequest));

        System.out.println("Payment Method ID null exception - Code: " + exception.getCode()
                + ", Message: " + exception.getMessage()
                + ", Status: " + exception.getStatus());

        assertEquals(PaymentException.PAYMENT_METHOD_REQUIRED_ERROR, exception.getCode());
        assertEquals("Payment method ID must not be null or empty", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("HTTP 400 BAD_REQUEST: Rejects payment when payment method ID is empty")
    void shouldThrowExceptionWhenPaymentMethodIdIsEmpty() {
        // Arrange
        PaymentRequestDTO invalidRequest = new PaymentRequestDTO(
                1L,
                100L,
                2,
                PaymentType.STRIPE,
                "");

        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentController.processPayment(invalidRequest));

        System.out.println("Payment Method ID empty exception - Code: " + exception.getCode()
                + ", Message: " + exception.getMessage()
                + ", Status: " + exception.getStatus());

        assertEquals(PaymentException.PAYMENT_METHOD_REQUIRED_ERROR, exception.getCode());
        assertEquals("Payment method ID must not be null or empty", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    @DisplayName("HTTP 500 INTERNAL_SERVER_ERROR: Forwards payment service exceptions to client")
    void shouldPropagateServiceExceptions() {
        // Arrange
        when(paymentService.processPayment(any(PaymentRequestDTO.class)))
                .thenThrow(new PaymentException(
                        "PAYMENT_PROCESSING_ERROR",
                        "Error processing payment",
                        HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        PaymentException exception = assertThrows(PaymentException.class,
                () -> paymentController.processPayment(validRequest));

        System.out.println("Service exception - Code: " + exception.getCode()
                + ", Message: " + exception.getMessage()
                + ", Status: " + exception.getStatus());

        assertEquals("PAYMENT_PROCESSING_ERROR", exception.getCode());
        assertEquals("Error processing payment", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
    }
}