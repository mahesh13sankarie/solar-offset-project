package org.example.server.controller;

import org.example.server.dto.PaymentRequestDTO;
import org.example.server.dto.PaymentResponseDTO;
import org.example.server.exception.PaymentException;
import org.example.server.service.Payment.PaymentService;
import org.example.server.utils.ApiResponse;
import org.example.server.utils.ApiResponseGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping
	public ApiResponse<ApiResponse.CustomBody<PaymentResponseDTO>> processPayment(
			@RequestBody PaymentRequestDTO request) {
		validatePaymentRequest(request);
		return ApiResponseGenerator.success(HttpStatus.OK, paymentService.processPayment(request));
	}

	/**
	 * Validate payment request parameters
	 * 
	 * @param request The payment request to validate
	 * @throws PaymentException if validation fails
	 */
	private void validatePaymentRequest(PaymentRequestDTO request) {
		if (request.userId() == null) {
			throw new PaymentException(
					PaymentException.VALIDATION_ERROR,
					"User ID must not be null",
					HttpStatus.BAD_REQUEST);
		}

		if (request.countryPanelId() == null) {
			throw new PaymentException(
					PaymentException.VALIDATION_ERROR,
					"Country Panel ID must not be null",
					HttpStatus.BAD_REQUEST);
		}

		if (request.quantity() == null) {
			throw new PaymentException(
					PaymentException.VALIDATION_ERROR,
					"Quantity must not be null",
					HttpStatus.BAD_REQUEST);
		}

		if (request.paymentType() == null) {
			throw new PaymentException(
					PaymentException.VALIDATION_ERROR,
					"Payment type must not be null",
					HttpStatus.BAD_REQUEST);
		}

		if (request.paymentMethodId() == null || request.paymentMethodId().isBlank()) {
			throw new PaymentException(
					PaymentException.PAYMENT_METHOD_REQUIRED_ERROR,
					"Payment method ID must not be null or empty",
					HttpStatus.BAD_REQUEST);
		}
	}
}
