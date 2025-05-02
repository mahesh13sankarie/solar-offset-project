package org.example.server.utils;

import org.example.server.exception.DataNotFoundException;
import org.example.server.exception.PaymentException;
import org.example.server.exception.PanelTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Handler for custom payment exceptions
	@ExceptionHandler(PaymentException.class)
	public ApiResponse<ApiResponse.CustomBody<Object>> handlePaymentExceptions(PaymentException ex) {
		return ApiResponseGenerator.fail(
				ex.getCode(),
				ex.getMessage(),
				ex.getStatus());
	}

	// Handler for panel transaction exceptions
	@ExceptionHandler(PanelTransactionException.class)
	public ApiResponse<ApiResponse.CustomBody<Object>> handlePanelTransactionExceptions(PanelTransactionException ex) {
		return ApiResponseGenerator.fail(
				ex.getCode(),
				ex.getMessage(),
				ex.getStatus());
	}

	// Handler for data not found exceptions
	@ExceptionHandler(DataNotFoundException.class)
	public ApiResponse<ApiResponse.CustomBody<Object>> handleDataNotFoundException(DataNotFoundException ex) {
		return ApiResponseGenerator.fail(
				"DATA-001", // Using a consistent code pattern
				ex.getMessage(),
				HttpStatus.NOT_FOUND);
	}
}