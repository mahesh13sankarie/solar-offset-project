package org.example.server.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class PanelTransactionException extends RuntimeException {

	// Error codes
	public static final String PANEL_NOT_FOUND_ERROR = "PANEL_TRX-001";
	public static final String USER_NOT_FOUND_ERROR = "PANEL_TRX-002";
	public static final String TRANSACTION_PROCESSING_ERROR = "PANEL_TRX-003";

	private final String code;
	private final HttpStatus status;

	public PanelTransactionException(String message) {
		super(message);
		this.code = TRANSACTION_PROCESSING_ERROR;
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public PanelTransactionException(String code, String message) {
		super(message);
		this.code = code;
		this.status = HttpStatus.BAD_REQUEST;
	}

	public PanelTransactionException(String code, String message, HttpStatus status) {
		super(message);
		this.code = code;
		this.status = status;
	}

	/**
	 * Create exception for panel not found
	 * 
	 * @param panelId ID of the panel not found
	 * @return PanelTransactionException with panel not found error
	 */
	public static PanelTransactionException panelNotFound(Long panelId) {
		return new PanelTransactionException(
				PANEL_NOT_FOUND_ERROR,
				"Panel not found with ID: " + panelId,
				HttpStatus.NOT_FOUND);
	}

	/**
	 * Create exception for user not found
	 * 
	 * @param userId ID of the user not found
	 * @return PanelTransactionException with user not found error
	 */
	public static PanelTransactionException userNotFound(Long userId) {
		return new PanelTransactionException(
				USER_NOT_FOUND_ERROR,
				"User not found with ID: " + userId,
				HttpStatus.NOT_FOUND);
	}

	public static PanelTransactionException paymentNotFound(Long paymentId) {
		return new PanelTransactionException(
				PANEL_NOT_FOUND_ERROR,
				"Payment not found with ID: " + paymentId,
				HttpStatus.NOT_FOUND);
	}
}