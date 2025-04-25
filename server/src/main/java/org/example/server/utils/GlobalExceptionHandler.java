package org.example.server.utils;

import org.example.server.exception.PaymentException;
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
}