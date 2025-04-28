package org.example.server.exception;

import org.springframework.http.HttpStatus;

import com.stripe.exception.StripeException;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {

	// Validation error codes
	public static final String VALIDATION_ERROR = "PAYMENT-001";
	public static final String PAYMENT_PROCESSING_ERROR = "PAYMENT-002";
	public static final String USER_NOT_FOUND_ERROR = "PAYMENT-005";
	public static final String COUNTRY_PANEL_NOT_FOUND_ERROR = "PAYMENT-006";
	public static final String PAYMENT_METHOD_REQUIRED_ERROR = "PAYMENT-007";

	private final String code;
	private final HttpStatus status;

	public PaymentException(String message) {
		super(message);
		this.code = PAYMENT_PROCESSING_ERROR;
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public PaymentException(String code, String message) {
		super(message);
		this.code = code;
		this.status = HttpStatus.BAD_REQUEST;
	}

	public PaymentException(String code, String message, HttpStatus status) {
		super(message);
		this.code = code;
		this.status = status;
	}

	/**
	 * Create PaymentException from StripeException
	 * 
	 * @param ex StripeException to convert
	 * @return PaymentException with appropriate error code and message
	 */
	public static PaymentException fromStripeException(StripeException ex) {
		return new PaymentException(
				PAYMENT_PROCESSING_ERROR,
				ex.getMessage(),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Create exception for user not found
	 * 
	 * @param userId ID of the user not found
	 * @return PaymentException with user not found error
	 */
	public static PaymentException userNotFound(Long userId) {
		return new PaymentException(
				USER_NOT_FOUND_ERROR,
				"User not found with ID: " + userId,
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Create exception for country panel not found
	 * 
	 * @param countryPanelId ID of the country panel not found
	 * @return PaymentException with country panel not found error
	 */
	public static PaymentException countryPanelNotFound(Long countryPanelId) {
		return new PaymentException(
				COUNTRY_PANEL_NOT_FOUND_ERROR,
				"Country panel not found with ID: " + countryPanelId,
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Create exception for payment method validation
	 * 
	 * @return PaymentException with payment method error
	 */
	public static PaymentException paymentMethodRequired() {
		return new PaymentException(
				PAYMENT_METHOD_REQUIRED_ERROR,
				"Payment method ID is required",
				HttpStatus.BAD_REQUEST);
	}
}