package org.example.server.dto;

public record PaymentResponseDTO(
		Long paymentId,
		String transactionId,
		String receiptUrl) {
}